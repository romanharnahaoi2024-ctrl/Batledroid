package droids.base;

public abstract class Droid {
    protected String name;
    protected int health;
    protected int maxHealth;
    protected int damage;
    protected boolean blocked = false;

    public Droid(String name, int health, int damage) {
        this.name = name;
        this.health = health;
        this.maxHealth = health;
        this.damage = damage;
    }

    public String getName() { return name; }
    public int getHealth() { return health; }
    public boolean isAlive() { return health > 0; }
    public boolean isBlocked() { return blocked; }
    public void setBlocked(boolean b) { blocked = b; }

    public int attack() { return damage; }

    public void takeDamage(int dmg) {
        health -= dmg;
        if (health < 0) health = 0;
    }

    public void heal(int hp) {
        health += hp;
        if (health > maxHealth) health = maxHealth;
    }

    @Override
    public String toString() {
        return name + " [HP=" + health + ", DMG=" + damage + "]";
    }
}
