package GameLogic;
import Graphic.GameProcess;
import entity.Mob;
import entity.Player;

public class AttackProcess {
    GameProcess gp;

    public AttackProcess(GameProcess gp) {
        this.gp = gp;
    }

    public boolean attacking(Player A, Mob B) {
        if(checkPMDistance(A,B) == 1) {
            if(A.getState() == Player.State.melee)	{     //melee
                A.Hit(B);
                return true;
            }
            else if(A.getState() == Player.State.projectile && A.getMP()-A.getCostMP() >= 0) {//projectile
                A.Shoot(B);
                return true;
            }
        }
        else if(A.getState() == Player.State.projectile && checkPMDistance(A,B) == 2 && A.getMP()-A.getCostMP() >= 0) {
            A.Shoot(B);
            return true;
        }
        return false;
    }

    public int checkPMDistance(Player A, Mob B) {//check distance between player and mob
        int Ax = A.getPosX() / gp.tileSize;
        int Ay = A.getPosY() / gp.tileSize;

        int Bx = B.getPosX() / gp.tileSize;
        int By = B.getPosY() / gp.tileSize;

        switch (A.getDirection()) {
            case "up":
                if(Ax == Bx) {
                    if(Ay - By <= 1) return 1; //melee or projectile
                    if(Ay - By > 1 && Ay - By <= 4) return 2; //only projectile
                }
                break;
            case "down":
                if(Ax == Bx) {
                    if(-Ay + By <= 1) return 1;
                    if(-Ay + By > 1 && -Ay + By <= 4) return 2;
                }
                break;
            case "left":
                if(Ay == By) {
                    if(Ax - Bx <= 1) return 1;
                    if(Ax - Bx > 1 && Ax - Bx <= 4) return 2;
                }
                break;
            case "right":
                if(Ay == By) {
                    if(-Ax + Bx <= 1) return 1;
                    if(-Ax + Bx > 1 && -Ax + Bx <= 4) return 2;
                }
                break;
        }
        return 0;
    }


}
