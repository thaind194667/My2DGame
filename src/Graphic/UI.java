package Graphic;

import java.awt.*;

public class UI {

    GameProcess gp;
    Graphics2D g2;
    Font arial_40, arial_80B, arial_60, arial_20;
    public boolean messageOn = false;
    public String message = "";

    public String currentDialogue = "";

    public int command = 0;

//    double playTime = 0;
//    DecimalFormat dFormat = new DecimalFormat("#0.00");

    public int slotCol = 0;
    public int slotRow = 0;

    public UI(GameProcess gp) {
        this.gp = gp;

        arial_20 = new Font("Arial", Font.PLAIN, 20);
        arial_40 = new Font("Arial", Font.PLAIN, 40);
        arial_60 = new Font("Arial", Font.PLAIN, 60);
        arial_80B = new Font("Arial", Font.BOLD, 80);
    }

    public void ShowMessage(String text) {
        message = text;
        messageOn = true;
    }

    public void draw(Graphics2D g2) {
        this.g2 = g2;

        switch (gp.gameState) {
            case Run -> {
                g2.setFont(arial_20);
                g2.setColor(Color.black);
                drawFPS();
                drawPlayerStat();
            }
            case Pause, Respawn -> drawPauseScreen();
            case Dialog -> drawDialogue();
            case Title -> drawTitleScreen();
            case InventoryOpen -> drawInventory();
            case Wasted -> drawDeathScreen();

        }
    }

    public void drawFPS() {
        String text = "FPS: " + gp.FPS;
        g2.drawString(text , gp.maxWorldCol * gp.tileSize - (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth() - gp.tileSize / 2,
                (int)g2.getFontMetrics().getStringBounds(text, g2).getHeight());
    }

    public void drawPlayerStat() {

        Color c = new Color(0, 0, 0, 200);  /// fourth number is opacity (0-> no background color, 255->full background color)
        g2.setColor(c);
        g2.fillRoundRect(0, 0, gp.screenWidth, gp.tileSize, 15, 15);
        g2.setColor(Color.WHITE);
        g2.setFont(arial_20);
        String Level = "Level " + gp.getPlayer().getLevel();
        int x = 0;
        int y = (int)g2.getFontMetrics().getStringBounds(Level, g2).getHeight();
        g2.drawString(Level, x, y);

        String CurrentEXP = "EXP: " + gp.getPlayer().getCurrentEXP();
        x += gp.tileSize * 4;
        g2.drawString(CurrentEXP, x, y);
        g2.setFont(arial_20);

        String HP = "HP: " + gp.getPlayer().getHP();
        g2.setColor(Color.RED);
        x += gp.tileSize * 6;
        g2.drawString(HP, x, y);
        g2.setColor(Color.BLUE);

        String MP = "MP: " + gp.getPlayer().getMP();
        x += gp.tileSize * 6;
        g2.drawString(MP, x, y);

    }

    public void drawPauseScreen() {

        g2.setFont(arial_80B);
        g2.setColor(Color.red);
        String text = "Paused";
        int x = getCenter(text);
        int y = gp.screenHeight / 2 - gp.tileSize * 3;

        g2.drawString(text, x, y);

        /// MENU
        g2.setColor(Color.BLACK);
        g2.setFont(arial_40);
        text = "Return";
        x = getCenter(text);
        y += gp.tileSize;
        g2.drawString(text, x, y);
        if(command == 0) {
            g2.drawString(">", x - gp.tileSize, y);
        }

        text = "New game";
        x = getCenter(text);
        y += gp.tileSize;
        g2.drawString(text, x, y);
        if(command == 1) {
            g2.drawString(">", x - gp.tileSize, y);
        }

        text = "Quit";
        x = getCenter(text);
        y += gp.tileSize;
        g2.drawString(text, x, y);
        if(command == 2) {
            g2.drawString(">", x - gp.tileSize , y);
        }
    }

    public void drawDialogue() {
        int x = gp.tileSize;
        int y = gp.tileSize;
        int w = gp.screenWidth - gp.tileSize * 2;
        int h = gp.tileSize * 5;
        drawSubWindow(x, y, w, h);
    }

    public void drawTitleScreen() {

        g2.setFont(arial_80B);
        String text = "A 2d game!";
        int x = getCenter(text);
        int y = gp.tileSize * 3;

        g2.setColor(Color.gray);
        g2.drawString(text, x + 5, y + 5);

        g2.setColor(Color.white);
        g2.drawString(text, x, y);

        x = gp.screenWidth / 2 - gp.tileSize;
        y += gp.tileSize * 2;
        g2.drawImage(gp.PProcess.down[1], x, y, gp.tileSize * 2, gp.tileSize * 2, null);

        /// MENU
        g2.setFont(arial_40);
        text = "New game";
        x = getCenter(text);
        y += gp.tileSize * 3;
        g2.drawString(text, x, y);
        if(command == 0) {
            g2.drawString(">", x - gp.tileSize, y);
        }

        text = "Load game";
        x = getCenter(text);
        y += gp.tileSize;
        g2.drawString(text, x, y);
        if(command == 1) {
            g2.drawString(">", x - gp.tileSize, y);
        }

        text = "Quit";
        x = getCenter(text);
        y += gp.tileSize;
        g2.drawString(text, x, y);
        if(command == 2) {
            g2.drawString(">", x - gp.tileSize , y);
        }
    }

    public void drawInventory() {

        //SuperObject[] now = gp.getPlayer().getInventory().getObj();

        int frameX = gp.tileSize;
        int frameY = gp.tileSize;
        int frameWidth = gp.screenWidth - gp.tileSize * 2;
        int frameHeight = gp.screenHeight - gp.tileSize * 2;
        drawSubWindow(frameX, frameY, frameWidth, frameHeight );

        //slot
        final int slotXstart = frameX+20;
        final int slotYstart = frameY+20;

        int slotWidth = gp.tileSize * 2;
        int slotHeight = gp.tileSize * 2;

        // ve item cua player
//        for(int i = 0;i < now.length;i++)
//        {
//            SuperObject currentObj = now[i];
//            if(currentObj != null) {
//                g2.drawImage(currentObj.image, slotX, slotY, slotWidth, slotHeight, null);
//                slotX += slotWidth;
//                if(i % 10 == 0 && i != 0)
//                {
//                    slotX = slotXstart;
//                    slotY = slotY + slotHeight;
//                }
//            }
//
//        }

        // con tro
        int cursorX = slotXstart + (slotWidth * slotCol );
        int cursorY = slotYstart + (slotHeight * slotRow );

        //ve con tro
        g2.setColor(Color.white);
        g2.setStroke(new BasicStroke(2));
        g2.drawRoundRect(cursorX,cursorY, slotWidth, slotHeight,10,10);
    }

    public void drawDeathScreen() {

        g2.setFont(arial_80B);
        g2.setColor(Color.black);
        String text = "Wasted";
        int x = getCenter(text);
        int y = gp.screenHeight / 4;
        g2.drawString(text, x, y);

        g2.setColor(Color.white);
        g2.setFont(arial_40);
        text = "Respawn";
        x = getCenter(text);
        y += gp.tileSize * 2;
        g2.drawString(text, x, y);
        if(command == 0) {
            g2.drawString(">", x - gp.tileSize, y);
        }

        g2.setFont(arial_40);
        text = "Back to start menu";
        x = getCenter(text);
        y += gp.tileSize;
        g2.drawString(text, x, y);
        if(command == 1) {
            g2.drawString(">", x - gp.tileSize, y);
        }

        g2.setFont(arial_40);
        text = "Quit";
        x = getCenter(text);
        y += gp.tileSize;
        g2.drawString(text, x, y);
        if(command == 2) {
            g2.drawString(">", x - gp.tileSize, y);
        }
    }


    public void drawSubWindow(int x, int y, int width, int height) {
        Color c = new Color(0, 0, 0, 150);  /// fourth number is opacity (0-> no background color, 255->full background color)
        g2.setColor(c);
        g2.fillRoundRect(x, y, width, height, 15, 15);
        g2.setFont(arial_40);
        g2.setColor(Color.white);
        g2.drawString(currentDialogue, x + gp.tileSize, y + gp.tileSize);
    }
    public int getCenter(String text) {
        int length = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        return gp.screenWidth / 2 - length / 2;
    }

}
