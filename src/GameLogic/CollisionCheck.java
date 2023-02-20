package GameLogic;

import Graphic.GameProcess;
import entity.Entity;
import entity.Player;
import object.SuperObject;

public class CollisionCheck {

    GameProcess gp;

    public CollisionCheck(GameProcess gp) {
        this.gp = gp;
    }

    public int checkTile(Entity entity) {

        int entityX = entity.getPosX() / gp.tileSize;
        int entityY = entity.getPosY() / gp.tileSize;

        int tileNum = 0;// = gp.tileM.mapTileNum[entityX][entityY];

        int[][] set = gp.map[gp.currentMap].setTile;

        switch (entity.getDirection()) {
            case "up" -> {
                if (entityY - 1 < 0)
                    entity.collisionOn = true;
                else
                    tileNum = set[entityX][entityY - 1];
            }
            case "down" -> {
                if (entityY + 1 >= gp.maxWorldRow)
                    entity.collisionOn = true;
                else
                    tileNum = set[entityX][entityY + 1];
            }
            case "left" -> {
                if (entityX - 1 < 0)
                    entity.collisionOn = true;
                else
                    tileNum = set[entityX - 1][entityY];
            }
            case "right" -> {
                if (entityX + 1 >= gp.maxWorldCol)
                    entity.collisionOn = true;
                else
                    tileNum = set[entityX + 1][entityY];
            }
            default -> {
            }
        }
        if(tileNum > 0)
            entity.collisionOn = true;


        if(tileNum < 0) return -tileNum;
        else return 0;

    }

    public int checkTeleportTile(Player player) {
        return checkTile(player);
    }

    public void CheckEntityCollision(Entity A, Entity B) {
        int Ax = A.getPosX() / gp.tileSize;
        int Ay = A.getPosY() / gp.tileSize;


        int Bx = B.getPosX() / gp.tileSize;
        int By = B.getPosY() / gp.tileSize;


        switch (A.getDirection()) {
            case "up" -> {
                if (Ay - 1 == By && Ax == Bx) A.collisionOn = true;
            }
            case "down" -> {
                if (Ay + 1 == By && Ax == Bx) A.collisionOn = true;
            }
            case "left" -> {
                if (Ax - 1 == Bx && Ay == By) A.collisionOn = true;
            }
            case "right" -> {
                if (Ax + 1 == Bx && Ay == By) A.collisionOn = true;
            }
        }
        //entity.collisionOn = true;
    }

    public void CheckObjCollision(Entity entity, int i) {
        int Ax = entity.getPosX() / gp.tileSize;
        int Ay = entity.getPosY() / gp.tileSize;

        SuperObject B = gp.map[gp.currentMap].getObjs()[i];

        int Bx = B.getX() / gp.tileSize;
        int By = B.getY() / gp.tileSize;

        //boolean collision = B.getCollision();

        int interactionType = B.getInteractionType();

        if(interactionType != 1) {
            switch (entity.getDirection()) {
                case "up" -> {
                    if (Ay - 1 == By && Ax == Bx) entity.collisionOn = true;
                }
                case "down" -> {
                    if (Ay + 1 == By && Ax == Bx) entity.collisionOn = true;
                }
                case "left" -> {
                    if (Ax - 1 == Bx && Ay == By) entity.collisionOn = true;
                }
                case "right" -> {
                    if (Ax + 1 == Bx && Ay == By) entity.collisionOn = true;
                }
            }
        }
    }

    public void checkObjCollisionOnPlayer(Player A, int i) {

        //Player player = gp.getPlayer();
        int Ax = A.getPosX() / gp.tileSize;
        int Ay = A.getPosY() / gp.tileSize;

        SuperObject B = gp.map[gp.currentMap].getObjs()[i];

        int Bx = B.getX() / gp.tileSize;
        int By = B.getY() / gp.tileSize;

        //boolean collision = B.getCollision();

        int interactionType = B.getInteractionType();

        if(interactionType != 1) {
            switch (A.getDirection()) {
                case "up" -> {
                    if (Ay - 1 == By && Ax == Bx) A.collisionOn = true;
                }
                case "down" -> {
                    if (Ay + 1 == By && Ax == Bx) A.collisionOn = true;
                }
                case "left" -> {
                    if (Ax - 1 == Bx && Ay == By) A.collisionOn = true;
                }
                case "right" -> {
                    if (Ax + 1 == Bx && Ay == By) A.collisionOn = true;
                }
            }
        }
        else  if(Ax == Bx && Ay == By) {
            int effectMass = B.getEffectMass();
            String effect = B.getEffect();
            int type = B.getType();
            System.out.println(B.getName() + " " + type + " " + effect + " " + effectMass);

            switch (type) {
                case 0 -> {
                    switch (effect) {
                        case "HP" -> A.setHP(A.getHP() - effectMass);
                        case "MP" -> A.setMP(A.getMP() - effectMass);
                        case "Speed" -> A.setSpeed(A.getSpeed() - effectMass);
                        case "Attack" -> A.setAttack(A.getAttack() - effectMass);
                        case "Defend" -> A.setDefend(A.getDefend() - effectMass);
                    }
                }

                case 1 -> {
                    switch (effect) {
                        case "HP" -> A.setHP(A.getHP() + effectMass);
                        case "MP" -> A.setMP(A.getMP() + effectMass);
                        case "Speed" -> A.setSpeed(A.getSpeed() + effectMass);
                        case "Attack" -> A.setAttack(A.getAttack() + effectMass);
                        case "Defend" -> A.setDefend(A.getDefend() + effectMass);
                    }
                }

            }

            //gp.getPlayer().Loot(B);
            gp.setOBJ(i, null);

        }

    }

}
