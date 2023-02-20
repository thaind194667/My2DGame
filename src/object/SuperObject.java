package object;

import java.awt.image.BufferedImage;

public class SuperObject {

    public BufferedImage image;

    private int id;
    private String name;
    private boolean collision;
    private int x, y;
    private int type;   //
    private String effect;
    private int effectMass;
    private int location;

    private int interactionType;

    public SuperObject() {
        collision = false;
    }

    public SuperObject(String name) {
        setName(name);
        collision = false;
    }

    public void setStat(String name, int x, int y, int interactionType, int type, String effect, int effectMass) {
        setName(name);
        setPos(x, y);
        setInteractionType(interactionType);
        setType(type);
        setEffect(effect);
        setEffectMass(effectMass);
    }

    public void setName(String name) {
        this.name = name;
    }
    public void setX(int x) {
        this.x = x;
    }
    public void setY(int y) {
        this.y = y;
    }
    public void setCollision(boolean collision) {
        this.collision = collision;
    }
    public void setPos(int x, int y) {
        setX(x);
        setY(y);
    }
    public void setEffect(String effect) {
        this.effect = effect;
    }
    public void setEffectMass(int effectMass) {
        this.effectMass = effectMass;
    }
    public void setType(int type) {
        this.type = type;
    }
    public void setInteractionType(int interactionType) {
        this.interactionType = interactionType;
    }

    public String getName() {
        return name;
    }
    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    public boolean getCollision() {return collision;}
    public String getEffect() {
        return effect;
    }
    public int getEffectMass() {
        return effectMass;
    }
    public int getType() {
        return type;
    }
    public int getInteractionType() {
        return interactionType;
    }

    public int getId() {
        return id;
    }
}
