package droids.battle;

import java.util.ArrayList;
import java.util.List;

public class BattleLog {
    private final List<String> events = new ArrayList<>();

    public void add(String event) { events.add(event); }

    public List<String> getEvents() { return events; }

    public void show() {
        System.out.println("\n--- ЛОГ БОЮ ---");
        events.forEach(System.out::println);
    }
}
