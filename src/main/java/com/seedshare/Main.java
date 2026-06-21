package com.seedshare;

import com.seedshare.service.BusinessQueryService;
import com.seedshare.service.CrudDemoService;
import com.seedshare.service.PlantCrudDemoService;
import com.seedshare.util.DatabaseResetUtil;
import com.seedshare.util.HibernateUtil;

import java.util.Scanner;

public class Main {

    private static final CrudDemoService crudDemo = new CrudDemoService();
    private static final BusinessQueryService businessQuery = new BusinessQueryService();

    public static void main(String[] args) {
        System.out.println("=== Hibernate Seed Share Demo (Java 17 · PostgreSQL · HikariCP) ===\n");

        try {
            HibernateUtil.getEntityManagerFactory();
            System.out.println("БД готова.\n");
        } catch (Exception e) {
            System.err.println("Ошибка инициализации: " + e.getMessage());
            HibernateUtil.close();
            return;
        }

        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.print("""
                    [1] CRUD  [2] Запросы  [3] Всё  [4] CRUD растений  [5] Сброс БД  [0] Выход
                    > """);

            try {
                switch (scanner.nextLine().trim()) {
                    case "1" -> runCrudMenu(scanner);
                    case "2" -> runBusinessMenu(scanner);
                    case "3" -> runAllDemo();
                    case "4" -> PlantCrudDemoService.run();
                    case "5" -> resetDatabase(scanner);
                    case "0" -> running = false;
                    default -> System.out.println("Неверный выбор.");
                }
            } catch (Exception e) {
                System.err.println("Ошибка: " + e.getMessage());
            }
        }

        System.out.println("До свидания!");
        HibernateUtil.close();
    }

    private static void runCrudMenu(Scanner scanner) {
        while (true) {
            System.out.print("""
                    [1] Create  [2] Read  [3] Update  [4] Delete
                    [5] Batch   [6] Транзакция  [7] Всё  [0] Назад
                    > """);

            switch (scanner.nextLine().trim()) {
                case "1" -> crudDemo.demoCreate(scanner);
                case "2" -> crudDemo.demoRead();
                case "3" -> crudDemo.demoUpdate();
                case "4" -> crudDemo.demoDelete();
                case "5" -> crudDemo.demoBatch();
                case "6" -> crudDemo.demoTransaction();
                case "7" -> crudDemo.runAll();
                case "0" -> {
                    return;
                }
                default -> System.out.println("Неверный выбор.");
            }
        }
    }

    private static void runBusinessMenu(Scanner scanner) {
        while (true) {
            System.out.print("""
                    [1] Топ сортов  [2] Надёжность   [3] Доставка
                    [4] Лечение     [5] Наличие      [6] История
                    [7] Незавершённые [8] Активные   [9] Всё  [0] Назад
                    > """);

            switch (scanner.nextLine().trim()) {
                case "1" -> businessQuery.topVarieties();
                case "2" -> businessQuery.gardenerReliability();
                case "3" -> businessQuery.exchangesByDelivery();
                case "4" -> {
                    System.out.print("ID растения [1]: ");
                    String value = scanner.nextLine().trim();
                    businessQuery.treatmentsForPlant(value.isEmpty() ? 1 : Integer.parseInt(value));
                }
                case "5" -> businessQuery.availableSeeds();
                case "6" -> {
                    System.out.print("ID садовода [1]: ");
                    String value = scanner.nextLine().trim();
                    businessQuery.gardenerExchangeHistory(value.isEmpty() ? 1 : Integer.parseInt(value));
                }
                case "7" -> businessQuery.pendingExchanges();
                case "8" -> businessQuery.topActiveGardeners();
                case "9" -> businessQuery.runAll();
                case "0" -> {
                    return;
                }
                default -> System.out.println("Неверный выбор.");
            }
        }
    }

    private static void runAllDemo() {
        System.out.println("\n--- CRUD ---");
        crudDemo.runAll();
        System.out.println("\n--- Бизнес-запросы ---");
        businessQuery.runAll();
        System.out.println("\nГотово.");
    }

    private static void resetDatabase(Scanner scanner) {
        System.out.print("Сбросить БД к исходному состоянию? [y/N]: ");
        String answer = scanner.nextLine().trim().toLowerCase();
        if (!answer.equals("y") && !answer.equals("yes") && !answer.equals("д") && !answer.equals("да")) {
            System.out.println("Сброс отменён.");
            return;
        }
        DatabaseResetUtil.reset();
        System.out.println("БД сброшена к исходному состоянию.\n");
    }
}
