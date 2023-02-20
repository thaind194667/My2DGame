package entity;

import java.awt.image.BufferedImage;

public class NPC extends Entity {

    //String name;
    public BufferedImage[] up;
    public BufferedImage[] down;
    public BufferedImage[] left;
    public BufferedImage[] right;
    public BufferedImage[] stand;
    public boolean moving = false;
    String direction;
    String[] dialogue;
    boolean MovingAround;
    private int StartPosX, StartPosY;
    public boolean onPath = false;
    public boolean onPathHome = false;
    public enum npcState {idle, move, follow, moveBack}
    private npcState state;

    public NPC() {
        super();

        //this.setName(name);
        //this.setSpeed(DefaultSpeed / 4);
        dialogue = new String[1000];

        direction = "stand";
        state = npcState.move;

    }

    public void setStat(String name, int speed, int x, int y, boolean moving) {
        setName(name);
        setSpeed(speed);
        setPos(x, y);
        setMovingAround(moving);
    }

    public void setStat(String name, int speed, int x, int y, boolean moving, int HP, int MP, int atk, int def) {
        setName(name);
        setSpeed(speed);
        setPos(x, y);
        setStartPos(x, y);
        setMovingAround(moving);
        setHP(HP);
        setMaxHP(HP);
        setMaxMP(MP);
        setMP(MP);
        setAttack(atk);
        setDefend(def);
    }

    public void setStartPos(int x, int y) {
        StartPosX = x;
        StartPosY = y;
    }

    public void setMovingAround(boolean movingAround) {
        MovingAround = movingAround;
    }
    public void setDialogue(String text, int index) {
        dialogue[index] = text;
    }
    public void setState(npcState state) {
        this.state = state;
    }

    public boolean getMovingAround() {
        return MovingAround;
    }
    public String[] getDialogue() {
        return dialogue;
    }
    public npcState getState() {
        return state;
    }

    public int getStartPosX() {
        return StartPosX;
    }
    public int getStartPosY() {
        return StartPosY;
    }
}
