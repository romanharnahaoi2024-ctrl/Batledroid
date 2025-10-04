package app;

import droids.base.Droid;
import droids.types.*;
import droids.battle.*;
import util.FileManager;

import java.util.*;

public class Main {
    private static final Scanner sc = new Scanner(System.in);
    private static final BattleManager battleManager = new BattleManager();
    private static List<Droid> player1Team = new ArrayList<>();
    private static List<Droid> player2Team = new ArrayList<>();

    public static void main(String[] args) {
        System.out.println("=== Вітаємо у грі 'Битва дроїдів' ===");
        chooseMode();
    }

    private static void chooseMode() {
        System.out.println("Виберіть режим: 1 – Гравець проти гравця, 2 – Гравець проти комп’ютера");
        String mode = sc.nextLine();
        switch (mode) {
            case "1" -> setupTeams(true);
            case "2" -> setupTeams(false);
            default -> { System.out.println("Невірний вибір!"); chooseMode(); }
        }
    }

    private static void setupTeams(boolean twoPlayers) {
        int min = 2, max = 4;

        System.out.println("Гравець 1: оберіть кількість дроїдів (" + min + "-" + max + ")");
        int count1 = Integer.parseInt(sc.nextLine());
        player1Team = createTeam(count1);

        if (twoPlayers) {
            System.out.println("Гравець 2: оберіть кількість дроїдів (" + min + "-" + max + ")");
            int count2 = Integer.parseInt(sc.nextLine());
            player2Team = createTeam(count2);
        } else {
            Random r = new Random();
            int count2 = r.nextInt(max - min + 1) + min;
            player2Team = createTeamAI(count2);
        }

        Arena arena = Arena.randomArena();
        System.out.println("\nБій на арені: " + arena);

        battleManager.startBattle(player1Team, player2Team, arena, twoPlayers);
    }

    private static List<Droid> createTeam(int count) {
        List<Droid> team = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            System.out.println("Виберіть дроїда #" + (i + 1) + " тип: 1-Attacker 2-Tank 3-Healer 4-Locker");
            String type = sc.nextLine();
            System.out.print("Введіть ім'я дроїда: ");
            String name = sc.nextLine();

            Droid d = switch (type) {
                case "1" -> new Attacker(name);
                case "2" -> new Tank(name);
                case "3" -> new Healer(name);
                case "4" -> new Locker(name);
                default -> null;
            };

            if (d != null) {
                team.add(d);
                System.out.println("Додано: " + d);
            } else {
                System.out.println("Невірний тип, спробуйте ще раз.");
                i--;
            }
        }
        return team;
    }

    private static List<Droid> createTeamAI(int count) {
        List<Droid> team = new ArrayList<>();
        Random r = new Random();
        String[] types = {"1","2","3","4"};
        for (int i = 0; i < count; i++) {
            String type = types[r.nextInt(types.length)];
            String name = "AI_" + (i+1);
            Droid d = switch (type) {
                case "1" -> new Attacker(name);
                case "2" -> new Tank(name);
                case "3" -> new Healer(name);
                case "4" -> new Locker(name);
                default -> null;
            };
            team.add(d);
        }
        return team;
    }
}
