package OutputProcess;

import Graphic.GameProcess;
import object.SuperObject;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class ObjectProcess {

    GameProcess gp;
    //public BufferedImage image;

    public ObjectProcess(GameProcess gp) {
        this.gp = gp;
    }


    public void getImg(SuperObject obj) {
        obj.image = loadImg(obj.getName());
    }

    public BufferedImage loadImg(String name) {
        UtilityTool uT = new UtilityTool();

        BufferedImage image = null;
        try {
            image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/object/" + name + ".png")));
            image = uT.scaleImg(image, gp.tileSize, gp.tileSize);
        }catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    public void draw(Graphics2D g2, SuperObject obj) {
//        int screenX = worldX - gp.player.worldX + gp.player.screenX;
//        int screenY = worldY - gp.player.worldY + gp.player.screenY;
//        if(     worldX + gp.tileSize > gp.player.worldX - gp.player.screenX &&
//                worldX - gp.tileSize < gp.player.worldX + gp.player.screenX &&
//                worldY + gp.tileSize > gp.player.worldY - gp.player.screenY &&
//                worldY - gp.tileSize < gp.player.worldY + gp.player.screenY)
        g2.drawImage(obj.image, obj.getX(), obj.getY(), gp.tileSize, gp.tileSize, null);
    }

}
