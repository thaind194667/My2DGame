package map;

import entity.Mob;
import entity.NPC;
import object.SuperObject;

public class Map {
    private final int MaxRow = 25;
    private final int MaxCol = 45;

    public int[][] frontTile;
    public int[][] backTile;

    public int[][] setTile;
    private String name;
    private int id;

    private NPC[] npcs;

    private Mob[] mobs;

    private SuperObject[] objs;

    public Map(int id) {
        this.id = id;

        setTile = new int[MaxCol][MaxRow];
        frontTile = new int[MaxCol][MaxRow];
        backTile = new int[MaxCol][MaxRow];

        npcs = new NPC[50];
        mobs = new Mob[50];
        objs = new SuperObject[50];
    }

    public int getId() {
        return id;
    }
    public Mob[] getMobs() {
        return mobs;
    }
    public NPC[] getNpcs() {
        return npcs;
    }
    public String getName() {
        return name;
    }

    public SuperObject[] getObjs() {
        return objs;
    }

    public int getMaxRow() {
        return MaxRow;
    }
    public int getMaxCol() {
        return MaxCol;
    }

    public void setName(String name) {
        this.name = name;
    }

}
