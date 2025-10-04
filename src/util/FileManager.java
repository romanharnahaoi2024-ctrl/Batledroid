package util;

import droids.battle.BattleLog;

import java.io.*;
import java.util.List;

public class FileManager {

    public static void saveLog(BattleLog log, String filename) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (String line : log.getEvents()) {
                writer.write(line);
                writer.newLine();
            }
            System.out.println("Лог бою збережено у файл: " + filename);
        } catch (IOException e) {
            System.out.println("Помилка збереження файлу: " + e.getMessage());
        }
    }

    public static void loadLog(String filename) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            System.out.println("\n--- ВІДТВОРЕННЯ ЛОГУ ---");
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            System.out.println("Помилка читання файлу: " + e.getMessage());
        }
    }
}
