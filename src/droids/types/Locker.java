package droids.types;

import droids.base.Droid;

import java.util.Random;

public class Locker extends Droid {
    private final Random random = new Random();

    public Locker(String name) {
        super(name, 120, 20);
    }

    public void block(Droid enemy) {
        enemy.setBlocked(true);
        System.out.println("\u001B[33m" + name + " заблокував " + enemy.getName() + "\u001B[0m");
    }
}
