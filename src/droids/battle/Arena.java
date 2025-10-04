package droids.battle;

import droids.base.Droid;

import java.util.Random;

public class Arena {
    private String name;
    private double accuracyModifier;
    private double damageModifier;
    private static final Random random = new Random();

    public Arena(String name, double accuracyModifier, double damageModifier) {
        this.name = name;
        this.accuracyModifier = accuracyModifier;
        this.damageModifier = damageModifier;
    }

    public int applyModifiers(Droid attacker, int baseDamage) {
        if (random.nextDouble() > accuracyModifier) {
            System.out.println(attacker.getName() + " промахнувся!");
            return 0;
        }
        return (int) Math.round(baseDamage * damageModifier);
    }

    public static Arena randomArena() {
        String[] names = {"Пустеля", "База Альянсу", "Місячна поверхня"};
        double[] accMods = {0.9, 1.0, 0.8};
        double[] dmgMods = {1.0, 0.95, 1.2};
        int i = random.nextInt(names.length);
        return new Arena(names[i], accMods[i], dmgMods[i]);
    }

    @Override
    public String toString() {
        return name + " (точність ×" + accuracyModifier + ", шкода ×" + damageModifier + ")";
    }
}
