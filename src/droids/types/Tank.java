package droids.types;

import droids.base.Droid;

public class Tank extends Droid {
    private int shield = 0;

    public Tank(String name) {
        super(name, 150, 15);
    }

    public void addShield(Droid ally) {
        ally.heal(5);
        shield += 5;
        System.out.println("\u001B[34m" + name + " дає +5 щита " + ally.getName() + "\u001B[0m");
    }

    @Override
    public void takeDamage(int dmg) {
        super.takeDamage(dmg);
    }
}
