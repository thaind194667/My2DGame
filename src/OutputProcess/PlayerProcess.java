package OutputProcess;

import Graphic.GameProcess;
import entity.Mob;
import entity.NPC;
import InputProcess.KeyHandler;
import entity.Player;
import object.SuperObject;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class PlayerProcess extends EntityProcess {

    public BufferedImage[] up;
    public BufferedImage[] down;
    public BufferedImage[] left;
    public BufferedImage[] right;
    public BufferedImage pUp, pDown, pLeft, pRight;//projectile attacking  //PHU
    public BufferedImage mUp, mDown, mLeft, mRight;//melee attacking  //PHU
    KeyHandler keyH;
    private final Player player;
    int spriteCounter = 0;
    int spriteNum = 1;
    public int teleport = 0;
    int damageCounter = 0;

    int attackCounter = 0;

    int expGet;

    public PlayerProcess(GameProcess gp, KeyHandler keyH, Player player) {
        super(gp);
        this.keyH = keyH;
        this.player = player;

        getPlayerImage();
        getMeleeImage();
        getProjectileImage();
    }

    public void getPlayerImage() {
        this.up = new BufferedImage[5];
        this.down = new BufferedImage[5];
        this.left = new BufferedImage[5];
        this.right = new BufferedImage[5];
        for(int i = 1; i <= 4; i++) {
            this.up[i] = loadImg("up" + i);
            this.down[i] = loadImg("down" + i);
            this.left[i] = loadImg("left" + i);
            this.right[i] = loadImg("right" + i);
        }

    }

    public void getProjectileImage() {//PHU
        this.pUp = loadImg("fireball_up");
        this.pDown = loadImg("fireball_down");
        this.pLeft = loadImg("fireball_left");
        this.pRight = loadImg("fireball_right");
    }

    public void getMeleeImage() {//PHU
        this.mUp = loadImg("sword_up");
        this.mDown = loadImg("sword_down");
        this.mLeft = loadImg("sword_left");
        this.mRight = loadImg("sword_right");
    }

    public BufferedImage loadImg(String img) {
        //UtilityTool uT = new UtilityTool();

        BufferedImage image = null;
        try {
            image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/players/" + img + ".png")));
            //image = uT.scaleImg(image, gp.tileSize, gp.tileSize);
        }catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    boolean checkNpcCollision() {
        //int currentMap = gp.currentMap;
        NPC[] npc = gp.map[gp.currentMap].getNpcs();
        for(int i = 0; i < npc.length; i++) {
            NPC now = npc[i];
            if(now != null) {
                player.collisionOn = false;
                gp.getcCheck().CheckEntityCollision(this.player, now);
                if(player.collisionOn) {
                    //currentCollision = true;
                    InteractNPC(i);
                    return true;
                }
            }

        }
        return false;
    }

    void checkMobCollision() {
        int currentMap = gp.currentMap;
        Mob[] mob = gp.map[currentMap].getMobs();
        for(int i = 0; i < mob.length; i++) {
            Mob now = mob[i];
            if(now != null && now.getState() != Mob.mobState.die) {
                player.collisionOn = false;
                gp.getcCheck().CheckEntityCollision(this.player, now);

                if(player.collisionOn) {
                    damageCounter++;
                    if(damageCounter == 1) {
                        player.getAttacked(now.getAttack());
                    }

                    if(damageCounter == 60) damageCounter = 0;
                    return;
                }
                else damageCounter = 0;
            }
        }
    }

    boolean checkOBJCollision() {
        int currentMap = gp.currentMap;
        SuperObject[] obj = gp.map[currentMap].getObjs();
        for(int i = 0; i < obj.length; i++) {
            SuperObject now = obj[i];
            if(now != null) {
                player.collisionOn = false;
                gp.getcCheck().checkObjCollisionOnPlayer(this.player, i);

                if(player.collisionOn) {
                    return true;
                }
            }
        }
        return false;
    }

    public int updateMove() {
        int worldX = this.player.getPosX();
        int worldY = this.player.getPosY();
        int speed = this.player.getSpeed();

        if(!player.collisionOn && teleport == 0) {
            switch (this.player.getDirection()) {
                case "up" -> this.player.setPosY(worldY - speed);//worldY -= speed;
                case "down" -> this.player.setPosY(worldY + speed);
                case "left" -> this.player.setPosX(worldX - speed);
                case "right" -> this.player.setPosX(worldX + speed);
            }

        }

        spriteCounter++;
        if(spriteCounter > 10) {
            spriteNum = (spriteNum + 1) % 4;
            if(spriteNum == 0) spriteNum = 4;
            spriteCounter = 0;
        }

        if(this.player.getPosX() % gp.tileSize == 0 && this.player.getPosY() % gp.tileSize == 0) {
            //moving = false;
            this.player.setState(Player.State.idle);
        }
        return 0;
    }


    public int updateStand() {
        if (keyH.upPressed || keyH.downPressed || keyH.leftPressed || keyH.rightPressed) {
            if (keyH.upPressed) {
                this.player.setDirection("up");
            } else if (keyH.downPressed) {
                this.player.setDirection("down");
            } else if (keyH.leftPressed) {
                this.player.setDirection("left");
            } else {
                this.player.setDirection("right");
            }
            if(keyH.sprintPressed) this.player.setSpeed(this.player.playerDefaultSpeed * 2);
            else if(keyH.crouchPressed) this.player.setSpeed(this.player.playerDefaultSpeed / 2);
            else this.player.setSpeed(this.player.playerDefaultSpeed);

            //moving = true;
            this.player.setState(Player.State.move);
            /// check tile collision
            player.collisionOn = false;

            gp.getcCheck().checkTile(this.player);
            boolean currentCollision = player.collisionOn;
            teleport = gp.getcCheck().checkTeleportTile(this.player);
            if(teleport != 0) {
                return teleport;
            }

            checkMobCollision();

            if(checkNpcCollision() || checkOBJCollision())
                player.collisionOn = true;
            if(currentCollision) player.collisionOn = true;

        }
        else {
            checkNpcCollision();
        }
        if(keyH.attackPressed)
            player.setState(Player.State.melee);
        if(keyH.attack2Pressed)
            player.setState(Player.State.projectile);
        return 0;
    }

    public void updateAttack() {
        int currentMap = gp.currentMap;
        Mob[] mob = gp.map[currentMap].getMobs();
        for(int i = 0; i < mob.length; i++) {
            Mob now = mob[i];
            if(now != null && now.getState() != Mob.mobState.die) {
                //gp.cCheck.CheckEntityCollision(player, now);
                System.out.println(now.getName() + " " + now.getHP());
                if(attackCounter == 0) {
                    gp.aProcess.attacking(player, now);
                }

                attackCounter++;
                if(attackCounter == 120) attackCounter = 0;

            }
            else attackCounter = 0;

        }
    }

    public void updateDie() {
        gp.setGameState(GameProcess.GameState.Wasted);
    }

    public int update() {

        //attackCounter = 0;

        switch (this.player.getState()) {
            case idle -> {
                attackCounter = 0;
                return updateStand();
            }

            case move -> {
                return updateMove();
            }

            case melee, projectile -> updateAttack();

            case die -> updateDie();

            case collectEXP -> player.CollectEXP(expGet);

        }
        return 0;
    }

    public void InteractNPC(int i) {
        if(i != -1) {
            if(keyH.interactPressed) {
                this.player.setState(Player.State.interact);
                gp.setGameState(GameProcess.GameState.Dialog);
                //gp.GameState = 3;
                gp.NProcess.speak(gp.map[gp.currentMap].getNpcs()[i]);
            }
        }
        keyH.interactPressed = false;
        this.player.setState(Player.State.idle);

    }

    public void drawAttack(Graphics2D g2) {
        int worldX = player.getPosX();
        int worldY = player.getPosY();
        int n = 0;
        BufferedImage image = null;
        switch (this.player.getDirection()) {
            case "up" -> {
                if(player.getState() == Player.State.melee) {image = this.mUp; n=1;}
                if(player.getState() == Player.State.projectile && this.player.getMP()-this.player.getCostMP() >=0){
                    image = this.pUp; n=4;}
                for(int i = 1;i <= n;i++) {
                        g2.drawImage(image, worldX, worldY - i * gp.tileSize, gp.tileSize, gp.tileSize, null);
                }

            }
            case "down" -> {
                if(player.getState() == Player.State.melee) {image = this.mDown; n=1;}
                if(player.getState() == Player.State.projectile && this.player.getMP()-this.player.getCostMP() >=0){
                    image = this.pDown; n=4;}
                for(int i=1;i<=n;i++)
                    g2.drawImage(image, worldX, worldY + i*gp.tileSize, gp.tileSize, gp.tileSize, null);
            }
            case "left" -> {
                if(player.getState() == Player.State.melee) {image = this.mLeft; n=1;}
                if(player.getState() == Player.State.projectile && this.player.getMP()-this.player.getCostMP() >=0){
                    image = this.pLeft; n=4;}
                for(int i=1;i<=n;i++)
                    g2.drawImage(image, worldX - i*gp.tileSize, worldY, gp.tileSize, gp.tileSize, null);
            }
            case "right" -> {
                if(player.getState() == Player.State.melee) {image = this.mRight; n=1;}
                if(player.getState() == Player.State.projectile && this.player.getMP()-this.player.getCostMP() >=0){
                    image = this.pRight; n=4;}
                for(int i=1;i<=n;i++)
                    g2.drawImage(image, worldX + i * gp.tileSize, worldY, gp.tileSize, gp.tileSize, null);
            }
        }
    }

    public void draw(Graphics2D g2) {

        double oneScale = (double)gp.tileSize / player.getMaxHP();
        double hpBarValue = oneScale * player.getHP();
        g2.setColor(new Color(35,35,35));
        g2.fillRect(player.getPosX()-1, player.getPosY()-16, gp.tileSize+2, 12);

        g2.setColor(new Color(255,0,30));
        g2.fillRect(player.getPosX(), player.getPosY()-15, (int)hpBarValue, 10);

        BufferedImage image = null;

        switch (this.player.getDirection()) {
            case "up" -> image = this.up[spriteNum];
            case "down" -> image = this.down[spriteNum];
            case "left" -> image = this.left[spriteNum];
            case "right" -> image = this.right[spriteNum];
        }
        g2.drawImage(image, this.player.getPosX(), this.player.getPosY(), gp.tileSize, gp.tileSize, null);

        if(player.getState() == Player.State.melee || player.getState() == Player.State.projectile){
            drawAttack(g2);
            player.setState(Player.State.idle);

            if(player.getState() == Player.State.projectile && this.player.getMP()-this.player.getCostMP() <0) {
                for(int i=1;i<=1000000;i++)
                    if(i==1000000) {
                        g2.setFont(g2.getFont().deriveFont(30F));
                        g2.drawString("Not enough mana!", gp.tileSize*3, gp.tileSize*5);
                    }
            }
        }

    }

}
