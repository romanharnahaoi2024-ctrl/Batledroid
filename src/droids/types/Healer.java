package droids.types;

import droids.base.Droid;
import droids.battle.BattleLog;
import droids.battle.Arena;
import util.ConsoleColors;

import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Healer extends Droid {
    private static final long serialVersionUID = 1L;

    public Healer(String name, int hp, int dmg) {
        super(name, hp, dmg);
    }

    @Override
    public void useAbility(List<Droid> allies, List<Droid> enemies, Scanner sc, boolean isPlayer, BattleLog log, Arena arena) {
        // Хіл від 5 до 25 (випадково)
        Droid ally = chooseTarget(allies, sc, isPlayer);
        if (ally == null) return;
        Random r = new Random();
        int amount = r.nextInt(21) + 5; // 5..25
        int before = ally.getHealth();
        ally.heal(amount);
        int healed = ally.getHealth() - before;
        String msg = ConsoleColors.GREEN + getName() + " лікує " + ally.getName() + " на " + healed + " HP" + ConsoleColors.RESET;
        System.out.println(msg);
        log.add(getName() + " лікує " + ally.getName() + " +" + healed);
    }
}
