package droids.types;

import droids.base.Droid;
import droids.battle.BattleLog;
import droids.battle.Arena;
import util.ConsoleColors;

import java.util.List;
import java.util.Scanner;

public class Tank extends Droid {
    private static final long serialVersionUID = 1L;

    public Tank(String name, int hp, int dmg) {
        super(name, hp, dmg);
    }

    @Override
    public void useAbility(List<Droid> allies, List<Droid> enemies, Scanner sc, boolean isPlayer, BattleLog log, Arena arena) {
        // дає щит: додає +5 до maxHealth та +5 до поточного HP (накопичувально)
        Droid ally = chooseTarget(allies, sc, isPlayer);
        if (ally == null) return;
        ally.addShield(5);
        String msg = ConsoleColors.BLUE + getName() + " дає щит +5 " + ally.getName() + ConsoleColors.RESET;
        System.out.println(msg);
        log.add(getName() + " дає +5 щита " + ally.getName());
    }
}
