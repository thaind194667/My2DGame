package entity;

public class Entity {

    public final int DefaultSpeed = 4;
    private int speed;
    private String name;
    private int HP;
    private int maxHP;
    private int MP;
    private int maxMP;
    private int level;
    private int Attack;
    private int Defend;
    private int PosX;
    private int PosY;
    private String direction;
    public boolean collisionOn = false;

    public Entity() {
        this.level = 1;
        this.speed = DefaultSpeed;
        this.direction = "down";
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }
    public void setAttack(int attack) {
        Attack = attack;
    }
    public void setHP(int HP) {
        this.HP = HP;
    }
    public void setMP(int MP) {
        this.MP = MP;
    }
    public void setDefend(int defend) {
        Defend = defend;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setPos(int x, int y) {
        setPosX(x);
        setPosY(y);
    }
    public void setPosX(int posX) {
        PosX = posX;
    }
    public void setPosY(int posY) {
        PosY = posY;
    }
    public void setDirection(String direction) {
        this.direction = direction;
    }
    public void setLevel(int level) {
        this.level = level;
    }

    public void setMaxHP(int maxHP) {
        this.maxHP = maxHP;
    }

    public void setMaxMP(int maxMP) {
        this.maxMP = maxMP;
    }

    public int getHP() {
        return HP;
    }
    public int getMP() {
        return MP;
    }
    public int getAttack() {
        return Attack;
    }
    public int getDefend() {
        return Defend;
    }
    public int getSpeed() {
        return speed;
    }
    public String getName() {
        return name;
    }
    public int getPosX() {
        return PosX;
    }
    public int getPosY() {
        return PosY;
    }
    public String getDirection() {
        return direction;
    }
    public int getLevel() {
        return level;
    }

    public int getMaxHP() {
        return maxHP;
    }

    public int getMaxMP() {
        return maxMP;
    }
}
