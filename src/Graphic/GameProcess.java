package Graphic;

import InputProcess.KeyHandler;
import InputProcess.Setter;
import GameLogic.AttackProcess;
import GameLogic.CollisionCheck;
import GameLogic.PathFinder;
import OutputProcess.MobProcess;
import OutputProcess.NpcProcess;
import OutputProcess.ObjectProcess;
import OutputProcess.PlayerProcess;
import entity.Mob;
import entity.NPC;
import entity.Player;
import map.Map;
import object.SuperObject;

import javax.swing.*;
import java.awt.*;

public class GameProcess extends JPanel implements Runnable {

    //public GameAttribute ga;

    // SCREEN SETTING
    private final int orgTileSize = 16; // 16x16 tile
    private final int scale = 2;

    public final int tileSize = orgTileSize * scale;
    public final int MaxScreenCol = 45;
    public final int MaxScreenRow = 25;
    public final int screenWidth = tileSize * MaxScreenCol;
    public final int screenHeight = tileSize * MaxScreenRow;

    // WORLD SETTING
    public final int maxWorldCol = MaxScreenCol;
    public final int maxWorldRow = MaxScreenRow;

    public int currentMap;

    public Map[] map;
    public MapDraw mapDraw;

    /// key input handling
    KeyHandler keyH1;

    UI ui;

    /// using thread to run game
    Thread gameThread;

    /// player
    public PlayerProcess PProcess;
    public Player player;

    /// NPC
    public NpcProcess NProcess;

    /// Mob
    public MobProcess MProcess;

    public ObjectProcess objProcess;

    public int FPS;

    public AttackProcess aProcess;

    public CollisionCheck cCheck;

    public PathFinder pFinder;

    public Setter setter;

    public enum GameState {Title, Run, Pause, Dialog, InventoryOpen, Wasted, Respawn}
    public GameState gameState;

    public GameProcess() {

        mapDraw = new MapDraw(this);

        //tileM = new TileManager(this);
        player = new Player();
        keyH1 = new KeyHandler(this);
        ui = new UI(this);

        this.PProcess = new PlayerProcess(this, keyH1, this.player);

        this.NProcess = new NpcProcess(this);
        this.objProcess = new ObjectProcess(this);
        this.MProcess = new MobProcess(this);

        this.aProcess = new AttackProcess(this);

        this.cCheck = new CollisionCheck(this);
        this.setter = new Setter(this);
        this.pFinder = new PathFinder(this);

        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH1);
        this.setFocusable(true);

    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public UI getUi() {
        return ui;
    }

//    }
    public Player getPlayer() {
        return this.player;
    }
//    }
    public CollisionCheck getcCheck() {
        return this.cCheck;
    }

    public void setOBJ(int i, SuperObject obj) {
        map[currentMap].getObjs()[i] = obj;
    }


    public void setupGame() {

        map = new Map[10];

        this.setGameState(GameState.Title);

        for(int i = 1; i <= 2; i++) {
            map[i] = new Map(i);
            setter.load(map[i]);

            NPC[] npc = map[i].getNpcs();
            Mob[] mob = map[i].getMobs();
            SuperObject[] obj = map[i].getObjs();

            for (NPC now : npc) {
                if (now != null) {
                    System.out.println(now.getName());
                    NProcess.getImg(now);
                }
            }

            for(Mob now : mob) {
                if(now != null) {
                    System.out.println(now.getName());
                    MProcess.getImg(now);
                }
            }

            for(SuperObject now : obj) {
                if(now != null) {
                    System.out.println(now.getName());
                    objProcess.getImg(now);
                }
            }

        }

        this.currentMap = 1;

        pFinder.Prepare();

    }

    public void startGameThread(){
        gameThread = new Thread(this);
        gameThread.start();
    }

    public void run() {
        /// FPS
        FPS = 60;
        double drawInterval = (double) 1000000000 / FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        long timer = 0;
        int drawCount = 0;

        while (gameThread != null) {
            currentTime = System.nanoTime();

            delta += (currentTime - lastTime) / drawInterval;
            timer += (currentTime - lastTime);

            lastTime = currentTime;

            if(delta >= 1) {
                update();
                repaint();
                delta--;
                drawCount++;
            }
            if(timer >= 1000000000) {
                FPS = drawCount;
                drawCount = 0;
                timer = 0;
            }
        }
    }

    public void update()  {

        NPC[] npc = map[currentMap].getNpcs();
        Mob[] mob = map[currentMap].getMobs();
        //SuperObject[] obj = map[currentMap].getObjs();

        int now = PProcess.update();
        //System.out.println(gameState);
        if(gameState == GameState.Run) {

            /// Update Player
            if(now != 0) {
                //String Des = "/maps/map0" + now + ".txt";
                switch (now) {
                    case 1 -> {
                        getPlayer().setPos(43 * tileSize, 12 * tileSize);
                        getPlayer().setState(Player.State.idle);
                    }
                    case 2 -> {
                        getPlayer().setPos(tileSize, 20 * tileSize);
                        getPlayer().setState(Player.State.idle);
                    }
                    default -> {
                    }
                }

                this.currentMap = now;
                PProcess.teleport = 0;

                mapDraw.drawPath = false;
                //pFinder.resetNodes();
                pFinder.Prepare();

            }

            /// Update NPC
            for (NPC thisNpc : npc) {
                if (thisNpc != null)
                    NProcess.update(thisNpc);
            }

            for (Mob thisMob : mob) {
                if (thisMob != null)
                    MProcess.update(thisMob);
            }

        }
        else if(gameState == GameState.Pause) {
            // do something
        }
        else if(gameState == GameState.Dialog) {
        }
        else if(gameState == GameState.Respawn){
            getPlayer().respawn();
            setGameState(GameState.Run);
        }
    }

    public void drawGame(Graphics2D g2) {

        mapDraw.draw(g2, map[currentMap]);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D)g;

        switch (gameState) {
            case Title, Wasted -> ui.draw(g2);
            default -> {

                drawGame(g2);
                PProcess.draw(g2);

                /// UI
                ui.draw(g2);
            }
        }
        g2.dispose();

    }

}
