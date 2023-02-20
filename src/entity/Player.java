package entity;

public class Player extends Entity {

    public enum State{idle, move, interact, collectEXP, melee, projectile, die, respawn}
    private State state;

    public final int playerDefaultSpeed = 2 * DefaultSpeed;

    private int CurrentEXP;

    //private final Inventory inventory;

    private int Money;

    private final int costMP;//PHU

    private int CriticalChance; // (0-100)(%)
    private int CriticalDamage; // > 100(%)

    public Player() {
        super();

        setMaxHP(100);
        setMaxMP(100);
        setAttack(10);
        setDefend(4);
        setIn4();
        CurrentEXP = 0;
        //inventory = new Inventory();
        CriticalChance = 5;
        CriticalDamage = 105;
        costMP = 20;

        Money = 0;
    }

    public void setIn4() {
        setHP(getMaxHP());
        setMP(getMaxMP());

        this.state = State.idle;

        setSpeed(playerDefaultSpeed);
        setPos(32 * 3, 32 * 3);

        setDirection("down");
    }

    public void setState(State state) {
        this.state = state;
    }

    public State getState() {
        return state;
    }
    public int getCurrentEXP() {
        return CurrentEXP;
    }

    //public Inventory getInventory() {
//        return inventory;
//    }

    public int getMoney() {
        return Money;
    }

    public void LevelUp() {
        int level = this.getLevel();
        this.CurrentEXP -= level * 100;
        this.setLevel(level + 1);
        this.setMaxHP(this.getMaxHP() + 50);
        this.setHP(this.getMaxHP());
        this.setMaxMP(this.getMaxMP() + 50);
        this.setMP(this.getMaxMP());
        this.setAttack(this.getAttack() + 2);
        this.setDefend(this.getDefend() + 2);
        CriticalDamage += 5;
        CriticalChance += 1;
    }

//    public void Loot(SuperObject obj) {
//        inventory.addObj(obj);
//    }

    public void Hit(Mob M) {
        int Damage = getAttack();
        M.setHP(M.getHP() - Damage);
        if(M.getHP() <= 0) {
            CollectEXP(M.getEXP());
            M.setState(Mob.mobState.die);
        }
    }

    //PHU
    public void Shoot(Mob M) {//projectile
        M.setHP(M.getHP() - this.getAttack()*2);
        if(M.getHP() <= 0) {
            CollectEXP(M.getEXP());
            M.setState(Mob.mobState.die);
        }
        this.setMP(this.getMP() - this.getCostMP());
    }

    public void CollectEXP(int exp) {
        this.CurrentEXP += exp;
        while(this.CurrentEXP >= this.getLevel() * 100)
            LevelUp();
    }

    public void getAttacked(int damage) {
        this.setHP( this.getHP() - damage * (100 - this.getDefend()) / 100 );
        if(this.getHP() <= 0) state = State.die;
    }

    public void respawn() {
        state = State.idle;
        setIn4();
    }

    public void addMoney(int money) {
        Money += money;
    }
    public void lostMoney(int money) {
        Money -= money;
    }

    public int getCostMP() {
        return costMP;
    }

}
