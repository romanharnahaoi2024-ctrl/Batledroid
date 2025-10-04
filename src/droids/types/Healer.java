package droids.types;

import droids.base.Droid;

import java.util.Random;

public class Healer extends Droid {
    private final Random random = new Random();

    public Healer(String name) {
        super(name, 90, 15);
    }

    public void healAlly(Droid ally) {
        int amount = random.nextInt(21) + 5; // 5-25 HP
        ally.heal(amount);
        System.out.println("\u001B[32m" + name + " лікує " + ally.getName() + " на " + amount + " HP\u001B[0m");
    }
}
