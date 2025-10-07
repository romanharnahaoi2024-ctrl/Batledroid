package droids.battle;

import droids.base.Droid;
import droids.types.*;
import util.ConsoleColors;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

public class BattleManager implements Serializable {
    private static final long serialVersionUID = 3L;

    private List<Droid> team1 = new ArrayList<>();
    private List<Droid> team2 = new ArrayList<>();
    private boolean secondIsAI = false;

    // transient (не серіалізуються)
    private transient Scanner sc;
    private transient Random rnd;

    private BattleLog log = new BattleLog();
    private transient Arena arena;

    public BattleManager() {
        initTransients();
    }

    // Після десеріалізації ініціалізувати transient поля
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        initTransients();
        if (log == null) log = new BattleLog();
    }

    private void initTransients() {
        sc = new Scanner(System.in);
        rnd = new Random();
        if (arena == null) arena = Arena.randomArena();
    }

    // Налаштування нової гри (інтерактивно)
    public void setupNewGame(Scanner scanner, boolean secondIsAI) {
        this.sc = scanner;
        this.rnd = new Random();
        this.secondIsAI = secondIsAI;
        this.arena = Arena.randomArena();
        this.log = new BattleLog();
        team1.clear();
        team2.clear();

        System.out.println("\n=== Налаштування команд ===");
        int n1 = askInt(scanner, "Гравець 1: скільки дроїдів у команді? (2-4): ", 2, 4);
        team1 = createTeamInteractive("P1", n1, scanner);

        if (secondIsAI) {
            int n2 = rnd.nextInt(3) + 2; // 2..4
            team2 = createTeamAI("AI", n2);
            System.out.println("Гравець 2 (AI) створив команду з " + n2 + " дроїдів.");
        } else {
            int n2 = askInt(scanner, "Гравець 2: скільки дроїдів у команді? (2-4): ", 2, 4);
            team2 = createTeamInteractive("P2", n2, scanner);
        }

        System.out.println(ConsoleColors.CYAN + "\nОбрана арена: " + arena + ConsoleColors.RESET);
    }

    private int askInt(Scanner scLocal, String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            String line = scLocal.nextLine().trim();
            try {
                int v = Integer.parseInt(line);
                if (v >= min && v <= max) return v;
            } catch (Exception ignored) {}
            System.out.println("Введіть число від " + min + " до " + max + ".");
        }
    }

    private List<Droid> createTeamInteractive(String prefix, int count, Scanner scanner) {
        List<Droid> team = new ArrayList<>();
        System.out.println("\nСтворюємо команду: " + prefix);
        for (int i = 0; i < count; i++) {
            System.out.println("\nОберіть тип для дроїда #" + (i+1) + ": 1-Attacker 2-Healer 3-Tank 4-Locker");
            int t = askInt(scanner, "Введіть тип (1-4): ", 1, 4);
            System.out.print("Ім'я дроїда: ");
            String name = scanner.nextLine().trim();
            switch (t) {
                case 1 -> team.add(new Attacker(name.isEmpty() ? prefix + "_Attacker"+(i+1) : name, 100, 25));
                case 2 -> team.add(new Healer(name.isEmpty() ? prefix + "_Healer"+(i+1) : name, 90, 15));
                case 3 -> team.add(new Tank(name.isEmpty() ? prefix + "_Tank"+(i+1) : name, 150, 15));
                case 4 -> team.add(new Locker(name.isEmpty() ? prefix + "_Locker"+(i+1) : name, 120, 20));
            }
        }
        return team;
    }

    private List<Droid> createTeamAI(String prefix, int count) {
        List<Droid> team = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            int t = rnd.nextInt(4) + 1;
            switch (t) {
                case 1 -> team.add(new Attacker(prefix + "_Attacker" + (i+1), 100, 25));
                case 2 -> team.add(new Healer(prefix + "_Healer" + (i+1), 90, 15));
                case 3 -> team.add(new Tank(prefix + "_Tank" + (i+1), 150, 15));
                default -> team.add(new Locker(prefix + "_Locker" + (i+1), 120, 20));
            }
        }
        return team;
    }

    // Повернути лог для збереження текстом
    public BattleLog getLog() {
        return log;
    }

    // Основний метод гри — хід по одному дроїду
    public void startBattleInteractive(Scanner scanner) {
        this.sc = scanner;
        if (arena == null) arena = Arena.randomArena();
        log.add("Початок бою на арені: " + arena.getName());
        boolean player1Turn = true;

        while (hasAlive(team1) && hasAlive(team2)) {
            List<Droid> current = player1Turn ? team1 : team2;
            List<Droid> enemy = player1Turn ? team2 : team1;
            boolean isHumanTurn = player1Turn || (!player1Turn && !secondIsAI);

            System.out.println(ConsoleColors.CYAN + "\n=== Хід " + (player1Turn ? "Гравця 1" : (secondIsAI ? "AI" : "Гравця 2")) + " ===" + ConsoleColors.RESET);
            printTeams();

            // вибір активного дроїда (один дроїд ходить)
            Droid active = chooseActiveDroid(current, isHumanTurn);

            if (active == null) {
                System.out.println("Немає доступних дроїдів у команді.");
                player1Turn = !player1Turn;
                continue;
            }

            if (!active.isAlive()) {
                System.out.println(active.getName() + " — мертвий, пропускаємо.");
                player1Turn = !player1Turn;
                continue;
            }

            if (active.isBlocked()) {
                System.out.println(ConsoleColors.YELLOW + active.getName() + " був заблокований і пропускає хід!" + ConsoleColors.RESET);
                log.add(active.getName() + " пропускає хід через блокування.");
                active.setBlocked(false);
                player1Turn = !player1Turn;
                continue;
            }

            // вибір дії
            int action;
            if (isHumanTurn) {
                action = askInt(scanner, "Оберіть дію: 1 - Атака, 2 - Спеціальна дія: ", 1, 2);
            } else {
                action = rnd.nextInt(2) + 1;
            }

            if (action == 1) {
                // атака
                Droid target = chooseTarget(enemy, isHumanTurn);
                if (target == null) {
                    System.out.println("Немає цілі для атаки.");
                } else {
                    int base = active.attackValue();
                    int dmg = arena.applyModifiers(active, base);
                    target.takeDamage(dmg);
                    String out = ConsoleColors.RED + active.getName() + " атакує " + target.getName() + " на " + dmg + " HP" + ConsoleColors.RESET;
                    System.out.println(out);
                    log.add(active.getName() + " атакує " + target.getName() + " -> -" + dmg);
                }
            } else {
                // спецдія (поліморфний виклик)
                try {
                    active.useAbility(current, enemy, scanner, isHumanTurn, log, arena);
                } catch (Exception ex) {
                    System.out.println("Помилка під час використання здібності: " + ex.getMessage());
                }
            }

            // очистка померлих
            team1 = team1.stream().filter(Droid::isAlive).collect(Collectors.toList());
            team2 = team2.stream().filter(Droid::isAlive).collect(Collectors.toList());

            player1Turn = !player1Turn;
        }

        // резульат
        if (!hasAlive(team1) && !hasAlive(team2)) {
            System.out.println(ConsoleColors.YELLOW + "Нічия!" + ConsoleColors.RESET);
            log.add("Результат: Нічия");
        } else if (hasAlive(team1)) {
            System.out.println(ConsoleColors.GREEN + "Команда 1 перемогла!" + ConsoleColors.RESET);
            log.add("Результат: Команда 1 перемогла");
        } else {
            System.out.println(ConsoleColors.GREEN + "Команда 2 перемогла!" + ConsoleColors.RESET);
            log.add("Результат: Команда 2 перемогла");
        }

        // вивести лог, зберегти лог у текстовий файл
        log.print();
        util.FileManager.saveLog(log);
    }

    private boolean hasAlive(List<Droid> team) {
        return team.stream().anyMatch(Droid::isAlive);
    }

    private Droid chooseActiveDroid(List<Droid> team, boolean isHuman) {
        List<Droid> alive = team.stream().filter(Droid::isAlive).collect(Collectors.toList());
        if (alive.isEmpty()) return null;
        if (!isHuman) return alive.get(rnd.nextInt(alive.size()));

        // людина обирає
        System.out.println("Оберіть дроїда для ходу:");
        for (int i = 0; i < alive.size(); i++) {
            System.out.println((i + 1) + " - " + alive.get(i));
        }
        while (true) {
            String line = sc.nextLine().trim();
            try {
                int idx = Integer.parseInt(line) - 1;
                if (idx >= 0 && idx < alive.size()) return alive.get(idx);
            } catch (Exception ignored) {}
            System.out.println("Невірний вибір, спробуйте ще раз.");
        }
    }

    private Droid chooseTarget(List<Droid> enemyTeam, boolean isHuman) {
        List<Droid> alive = enemyTeam.stream().filter(Droid::isAlive).collect(Collectors.toList());
        if (alive.isEmpty()) return null;
        if (!isHuman) return alive.get(rnd.nextInt(alive.size()));

        System.out.println("Оберіть ціль:");
        for (int i = 0; i < alive.size(); i++) {
            System.out.println((i + 1) + " - " + alive.get(i));
        }
        while (true) {
            String line = sc.nextLine().trim();
            try {
                int idx = Integer.parseInt(line) - 1;
                if (idx >= 0 && idx < alive.size()) return alive.get(idx);
            } catch (Exception ignored) {}
            System.out.println("Невірний вибір, спробуйте ще раз.");
        }
    }

    private void printTeams() {
        System.out.println(ConsoleColors.CYAN + "\n--- Команда 1 ---" + ConsoleColors.RESET);
        team1.forEach(d -> System.out.println(d));
        System.out.println(ConsoleColors.CYAN + "\n--- Команда 2 ---" + ConsoleColors.RESET);
        team2.forEach(d -> System.out.println(d));
    }
}
