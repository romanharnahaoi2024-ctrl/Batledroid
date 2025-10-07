package droids.base;

import droids.battle.BattleLog;
import droids.battle.Arena;
import util.ConsoleColors;

import java.io.Serializable;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public abstract class Droid implements Serializable {
    private static final long serialVersionUID = 2L;

    protected String name;
    protected int health;
    protected int maxHealth;
    protected int damage;
    protected boolean blocked = false;     // заблокований (пропускає наступний хід)
    protected int shieldAccum = 0;         // накопичений щит (через Tank), безпечна додаткова max HP
    protected boolean tookDamageThisTurn = false;

    public Droid(String name, int maxHealth, int damage) {
        this.name = name;
        this.maxHealth = maxHealth;
        this.health = maxHealth;
        this.damage = damage;
    }

    public String getName() { return name; }
    public int getHealth() { return health; }
    public int getMaxHealth() { return maxHealth; }
    public int getDamage() { return damage; }
    public boolean isBlocked() { return blocked; }
    public void setBlocked(boolean b) { blocked = b; }

    public boolean isAlive() { return health > 0; }

    // базова атака — може бути перевизначена в підкласах (Attacker робить крит)
    public int attackValue() {
        return damage;
    }

    // застосувати ушкодження (позначає, що дроїд брав ушкодження)
    public void takeDamage(int dmg) {
        if (dmg <= 0) return;
        tookDamageThisTurn = true;
        health -= dmg;
        if (health < 0) health = 0;
    }

    // лікування (не більше maxHealth + shieldAccum)
    public void heal(int amount) {
        if (amount <= 0) return;
        int effectiveMax = maxHealth + shieldAccum;
        health += amount;
        if (health > effectiveMax) health = effectiveMax;
    }

    // Tank додає щит (накопичувальний)
    public void addShield(int amount) {
        if (amount <= 0) return;
        shieldAccum += amount;
        maxHealth += amount;
        health += amount; // одразу додаткове HP
    }

    public void resetTurnFlags() {
        tookDamageThisTurn = false;
    }

    // Поліморфний метод — кожен тип реалізує власну здатність
    // allies/enemies передається як списки (несеріалізовані Scanner/Log)
    public abstract void useAbility(List<Droid> allies, List<Droid> enemies,
                                    Scanner sc, boolean isPlayer, BattleLog log, Arena arena);

    // допоміжний метод вибору цілі (для користувача або для AI)
    protected Droid chooseTarget(List<Droid> list, Scanner sc, boolean isPlayer) {
        // враховує тільки живих дроїдів
        list.removeIf(d -> d != null && !d.isAlive());
        if (list.isEmpty()) return null;
        if (!isPlayer) {
            Random r = new Random();
            return list.get(r.nextInt(list.size()));
        }

        // вибір користувачем
        while (true) {
            System.out.println("Оберіть ціль:");
            for (int i = 0; i < list.size(); i++) {
                System.out.println((i + 1) + " - " + list.get(i).toString());
            }
            String line = sc.nextLine().trim();
            try {
                int idx = Integer.parseInt(line) - 1;
                if (idx >= 0 && idx < list.size()) return list.get(idx);
            } catch (Exception ignored) {}
            System.out.println("Невірний ввід — спробуйте ще раз.");
        }
    }

    @Override
    public String toString() {
        String s = name + " [HP: " + health + "/" + maxHealth;
        if (shieldAccum > 0) s += " (shield+" + shieldAccum + ")";
        s += ", DMG: " + damage + "]";
        if (blocked) s += " " + ConsoleColors.YELLOW + "🔒" + ConsoleColors.RESET;
        return s;
    }
}
