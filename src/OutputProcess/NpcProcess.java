package OutputProcess;

import Graphic.GameProcess;
import entity.Mob;
import entity.NPC;
import object.SuperObject;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;
import java.util.Random;

public class NpcProcess extends EntityProcess {

    int actionLocker = 0;
    int spriteCounter = 0;
    int spriteNum = 1;
    int CurrentSpeak = 0;

    public NpcProcess(GameProcess gp) {
        super(gp);

    }

    public void getImg(NPC npc) {
        npc.up = new BufferedImage[5];
        npc.down = new BufferedImage[5];
        npc.left = new BufferedImage[5];
        npc.right = new BufferedImage[5];
        npc.stand = new BufferedImage[5];
        for(int i = 1; i <= 2; i++) {
            npc.up[i] = loadImg("up" + i, npc.getName());
            npc.down[i] = loadImg("down" + i, npc.getName());
            npc.left[i] = loadImg("left" + i, npc.getName());
            npc.right[i] = loadImg("right" + i, npc.getName());
            npc.stand[i] = loadImg("down1", npc.getName());
        }
    }

    public BufferedImage loadImg(String img, String name) {
        UtilityTool uT = new UtilityTool();

        BufferedImage image = null;
        try {
            image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/NPC/" + name + "/" + img + ".png")));
            image = uT.scaleImg(image, gp.tileSize, gp.tileSize);
        }catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    public void turnBack(NPC npc) {
        switch (npc.getDirection()) {
            case "up" -> npc.setDirection("down");
            case "down" -> npc.setDirection("up");
            case "left" -> npc.setDirection("right");
            case "right" -> npc.setDirection("left");
        }
    }

    public void turnToPlayer(NPC npc) {
        switch (gp.getPlayer().getDirection()) {
            case "up" -> npc.setDirection("down");
            case "down" -> npc.setDirection("up");
            case "left" -> npc.setDirection("right");
            case "right" -> npc.setDirection("left");
        }
    }

    public void speak(NPC npc) {

//        if(CurrentSpeak == 0)
//        {
//            npc.onPath = true;
//            npc.onPathHome = false;
//            npc.setState(NPC.npcState.follow);
//        }
//        else {
//            npc.setState(NPC.npcState.moveBack);
//            npc.onPathHome = true;
//            npc.onPath = false;
//        }

        if(npc.getDialogue()[CurrentSpeak] == null) CurrentSpeak = 0;
        gp.getUi().currentDialogue = npc.getDialogue()[CurrentSpeak];
        CurrentSpeak++;

        turnToPlayer(npc);
        //npc.setMovingAround(true);
    }

    public void checkCollision(NPC npc) {

        boolean currentCollision = false;
        npc.collisionOn = false;
        // check tile collision
        gp.getcCheck().checkTile(npc);
        if(npc.collisionOn) {
            currentCollision = true;
        }

        npc.collisionOn = false;
        // check player collision
        gp.getcCheck().CheckEntityCollision(npc, gp.player);
        if(npc.collisionOn) {
            currentCollision = true;
        }

        npc.collisionOn = false;
        // check other npc collision
        for(int i = 0; i < gp.map[gp.currentMap].getNpcs().length; i++) {
            NPC now = gp.map[gp.currentMap].getNpcs()[i];
            npc.collisionOn = false;
            if(now != null && now.equals(npc)) {
                gp.getcCheck().CheckEntityCollision(npc, now);
                if(npc.collisionOn)
                    currentCollision = true;
            }
        }

        for(int i = 0; i < gp.map[gp.currentMap].getMobs().length; i++) {
            Mob now = gp.map[gp.currentMap].getMobs()[i];
            npc.collisionOn = false;
            if(now != null) {
                gp.getcCheck().CheckEntityCollision(npc, now);
                if(npc.collisionOn)
                    currentCollision = true;
            }
        }

        for(int i = 0; i < gp.map[gp.currentMap].getObjs().length; i++) {
            SuperObject now = gp.map[gp.currentMap].getObjs()[i];
            npc.collisionOn = false;
            if(now != null) {
                gp.getcCheck().CheckObjCollision(npc, i);
                if(npc.collisionOn)
                    currentCollision = true;
            }
        }

        npc.collisionOn = currentCollision;
    }

    public void updateIdle(NPC npc) {
        npc.setMovingAround(false);
    }

    public void updateMove(NPC npc) {

        int worldX = npc.getPosX();
        int worldY = npc.getPosY();
        int speed = npc.getSpeed();

        if(!npc.collisionOn) {
            switch (npc.getDirection()) {
                case "up" -> npc.setPosY(worldY - speed);
                case "down" -> npc.setPosY(worldY + speed);
                case "left" -> npc.setPosX(worldX - speed);
                case "right" -> npc.setPosX(worldX + speed);
            }

            if(npc.getPosX() % gp.tileSize == 0 && npc.getPosY() % gp.tileSize == 0) {
                if(actionLocker >= 120) {
                    Random random = new Random();
                    int i = random.nextInt(125) + 1;

                    if(i <= 25) npc.setDirection("up");
                    else if(i <= 50) npc.setDirection("down");
                    else if(i <= 75) npc.setDirection("left");
                    else if(i <= 100) npc.setDirection("right");
                    else npc.setDirection("stand");
                    actionLocker = 0;

                }
                npc.setState(NPC.npcState.idle);
            }

            spriteCounter++;
            if(spriteCounter > 20) {
                spriteNum = (spriteNum + 1) % 2;
                if(spriteNum == 0) spriteNum = 2;
                spriteCounter = 0;
            }
        }
        else {
            if(npc.getPosX() % gp.tileSize == 0 && npc.getPosY() % gp.tileSize == 0) {
                Random random = new Random();
                int i = random.nextInt(125) + 1;

                if(i <= 25) npc.setDirection("up");
                else if(i <= 50) npc.setDirection("down");
                else if(i <= 75) npc.setDirection("left");
                else if(i <= 100) npc.setDirection("right");
                else npc.setDirection("stand");

                npc.setState(NPC.npcState.idle);
            }
            else {
                turnBack(npc);
            }

        }
    }

    public void updateFollow(NPC npc) {

        checkCollision(npc);

        if(!npc.collisionOn) {
            int worldX = npc.getPosX();
            int worldY = npc.getPosY();
            int speed = npc.getSpeed();
            switch (npc.getDirection()) {
                case "up" -> npc.setPosY(worldY - speed);
                case "down" -> npc.setPosY(worldY + speed);
                case "left" -> npc.setPosX(worldX - speed);
                case "right" -> npc.setPosX(worldX + speed);
            }
            if(npc.getPosX() % gp.tileSize == 0 && npc.getPosY() % gp.tileSize == 0) {
                npc.onPath = false;
            }
        }
    }

    public void updateMoveBack(NPC npc) {
        checkCollision(npc);
        if(npc.getStartPosX() == npc.getStartPosX() && npc.getPosY() == npc.getStartPosY()) {
            npc.setState(NPC.npcState.idle);
            return;
        }

        if(!npc.collisionOn) {
            int worldX = npc.getPosX();
            int worldY = npc.getPosY();
            int speed = npc.getSpeed();
            switch (npc.getDirection()) {
                case "up" -> npc.setPosY(worldY - speed);
                case "down" -> npc.setPosY(worldY + speed);
                case "left" -> npc.setPosX(worldX - speed);
                case "right" -> npc.setPosX(worldX + speed);
            }
            if(npc.getPosX() % gp.tileSize == 0 && npc.getPosY() % gp.tileSize == 0) {
                npc.onPathHome = false;
            }
        }
    }

    public void update(NPC npc) {

        if(npc.getMovingAround()) {
            actionLocker++;
            if(npc.onPath) {
                gp.pFinder.searchPath(gp.player.getPosX() / gp.tileSize , gp.player.getPosY() / gp.tileSize, npc);
                gp.mapDraw.drawPath = true;
            }
            if(npc.onPathHome) {
                gp.pFinder.searchPath(npc.getStartPosX() / gp.tileSize, npc.getStartPosY() / gp.tileSize, npc);
                gp.mapDraw.drawPath = true;
            }
            switch (npc.getState()) {
                case move -> updateMove(npc);

                case idle -> updateIdle(npc);

                case follow -> {
                    npc.onPath = true;
                    updateFollow(npc);
                }
                case moveBack -> {
                    npc.onPathHome = true;
                    updateMoveBack(npc);
                }
            }
        }

    }

    public void draw(Graphics2D g2, NPC npc) {

        BufferedImage image = null;
        switch (npc.getDirection()) {
            case "up" -> image = npc.up[spriteNum];
            case "down" -> image = npc.down[spriteNum];
            case "left" -> image = npc.left[spriteNum];
            case "right" -> image = npc.right[spriteNum];
            case "stand" -> image = npc.stand[spriteNum];
        }
        //if(image == null) System.out.println("Null");
        g2.drawImage(image, npc.getPosX(), npc.getPosY(), gp.tileSize, gp.tileSize, null);
    }

}
