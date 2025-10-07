package app;

import droids.battle.BattleManager;
import util.ConsoleColors;
import util.FileManager;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("""
                    
                    ====== БИТВА ДРОЇДІВ ======
                    1 - Нова гра (гравець проти гравця)
                    2 - Нова гра (гравець проти комп'ютера)
                    3 - Завантажити збережену гру
                    4 - Відтворити збережений лог (battle_log.txt)
                    5 - Вийти
                    Виберіть опцію:
                    """);
            String cmd = sc.nextLine().trim();
            switch (cmd) {
                case "1" -> {
                    BattleManager manager = new BattleManager();
                    manager.setupNewGame(sc, false);
                    manager.startBattleInteractive(sc);
                    FileManager.saveState(manager);
                }
                case "2" -> {
                    BattleManager manager = new BattleManager();
                    manager.setupNewGame(sc, true);
                    manager.startBattleInteractive(sc);
                    FileManager.saveState(manager);
                }
                case "3" -> {
                    BattleManager loaded = FileManager.loadState();
                    if (loaded != null) {
                        // ініціалізуємо transient поля
                        loaded.startBattleInteractive(sc);
                        FileManager.saveState(loaded);
                    }
                }
                case "4" -> FileManager.showSavedLog();
                case "5" -> {
                    System.out.println(ConsoleColors.CYAN + "Вихід. До зустрічі!" + ConsoleColors.RESET);
                    return;
                }
                default -> System.out.println("Невідома команда, спробуйте ще раз.");
            }
        }
    }
}
