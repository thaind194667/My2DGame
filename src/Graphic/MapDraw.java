package Graphic;

import OutputProcess.UtilityTool;
import entity.Mob;
import entity.NPC;
import map.Map;
import object.SuperObject;
import tile.Tile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.util.Objects;

public class MapDraw {

    int currentMap;
    Tile[] tiles;
    GameProcess gp;

    public boolean drawPath = true;

    public MapDraw(GameProcess gp) {
        this.gp = gp;
        currentMap = 1;
        tiles = new Tile[2000];
        getTileImage();
    }

    public void getTileImage() {

        UtilityTool uT = new UtilityTool();
        for(int i = 0; i <= 1766; i++) {
            String url = "/tile/tile";
            //if(i > 310 && i < 594) continue;
            if(i < 10) url = url + "00" + i + ".png";
            else if(i < 100) url = url + "0" + i + ".png";
            else url = url + i + ".png";
            tiles[i] = new Tile();
            try {
                tiles[i].image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream(url)));
                //if(tile[i].image == null) continue;
                tiles[i].image = uT.scaleImg(tiles[i].image, gp.tileSize, gp.tileSize);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public void drawMap(Graphics2D g2, Map map) {

        int worldCol = 0;
        int worldRow = 0;
        while (worldCol < gp.maxWorldCol && worldRow < gp.maxWorldRow) {
            int back = map.backTile[worldCol][worldRow];
            int front = map.frontTile[worldCol][worldRow];
            int worldX = worldCol * gp.tileSize;
            int worldY = worldRow * gp.tileSize;
            g2.drawImage(tiles[back].image, worldX, worldY, gp.tileSize, gp.tileSize, null);
            g2.drawImage(tiles[front].image, worldX, worldY, gp.tileSize, gp.tileSize, null);
            worldCol++;
            if(worldCol == gp.maxWorldCol) {
                worldCol = 0;
                worldRow++;
            }
        }

        if(drawPath) {
            g2.setColor(Color.RED);
            for(int i = 0; i < gp.pFinder.pathList.size(); i++) {
                int worldX = gp.pFinder.pathList.get(i).col * gp.tileSize;
                int worldY = gp.pFinder.pathList.get(i).row * gp.tileSize;

                g2.fillRect(worldX, worldY, gp.tileSize, gp.tileSize);
            }
        }

        //drawEntity(g2, map);

    }

    public void drawEntity(Graphics2D g2, Map map) {

        for (int i = 0; i < map.getNpcs().length; i++) {
            NPC now = map.getNpcs()[i];
            if (now != null) {
                gp.NProcess.draw(g2, now);
            }
            else break;
        }

        for (int i = 0; i < map.getMobs().length; i++) {
            Mob now = map.getMobs()[i];
            if (now != null) {
                gp.MProcess.draw(g2, now);
            }
            else break;
        }

        for(int i = 0; i < map.getObjs().length; i++) {
            SuperObject now = map.getObjs()[i];
            if(now != null) {
                gp.objProcess.draw(g2, now);
            }
        }

    }

    public void draw(Graphics2D g2, Map map) {

        drawMap(g2, map);
        drawEntity(g2, map);

    }

}
