package InputProcess;

import Graphic.GameProcess;
import entity.Player;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {

    public boolean upPressed, downPressed, leftPressed, rightPressed;
    public boolean sprintPressed, crouchPressed, interactPressed, attackPressed, attack2Pressed;
    int up, down, left, right;
    int pause, Return;
    int sprint, crouch, interact, attack, attack2, openInventory;
    GameProcess gp;

    /* Default key control */
    public KeyHandler(GameProcess gp) {
        this.gp = gp;
        up = KeyEvent.VK_W;
        down = KeyEvent.VK_S;
        left = KeyEvent.VK_A;
        right = KeyEvent.VK_D;
        pause = KeyEvent.VK_ESCAPE;
        sprint = KeyEvent.VK_SHIFT;
        crouch = KeyEvent.VK_CONTROL;
        interact = KeyEvent.VK_E;
        attack = KeyEvent.VK_F;
        attack2 = KeyEvent.VK_Q;
        openInventory = KeyEvent.VK_I;
        Return = KeyEvent.VK_ESCAPE;
    }

    public KeyHandler(GameProcess gp, int up, int down, int left, int right) {
        this.gp = gp;
        setUp(up);
        setDown(down);
        setLeft(left);
        setRight(right);
        pause = KeyEvent.VK_ESCAPE;
        sprint = KeyEvent.VK_SHIFT;
        crouch = KeyEvent.VK_CONTROL;
        interact = KeyEvent.VK_E;
        attack = KeyEvent.VK_SPACE;
    }

    public void setUp(int up) {
        this.up = up;
    }

    public void setDown(int down) {
        this.down = down;
    }

    public void setLeft(int left) {
        this.left = left;
    }

    public void setRight(int right) {
        this.right = right;
    }

    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();
        switch (gp.gameState) {
            case Run -> {
                if(code == up)  upPressed = true;
                if(code == down) downPressed = true;
                if(code == left) leftPressed = true;
                if(code == right) rightPressed = true;

                if(code == pause) gp.setGameState(GameProcess.GameState.Pause);

                if(code == sprint) sprintPressed = true;
                if(code == crouch) crouchPressed = true;

                if(code == interact) interactPressed = true;

                if(code == openInventory) gp.setGameState(GameProcess.GameState.InventoryOpen);

                if(code == attack) {
                    attackPressed = true;
                    gp.getPlayer().setState(Player.State.melee);
                }
                if(code == attack2) {
                    attack2Pressed = true;
                    gp.getPlayer().setState(Player.State.projectile);
                }

            }

            case Pause -> {
                if(code == pause) {
                    gp.setGameState(GameProcess.GameState.Run);
                }

                if(code == up) {
                    gp.getUi().command--;
                    if(gp.getUi().command < 0) gp.getUi().command = 0;
                }
                if(code == down) {
                    gp.getUi().command++;
                    if(gp.getUi().command > 2) gp.getUi().command = 2;
                }
                if(code == interact) {
                    switch (gp.getUi().command) {
                        case 0-> gp.setGameState(GameProcess.GameState.Run);
                        case 1-> gp.setGameState(GameProcess.GameState.Respawn);
                        case 2-> {
                            System.out.println("quit");
                            System.exit(0);
                        }
                    }
                }

            }

            case Dialog -> {
                if(code == interact || code == pause) {
                    gp.setGameState(GameProcess.GameState.Run);
                }
            }

            case Title -> {
                if(code == up) {
                    gp.getUi().command--;
                    if(gp.getUi().command < 0) gp.getUi().command = 0;
                }
                if(code == down) {
                    gp.getUi().command++;
                    if(gp.getUi().command > 2) gp.getUi().command = 2;
                }
                if(code == interact) {
                    switch (gp.getUi().command) {
                        case 0-> gp.setGameState(GameProcess.GameState.Run);
                        case 1-> System.out.println("load game");
                        case 2-> {
                            System.out.println("quit");
                            //gp.setGameState(GameAttribute.GameState.Exit);
                            System.exit(0);
                        }
                    }
                }
            }

            case Wasted -> {
                if(code == up) {
                    gp.getUi().command--;
                    if(gp.getUi().command < 0) gp.getUi().command = 0;
                }
                if(code == down) {
                    gp.getUi().command++;
                    if(gp.getUi().command > 2) gp.getUi().command = 2;
                }
                if(code == interact) {
                    switch (gp.getUi().command) {
                        case 0-> {
                            gp.setGameState(GameProcess.GameState.Respawn);
                            gp.getPlayer().setState(Player.State.respawn);
                        }
                        case 1-> {
                            System.out.println("Back");
                            gp.setGameState(GameProcess.GameState.Title);
                        }
                        case 2-> System.exit(0);
                    }
                }
            }

            case InventoryOpen -> {
                if(code == Return)
                    gp.setGameState(GameProcess.GameState.Run);
                if (code == up) {
                    if (gp.getUi().slotRow!=0) {
                        gp.getUi().slotRow--;
                    }
                }

                if (code == left) {
                    if(gp.getUi().slotCol!=0){
                        gp.getUi().slotCol--;
                    }
                }
                if (code == right) {
                    if(gp.getUi().slotCol!=4){
                        gp.getUi().slotCol++;
                    }
                }
                if (code == down) {
                    if(gp.getUi().slotRow!=3){
                        gp.getUi().slotRow++;
                    }}

            }

        }

    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();

        if(code == up) {
            upPressed = false;
        }
        if(code == down) {
            downPressed = false;
        }
        if(code == left) {
            leftPressed = false;
        }
        if(code == right) {
            rightPressed = false;
        }
        if(code == sprint) {
            sprintPressed = false;
        }
        if(code == crouch) {
            crouchPressed = false;
        }
        if(code == interact) {
            interactPressed = false;
        }

        if(code == attack)
            attackPressed = false;
        if(code == attack2)
            attack2Pressed = false;

    }
}
