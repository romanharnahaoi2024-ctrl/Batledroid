package droids.types;

import droids.base.Droid;
import droids.battle.BattleLog;
import droids.battle.Arena;
import util.ConsoleColors;

import java.util.List;
import java.util.Scanner;

public class Locker extends Droid {
    private static final long serialVersionUID = 1L;

    public Locker(String name, int hp, int dmg) {
        super(name, hp, dmg);
    }

    @Override
    public void useAbility(List<Droid> allies, List<Droid> enemies, Scanner sc, boolean isPlayer, BattleLog log, Arena arena) {
        // блокує одного супротивника на його наступний хід
        Droid target = chooseTarget(enemies, sc, isPlayer);
        if (target == null) return;
        target.setBlocked(true);
        String msg = ConsoleColors.YELLOW + getName() + " заблокував " + target.getName() + ConsoleColors.RESET;
        System.out.println(msg);
        log.add(getName() + " блокує " + target.getName());
    }
}
