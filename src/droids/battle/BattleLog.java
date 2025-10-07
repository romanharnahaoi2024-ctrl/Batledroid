package droids.battle;

import util.ConsoleColors;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class BattleLog implements Serializable {
    private static final long serialVersionUID = 1L;
    private final List<String> entries = new ArrayList<>();

    public void add(String s) {
        entries.add(s);
    }

    public List<String> getEntries() {
        return new ArrayList<>(entries);
    }

    public void print() {
        System.out.println(ConsoleColors.CYAN + "\n===== ЛОГ БОЮ =====" + ConsoleColors.RESET);
        for (String e : entries) System.out.println(e);
        System.out.println(ConsoleColors.CYAN + "===================" + ConsoleColors.RESET);
    }
}
