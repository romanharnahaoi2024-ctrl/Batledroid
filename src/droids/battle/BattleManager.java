package droids.battle;

import droids.base.Droid;
import droids.types.*;

import java.util.*;

public class BattleManager {
    private BattleLog lastBattle = new BattleLog();
    private final Scanner sc = new Scanner(System.in);
    private final Random random = new Random();

    public void startBattle(List<Droid> team1, List<Droid> team2, Arena arena, boolean twoPlayers) {
        lastBattle = new BattleLog();
        boolean player1Turn = true;

        while (!team1.isEmpty() && !team2.isEmpty()) {
            List<Droid> currentTeam = player1Turn ? team1 : team2;
            List<Droid> enemyTeam = player1Turn ? team2 : team1;

            System.out.println("\n" + (player1Turn ? "Хід Гравця 1" : "Хід " + (twoPlayers ? "Гравця 2" : "Комп’ютера")));

            // Вибір дроїда для ходу
            Droid d;
            if (player1Turn || twoPlayers) {
                d = chooseDroid(currentTeam, true);
            } else {
                d = currentTeam.get(random.nextInt(currentTeam.size()));
            }

            if (d.isBlocked()) {
                System.out.println(d.getName() + " пропускає хід через блокування!");
                d.setBlocked(false);
            } else {
                takeTurn(d, enemyTeam, currentTeam, player1Turn || twoPlayers, arena);
            }

            team1.removeIf(t -> !t.isAlive());
            team2.removeIf(t -> !t.isAlive());

            player1Turn = !player1Turn;
        }

        String winner = team1.isEmpty() ? "Команда 2" : "Команда 1";
        System.out.println("🏆 Перемогла " + winner + "!");
        lastBattle.add("Переможець: " + winner);
        lastBattle.show();
    }

    private Droid chooseDroid(List<Droid> team, boolean isPlayer) {
        if (!isPlayer) return team.get(random.nextInt(team.size()));
        System.out.println("Виберіть дроїда для ходу:");
        for (int i = 0; i < team.size(); i++) {
            System.out.println((i + 1) + " - " + team.get(i));
        }
        int choice = Integer.parseInt(sc.nextLine());
        return team.get(choice - 1);
    }

    private void takeTurn(Droid d, List<Droid> enemies, List<Droid> allies, boolean isPlayer, Arena arena) {
        int action = 1; // default attack
        if (isPlayer) {
            System.out.println("Виберіть дію для " + d.getName() + ": 1-Атака 2-Спецдія");
            action = Integer.parseInt(sc.nextLine());
        } else {
            action = random.nextInt(2) + 1;
        }

        switch (action) {
            case 1 -> { // атака
                Droid target = chooseTarget(enemies, isPlayer);
                int dmg = d.attack();
                dmg = arena.applyModifiers(d, dmg);
                target.takeDamage(dmg);
                System.out.println("\u001B[31m" + d.getName() + " атакує " + target.getName() + " на " + dmg + " HP\u001B[0m");
                lastBattle.add(d.getName() + " атакує " + target.getName() + " на " + dmg);
            }
            case 2 -> { // спецдія
                if (d instanceof Healer healer) {
                    Droid ally = chooseTarget(allies, isPlayer);
                    healer.healAlly(ally);
                    lastBattle.add(d.getName() + " лікує " + ally.getName());
                } else if (d instanceof Tank tank) {
                    Droid ally = chooseTarget(allies, isPlayer);
                    tank.addShield(ally);
                    lastBattle.add(d.getName() + " дає щит " + ally.getName());
                } else if (d instanceof Locker locker) {
                    Droid target = chooseTarget(enemies, isPlayer);
                    locker.block(target);
                    lastBattle.add(d.getName() + " блокує " + target.getName());
                } else { // Attacker може тільки атакувати
                    Droid target = chooseTarget(enemies, isPlayer);
                    int dmg = d.attack();
                    dmg = arena.applyModifiers(d, dmg);
                    target.takeDamage(dmg);
                    System.out.println("\u001B[31m" + d.getName() + " атакує " + target.getName() + " на " + dmg + " HP\u001B[0m");
                    lastBattle.add(d.getName() + " атакує " + target.getName() + " на " + dmg);
                }
            }
        }
    }

    private Droid chooseTarget(List<Droid> targets, boolean isPlayer) {
        if (targets.isEmpty()) return null;
        if (!isPlayer) return targets.get(random.nextInt(targets.size()));

        System.out.println("Виберіть ціль:");
        for (int i = 0; i < targets.size(); i++) {
            System.out.println((i + 1) + " - " + targets.get(i));
        }
        int choice = Integer.parseInt(sc.nextLine());
        return targets.get(choice - 1);
    }
}
