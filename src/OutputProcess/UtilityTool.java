package OutputProcess;

import java.awt.*;
import java.awt.image.BufferedImage;

public class UtilityTool {
    public BufferedImage scaleImg(BufferedImage original, int width, int height) {
        BufferedImage scale = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = scale.createGraphics();
        g2.drawImage(original, 0, 0, width, height, null);
        g2.dispose();

        return scale;
    }
}
