package droids.battle;

import droids.base.Droid;

import java.io.Serializable;
import java.util.Random;

public class Arena implements Serializable {
    private static final long serialVersionUID = 1L;
    private final String name;
    private final double accuracyModifier; // 0..1 — шанс влучити
    private final double damageModifier;   // множник шкоди
    private static final Random rnd = new Random();

    public Arena(String name, double accuracyModifier, double damageModifier) {
        this.name = name;
        this.accuracyModifier = accuracyModifier;
        this.damageModifier = damageModifier;
    }

    public int applyModifiers(Droid attacker, int baseDamage) {
        if (rnd.nextDouble() > accuracyModifier) {
            // промах
            return 0;
        }
        return (int)Math.round(baseDamage * damageModifier);
    }

    public String getName() { return name; }

    public static Arena randomArena() {
        String[] names = {"Пустеля", "База Альянсу", "Місячна поверхня"};
        double[] acc = {0.88, 1.0, 0.82};
        double[] dmg = {1.0, 0.95, 1.15};
        int i = rnd.nextInt(names.length);
        return new Arena(names[i], acc[i], dmg[i]);
    }

    @Override
    public String toString() {
        return name + " (точність×" + accuracyModifier + ", шкода×" + damageModifier + ")";
    }
}
