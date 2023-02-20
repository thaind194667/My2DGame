package OutputProcess;

import Graphic.GameProcess;
import entity.Mob;
import entity.NPC;
import entity.Player;
import object.SuperObject;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;
import java.util.Random;

public class MobProcess extends EntityProcess {

    int actionLocker = 0;
    int spriteCounter = 0;
    int spriteNum = 1;

    int respawnCounter = 0;



    public MobProcess(GameProcess gp) {
        super(gp);

    }

    public void getImg(Mob mob) {
        mob.up = new BufferedImage[5];
        mob.down = new BufferedImage[5];
        mob.left = new BufferedImage[5];
        mob.right = new BufferedImage[5];
        mob.stand = new BufferedImage[5];
        for(int i = 1; i <= 2; i++) {
            mob.up[i] = loadImg(mob.getName() + "_" + i);
            mob.down[i] = loadImg(mob.getName() + "_" + i);
            mob.left[i] = loadImg(mob.getName() + "_" + i);
            mob.right[i] = loadImg(mob.getName() + "_" + i);
            mob.stand[i] = loadImg(mob.getName() + "_" + i);
        }
    }

    public BufferedImage loadImg(String img) {
        UtilityTool uT = new UtilityTool();

        BufferedImage image = null;
        try {
            image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/mob/" + img + ".png")));
            image = uT.scaleImg(image, gp.tileSize, gp.tileSize);
        }catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    public void turnBack(Mob mob) {
        switch (mob.getDirection()) {
            case "up" -> mob.setDirection("down");
            case "down" -> mob.setDirection("up");
            case "left" -> mob.setDirection("right");
            case "right" -> mob.setDirection("left");
        }
    }

    public void checkCollision(Mob mob) {

        mob.collisionOn = false;

        boolean currentCollision = false;
        gp.getcCheck().CheckEntityCollision(mob, gp.player);
        if(mob.collisionOn) {
            mob.atkCounter++;
            if(mob.atkCounter == 30) {
                mob.DealDamage(gp.player);
                mob.atkCounter = 0;
            }
        }
        
        if(mob.collisionOn) currentCollision = true;

        mob.collisionOn = false;

        gp.getcCheck().checkTile(mob);
        if(mob.collisionOn) currentCollision = true;
        // check collision with npc


        for(int i = 0; i < gp.map[gp.currentMap].getNpcs().length; i++) {
            NPC now = gp.map[gp.currentMap].getNpcs()[i];
            mob.collisionOn = false;
            if(now != null) {
                gp.getcCheck().CheckEntityCollision(mob, now);
                if(mob.collisionOn)
                    currentCollision = true;
            }
        }

        for(int i = 0; i < gp.map[gp.currentMap].getMobs().length; i++) {
            Mob now = gp.map[gp.currentMap].getMobs()[i];
            mob.collisionOn = false;
            if(now != null) {
                gp.getcCheck().CheckEntityCollision(mob, now);
                if(mob.collisionOn)
                    currentCollision = true;
            }
        }

        for(int i = 0; i < gp.map[gp.currentMap].getObjs().length; i++) {
            SuperObject now = gp.map[gp.currentMap].getObjs()[i];
            mob.collisionOn = false;
            if(now != null) {
                gp.getcCheck().CheckObjCollision(mob, i);
                if(mob.collisionOn)
                    currentCollision = true;
            }
        }

        mob.collisionOn = currentCollision;
    }

    public boolean checkPlayerPos(Mob mob) {
        Player player = gp.player;

        int Ax = mob.getPosX() / gp.tileSize;
        int Ay = mob.getPosY() / gp.tileSize;

        int Bx = player.getPosX() / gp.tileSize;
        int By = player.getPosY() / gp.tileSize;

        int range = mob.getRange();

        return Ax - range <= Bx && Bx <= Ax + range && Ay - range <= By && By <= Ay + range;
    }

    public void updateIdle(Mob mob) {

        checkCollision(mob);
        if(checkPlayerPos(mob)) {
            mob.setState(Mob.mobState.chase);
            mob.onPath = true;
        }
        else {
            mob.onPath = false;
            mob.setState(Mob.mobState.move);
        }

    }

    public void updateMove(Mob mob) {

        checkCollision(mob);

        int worldX = mob.getPosX();
        int worldY = mob.getPosY();
        int speed = mob.getSpeed();

        if(!mob.collisionOn) {
            switch (mob.getDirection()) {
                case "up" -> mob.setPosY(worldY - speed);
                case "down" -> mob.setPosY(worldY + speed);
                case "left" -> mob.setPosX(worldX - speed);
                case "right" -> mob.setPosX(worldX + speed);
            }

            if(mob.getPosX() % gp.tileSize == 0 && mob.getPosY() % gp.tileSize == 0) {
                if(actionLocker >= 120) {
                    Random random = new Random();
                    int i = random.nextInt(125) + 1;

                    if(i <= 25) mob.setDirection("up");
                    else if(i <= 50) mob.setDirection("down");
                    else if(i <= 75) mob.setDirection("left");
                    else if(i <= 100) mob.setDirection("right");
                    else mob.setDirection("stand");
                    actionLocker = 0;

                }
                mob.setState(Mob.mobState.idle);
            }

            spriteCounter++;
            if(spriteCounter > 20) {
                spriteNum = (spriteNum + 1) % 2;
                if(spriteNum == 0) spriteNum = 2;
                spriteCounter = 0;
            }
        }
        else {
            if(mob.getPosX() % gp.tileSize == 0 && mob.getPosY() % gp.tileSize == 0) {
                Random random = new Random();
                int i = random.nextInt(125) + 1;

                if(i <= 25) mob.setDirection("up");
                else if(i <= 50) mob.setDirection("down");
                else if(i <= 75) mob.setDirection("left");
                else if(i <= 100) mob.setDirection("right");
                else mob.setDirection("stand");

                mob.setState(Mob.mobState.idle);
            }
            else {
                turnBack(mob);
            }

        }

        if(checkPlayerPos(mob)) {
            mob.setState(Mob.mobState.chase);
            mob.onPath = true;
        }
    }

    public void updateChase(Mob mob){

        if(!checkPlayerPos(mob)) {
            mob.setState(Mob.mobState.move);
            return;
        }

        mob.collisionOn = false;
        gp.getcCheck().CheckEntityCollision(mob, gp.player);
        if(mob.collisionOn) {
            mob.atkCounter++;
            if(mob.atkCounter == 30) {
                gp.mapDraw.drawPath = false;
                mob.DealDamage(gp.player);
                mob.atkCounter = 0;
            }
        }
        else gp.mapDraw.drawPath = true;

        checkCollision(mob);

        if(!mob.collisionOn) {
            int worldX = mob.getPosX();
            int worldY = mob.getPosY();
            int speed = mob.getSpeed();
            switch (mob.getDirection()) {
                case "up" -> mob.setPosY(worldY - speed);
                case "down" -> mob.setPosY(worldY + speed);
                case "left" -> mob.setPosX(worldX - speed);
                case "right" -> mob.setPosX(worldX + speed);
            }
            if(mob.getPosX() % gp.tileSize == 0 && mob.getPosY() % gp.tileSize == 0) {
                mob.onPath = false;
            }
        }

    }

    public void updateDie(Mob mob) {
        mob.RespawnCounter++;
        if(mob.RespawnCounter == mob.getTimeToRespawn()) {
            mob.setState(Mob.mobState.respawn);
            mob.RespawnCounter = 0;
        }
    }
    public void update(Mob mob) {

        actionLocker++;
        //System.out.println(mob.getState());

        if(mob.onPath) {
            gp.mapDraw.drawPath = true;
            gp.pFinder.searchPath(gp.player.getPosX() / gp.tileSize , gp.player.getPosY() / gp.tileSize, mob);
        }
        else gp.mapDraw.drawPath = false;

        switch (mob.getState()) {
            case idle -> updateIdle(mob);
            case move -> updateMove(mob);
            case chase -> {
                mob.onPath = true;
                updateChase(mob);
            }
            case die -> {
                updateDie(mob);
                mob.onPath = false;
            }
            case respawn -> mob.Respawn(gp.getPlayer().getLevel());
        }

    }

//    public void Move(char direction, Mob mob) {
//
//        do {
//            int worldX = mob.getPosX();
//            int worldY = mob.getPosY();
//            int speed = mob.getSpeed();
//            switch (direction) {
//                case 'U' -> mob.setPosY(worldY - speed);//worldY -= speed;
//                case 'D' -> mob.setPosY(worldY + speed);
//                case 'L' -> mob.setPosX(worldX - speed);
//                case 'R' -> mob.setPosX(worldX + speed);
//            }
//
//        } while (mob.getPosX() % gp.tileSize != 0 || mob.getPosY() % gp.tileSize != 0);
//
//    }
    
    public void draw(Graphics2D g2, Mob mob) {

        if(mob.getState() == Mob.mobState.die || mob.getState() == Mob.mobState.respawn) return;
        BufferedImage image = null;
        switch (mob.getDirection()) {
            case "up" -> image = mob.up[spriteNum];
            case "down" -> image = mob.down[spriteNum];
            case "left" -> image = mob.left[spriteNum];
            case "right" -> image = mob.right[spriteNum];
            case "stand" -> image = mob.stand[spriteNum];
        }

        double oneScale = (double)gp.tileSize/mob.getMaxHP();
        double hpBarValue = oneScale*mob.getHP();
        g2.setColor(new Color(35,35,35));
        g2.fillRect(mob.getPosX()-1, mob.getPosY()-16, gp.tileSize+2, 12);

        g2.setColor(new Color(255,0,30));
        g2.fillRect(mob.getPosX(), mob.getPosY()-15, (int)hpBarValue, 10);

        g2.drawImage(image, mob.getPosX(), mob.getPosY(), gp.tileSize, gp.tileSize, null);
    }
}