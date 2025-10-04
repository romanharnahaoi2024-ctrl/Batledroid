package droids.types;

import droids.base.Droid;

import java.util.Random;

public class Attacker extends Droid {
    private final Random random = new Random();

    public Attacker(String name) {
        super(name, 100, 25);
    }

    @Override
    public int attack() {
        if (random.nextDouble() < 0.2) { // 20% шанс криту
            System.out.println("\u001B[31m" + name + " робить КРИТИЧНИЙ удар!\u001B[0m");
            return damage * 2;
        }
        return damage;
    }
}
