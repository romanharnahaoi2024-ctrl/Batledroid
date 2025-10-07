package util;

import droids.battle.BattleManager;
import droids.battle.BattleLog;

import java.io.*;
import java.nio.file.*;
import java.util.List;

public class FileManager {
    private static final String SAVE_FILE = "battle_save.dat";
    private static final String LOG_FILE = "battle_log.txt";

    // Зберегти стан гри (серіалізація)
    public static void saveState(BattleManager manager) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(SAVE_FILE))) {
            oos.writeObject(manager);
            System.out.println(ConsoleColors.CYAN + "Статус гри збережено у " + SAVE_FILE + ConsoleColors.RESET);
        } catch (IOException e) {
            System.out.println("Помилка збереження: " + e.getMessage());
        }
    }

    // Завантажити стан гри (десеріалізація)
    public static BattleManager loadState() {
        if (!Files.exists(Paths.get(SAVE_FILE))) {
            System.out.println("Файл збереження не знайдено: " + SAVE_FILE);
            return null;
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(SAVE_FILE))) {
            BattleManager manager = (BattleManager) ois.readObject();
            System.out.println(ConsoleColors.CYAN + "Бій завантажено зі " + SAVE_FILE + ConsoleColors.RESET);
            return manager;
        } catch (Exception e) {
            System.out.println("Помилка завантаження: " + e.getMessage());
            return null;
        }
    }

    // Зберегти лог у читаємому текстовому файлі
    public static void saveLog(BattleLog log) {
        try {
            List<String> lines = log.getEntries();
            Files.write(Paths.get(LOG_FILE), lines, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            System.out.println(ConsoleColors.CYAN + "Лог бою збережено у " + LOG_FILE + ConsoleColors.RESET);
        } catch (IOException e) {
            System.out.println("Помилка запису лог-файлу: " + e.getMessage());
        }
    }

    // Показати лог з файлу
    public static void showSavedLog() {
        Path p = Paths.get(LOG_FILE);
        if (!Files.exists(p)) {
            System.out.println("Файлу логу не знайдено: " + LOG_FILE);
            return;
        }
        System.out.println("\n=== ВІДТВОРЕННЯ ЛОГУ ===");
        try {
            Files.lines(p).forEach(System.out::println);
        } catch (IOException e) {
            System.out.println("Помилка читання логу: " + e.getMessage());
        }
    }
}
