package droids.types;

import droids.base.Droid;
import droids.battle.BattleLog;
import droids.battle.Arena;
import util.ConsoleColors;

import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Attacker extends Droid {
    private static final long serialVersionUID = 1L;

    public Attacker(String name, int hp, int dmg) {
        super(name, hp, dmg);
    }

    @Override
    public void useAbility(List<Droid> allies, List<Droid> enemies, Scanner sc, boolean isPlayer, BattleLog log, Arena arena) {
        // Attacker — спеціальної здатності немає, робить сильну атаку (але ми залишаємо шанс криту)
        Droid target = chooseTarget(enemies, sc, isPlayer);
        if (target == null) return;
        Random r = new Random();
        int base = attackValue();
        boolean crit = r.nextDouble() < 0.2;
        int dmg = crit ? base * 2 : base;
        dmg = arena.applyModifiers(this, dmg);
        target.takeDamage(dmg);
        String msg = ConsoleColors.RED + getName() + (crit ? " (КРИТ) " : " ") + "атакує " + target.getName() + " на " + dmg + " HP" + ConsoleColors.RESET;
        System.out.println(msg);
        log.add(getName() + (crit ? " (КРИТ)" : "") + " атакує " + target.getName() + " -> -" + dmg);
    }
}
