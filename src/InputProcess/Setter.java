package InputProcess;

import Graphic.GameProcess;
import entity.Mob;
import entity.NPC;
import map.Map;
import object.SuperObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Setter {

    GameProcess gp;

    int maxWorldCol;
    int maxWorldRow;

    public Setter(GameProcess gp) {
        this.gp = gp;
    }

    public void load(Map map) {
        maxWorldCol = map.getMaxCol();
        maxWorldRow = map.getMaxRow();
        loadComponent(map);
        loadMapTile(map);
    }

    public void loadComponent(Map map) {
        try {
            InputStream is = getClass().getResourceAsStream("/maps/map0" + map.getId() + "Setting.txt");
            assert is != null;
            BufferedReader sc = new BufferedReader(new InputStreamReader(is));

            int Type = 0;
            int npcCount = 0;
            int mobCount = 0;
            int objCount = 0;

//            NPC[] npcs = map.getNpcs();
//            Mob[] mobs = map.getMobs();
            SuperObject[] obj = map.getObjs();

            int dialogIndex = 0;

            while (true) {
                String line = sc.readLine();
                if(line == null) break;
                System.out.println(line);

                switch (line.charAt(0)) {
                    case '#' -> {
                        Type = 0;
                        String name = line.split(" ")[1];
                        map.setName(name);
                    }
                    case '*' -> {
                        dialogIndex = 0;
                        String type = line.split(" ")[1];
                        if(type.equals("NPC"))
                            Type = 1;
                        else if(type.equals("Mob"))
                            Type = 2;
                        else
                            Type = 3;
                    }
                    case '+' -> {
                        dialogIndex = 0;
                        if(Type == 1) {
                            map.getNpcs()[npcCount] = new NPC();
                            setNpc(map.getNpcs()[npcCount], line);
                            npcCount++;
                        }
                        else if(Type == 2){
                            map.getMobs()[mobCount] = new Mob();
                            setMob(map.getMobs()[mobCount], line);
                            //System.out.println(map.getMobs()[mobCount].getName());
                            mobCount++;
                        }
                        else {
                            obj[objCount] = new SuperObject();
                            setObj(obj[objCount], line);
                            objCount++;
                        }
                    }
                    case '-' -> {
                        String dialog = line.split("-")[1];
                        if(Type == 1) {
                            NPC now = map.getNpcs()[npcCount - 1];
                            now.setDialogue(dialog, dialogIndex);
                        }
                        else if(Type == 2){
                            Mob now = map.getMobs()[mobCount - 1];
                            now.setDialogue(dialog, dialogIndex);
                        }
                        dialogIndex++;
                    }
                }

            }

            sc.close();

        }
        catch (Exception ignored) {}
    }

    public void loadMapTile(Map map) {
        loadFront(map);
        loadBack(map);
        loadSet(map);
    }

    public void loadFront(Map map) {
        int id = map.getId();
        String url = "/maps/map0" + id + ".txt";

        try {
            InputStream is = getClass().getResourceAsStream(url);
            assert is != null;
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            int col = 0;
            int row = 0;
            while(col < maxWorldCol && row < maxWorldRow) {
                String line = br.readLine();
                while (col < maxWorldCol) {
                    String[] number = line.split(",");
                    int num = Integer.parseInt(number[col]) ;
                    if(num == 0) num = 266;
                    else num--;
                    map.frontTile[col][row] = num;
                    col++;
                }
                if(col == maxWorldCol) {
                    col = 0;
                    row++;
                }
            }
            br.close();
        }catch (Exception ignored) {
        }
    }

    public void loadBack(Map map) {
        int id = map.getId();
        String url = "/maps/background0" + id + ".txt";

        try {
            InputStream is = getClass().getResourceAsStream(url);
            assert is != null;
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            int col = 0;
            int row = 0;
            while(col < maxWorldCol && row < maxWorldRow) {
                String line = br.readLine();
                while (col < maxWorldCol) {
                    String[] number = line.split(",");
                    int num = Integer.parseInt(number[col]) - 1;
                    map.backTile[col][row] = num;
                    col++;
                }
                if(col == maxWorldCol) {
                    col = 0;
                    row++;
                }
            }
            br.close();

        }catch (Exception ignored) {
        }
    }

    public void loadSet(Map map) {
        int id = map.getId();
        String url = "/maps/tileSet0" + id + ".txt";

        try {
            InputStream is = getClass().getResourceAsStream(url);
            assert is != null;
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            int col = 0;
            int row = 0;
            while(col < maxWorldCol && row < maxWorldRow) {
                String line = br.readLine();
                while (col < maxWorldCol) {
                    String[] number = line.split(",");
                    int num = Integer.parseInt(number[col]);
                    map.setTile[col][row] = num;
                    col++;
                }
                if(col == maxWorldCol) {
                    col = 0;
                    row++;
                }
            }
            br.close();

        }catch (Exception ignored) {
        }
    }

    public void setNpc(NPC npc, String line) {
        String[] now = line.split(" ");
        String name = now[1];
        int x = Integer.parseInt(now[2]);
        int y = Integer.parseInt(now[3]);
        boolean move = Boolean.parseBoolean(now[4]);
        int speed = Integer.parseInt(now[5]);
        int HP = Integer.parseInt(now[6]);
        if(HP == 0) {
            npc.setStat(name, speed, x * gp.tileSize, y * gp.tileSize, move);
        }
        else {
            int MP = Integer.parseInt(now[7]);
            int atk = Integer.parseInt(now[8]);
            int def = Integer.parseInt(now[9]);
            npc.setStat(name, speed, x * gp.tileSize, y * gp.tileSize, move, HP, MP, atk, def);
        }
    }

    public void setMob(Mob mob, String line) {
        String[] now = line.split(" ");
        String name = now[1];
        int x = Integer.parseInt(now[2]);
        int y = Integer.parseInt(now[3]);
        int range = Integer.parseInt(now[4]);
        int speed = Integer.parseInt(now[5]);
        int HP = Integer.parseInt(now[6]);
        int MP = Integer.parseInt(now[7]);
        int atk = Integer.parseInt(now[8]);
        int def = Integer.parseInt(now[9]);
        mob.setStat(name, x * gp.tileSize, y * gp.tileSize, range, speed, HP, MP, atk, def);
    }

    public void setObj(SuperObject obj, String line) {
        String[] now = line.split(" ");
        String name = now[1];
        int x = Integer.parseInt(now[2]);
        int y = Integer.parseInt(now[3]);
        int interactionType = Integer.parseInt(now[4]);
        if(interactionType != 1) {
            obj.setStat(name, x * gp.tileSize, y * gp.tileSize, interactionType, 2, "non", 0);
            return;
        }
        String type = now[5];
        int Type = 2;
        switch (type.charAt(0)) {
            case '+' -> Type = 1;
            case '-' -> Type = 0;
        }
        if(Type == 2) {
            obj.setStat(name, x * gp.tileSize, y * gp.tileSize, interactionType, Type, "non", 0);
            return;
        }

        int effectMass = Integer.parseInt(now[6]);
        String effect = now[7];
        obj.setStat(name, x * gp.tileSize, y * gp.tileSize, interactionType, Type, effect, effectMass);

    }

}
