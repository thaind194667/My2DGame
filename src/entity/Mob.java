package entity;

import java.awt.image.BufferedImage;
import java.util.Random;

public class Mob extends Entity {

    String direction;
    private int range;
    private final int timeToRespawn;

    public BufferedImage[] up;
    public BufferedImage[] down;
    public BufferedImage[] left;
    public BufferedImage[] right;
    public BufferedImage[] stand;

    public enum mobState {idle, move, chase, die, respawn}

    private mobState state;

    private int EXP;

    String[] dialogue;

    private int StartPosX, StartPosY;

    public boolean onPath = false;

    public int RespawnCounter = 0;

    public int atkCounter = 0;

    public int getDamCounter = 0;

    public Mob() {
        super();

        dialogue = new String[1000];

        direction = "down";

        setState(mobState.idle);

        timeToRespawn = 1200;

        EXP = getLevel() * 15;

    }

    public void setStat(String name, int x, int y, int range, int speed, int HP, int MP, int atk, int def) {
        setName(name);
        setPos(x, y);
        setStartPos(x, y);
        setRange(range);
        setSpeed(speed);
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

    public void setRange(int range) {
        this.range = range;
    }

    public void setDialogue(String text, int index) {
        dialogue[index] = text;
    }

    public void setState(mobState state) {
        this.state = state;
    }

    public int getEXP() {
        return EXP;
    }

    public String[] getDialogue() {
        return dialogue;
    }

    public void LevelScale(int level) {
        setMaxHP(getMaxHP() + 20 * (level - getLevel()));
        setHP(getMaxHP());
        setAttack(getAttack() + 5 * (level - getLevel()));
        setLevel(level);
        Random random = new Random();
        EXP = random.nextInt(level * 5) + level * 15;
    }

    public void Respawn(int level) {
        setPos(StartPosX, StartPosY);
        LevelScale(level);
        setState(mobState.idle);
    }

    public int getRange() {
        return range;
    }

    public mobState getState() {
        return state;
    }

    public int getTimeToRespawn() {
        return timeToRespawn;
    }

    public void DealDamage(Player player) {
        player.setHP(player.getHP() - getAttack());
        if(player.getHP() <= 0) {
            player.setState(Player.State.die);
            onPath = false;
        }

    }

    public int getStartPosX() {
        return StartPosX;
    }
    public int getStartPosY() {
        return StartPosY;
    }
}
