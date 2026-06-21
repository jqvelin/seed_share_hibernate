package com.seedshare.service;

import com.seedshare.entity.Exchange;
import com.seedshare.entity.ExchangeItem;
import com.seedshare.entity.Gardener;
import com.seedshare.entity.Issue;
import com.seedshare.entity.Plant;
import com.seedshare.entity.PlantIssue;
import com.seedshare.entity.Seed;
import com.seedshare.entity.Variety;
import com.seedshare.repository.ExchangeItemRepository;
import com.seedshare.repository.GardenerRepository;
import com.seedshare.repository.GenericRepository;
import com.seedshare.repository.PlantIssueRepository;
import com.seedshare.repository.PlantRepository;
import com.seedshare.repository.SeedRepository;
import com.seedshare.repository.VarietyRepository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CrudDemoService {

    private final GardenerRepository gardenerRepo = new GardenerRepository();
    private final PlantRepository plantRepo = new PlantRepository();
    private final VarietyRepository varietyRepo = new VarietyRepository();
    private final SeedRepository seedRepo = new SeedRepository();
    private final PlantIssueRepository plantIssueRepo = new PlantIssueRepository();
    private final ExchangeItemRepository exchangeItemRepo = new ExchangeItemRepository();
    private final GenericRepository<Exchange, Integer> exchangeRepo = new GenericRepository<>(Exchange.class);
    private final GenericRepository<Issue, Integer> issueRepo = new GenericRepository<>(Issue.class);

    public void demoCreate(Scanner scanner) {
        printHeader("CREATE — Создание записи");
        System.out.print("""
                Что добавить?
                [1] Садовод  [2] Растение  [3] Сорт  [4] Семена
                [5] Обмен    [6] Связь растение-проблема  [0] Отмена
                > """);

        switch (scanner.nextLine().trim()) {
            case "1" -> createGardener(scanner);
            case "2" -> createPlant(scanner);
            case "3" -> createVariety(scanner);
            case "4" -> createSeed(scanner);
            case "5" -> createExchange(scanner);
            case "6" -> createPlantIssue(scanner);
            case "0" -> System.out.println("Создание отменено.");
            default -> System.out.println("Неверный выбор.");
        }

        printDivider();
    }

    private void createGardener(Scanner scanner) {
        String username = ask(scanner, "Имя пользователя: ");
        if (username.isEmpty()) {
            System.out.println("Имя пользователя не может быть пустым.");
            return;
        }
        BigDecimal rating = askRating(scanner, "Рейтинг 0..5 (Enter — пропустить): ");
        if (rating == BAD_DECIMAL) {
            return;
        }
        try {
            Gardener gardener = gardenerRepo.save(new Gardener(username, rating));
            System.out.printf("Создан садовод: id=%d, %s%n", gardener.getId(), gardener.getUsername());
        } catch (Exception e) {
            System.out.println("Не удалось создать садовода: " + e.getMessage());
        }
    }

    private void createPlant(Scanner scanner) {
        String name = ask(scanner, "Название растения: ");
        if (name.isEmpty()) {
            System.out.println("Название не может быть пустым.");
            return;
        }
        try {
            Plant plant = plantRepo.save(new Plant(name));
            System.out.printf("Создано растение: id=%d, %s%n", plant.getId(), plant.getName());
        } catch (Exception e) {
            System.out.println("Не удалось создать растение: " + e.getMessage());
        }
    }

    private void createVariety(Scanner scanner) {
        listPlants();
        Plant plant = askPlant(scanner);
        if (plant == null) {
            return;
        }
        String name = ask(scanner, "Название сорта: ");
        if (name.isEmpty()) {
            System.out.println("Название не может быть пустым.");
            return;
        }
        String conditions = ask(scanner, "Условия выращивания (Enter — пропустить): ");
        BigDecimal rating = askRating(scanner, "Рейтинг 0..5 (Enter — пропустить): ");
        if (rating == BAD_DECIMAL) {
            return;
        }
        try {
            Variety variety = varietyRepo.save(new Variety(plant, name, conditions.isEmpty() ? null : conditions, rating));
            System.out.printf("Создан сорт: id=%d, %s (растение %s)%n", variety.getId(), variety.getName(), plant.getName());
        } catch (Exception e) {
            System.out.println("Не удалось создать сорт: " + e.getMessage());
        }
    }

    private void createSeed(Scanner scanner) {
        listVarieties();
        Variety variety = askVariety(scanner);
        if (variety == null) {
            return;
        }
        listGardeners();
        Gardener gardener = askGardener(scanner);
        if (gardener == null) {
            return;
        }
        Integer harvestYear = askOptionalInt(scanner, "Год сбора (Enter — пропустить): ");
        if (harvestYear == BAD_INT) {
            return;
        }
        String lineage = ask(scanner, "Родословная (Enter — пропустить): ");
        Integer packets = askOptionalInt(scanner, "Количество пакетиков: ");
        if (packets == BAD_INT) {
            return;
        }
        if (packets == null || packets <= 0) {
            System.out.println("Количество пакетиков должно быть больше нуля.");
            return;
        }
        try {
            Seed seed = seedRepo.save(new Seed(variety, gardener, harvestYear, lineage.isEmpty() ? null : lineage, packets));
            System.out.printf("Добавлены семена: id=%d, сорт=%s, садовод=%s, пакетиков=%d%n",
                    seed.getId(), variety.getName(), gardener.getUsername(), seed.getPacketsCount());
        } catch (Exception e) {
            System.out.println("Не удалось добавить семена: " + e.getMessage());
        }
    }

    private void createExchange(Scanner scanner) {
        System.out.print("""
                Способ доставки:
                [1] in_person  [2] mail  [3] courier  [4] pickup_point  [5] other  [0] не указывать
                > """);
        String[] methods = {"in_person", "mail", "courier", "pickup_point", "other"};
        String choice = scanner.nextLine().trim();
        String deliveryMethod = null;
        switch (choice) {
            case "1" -> deliveryMethod = methods[0];
            case "2" -> deliveryMethod = methods[1];
            case "3" -> deliveryMethod = methods[2];
            case "4" -> deliveryMethod = methods[3];
            case "5" -> deliveryMethod = methods[4];
            case "0", "" -> deliveryMethod = null;
            default -> {
                System.out.println("Неверный выбор способа доставки.");
                return;
            }
        }
        String completedAnswer = ask(scanner, "Передача завершена? [y/N]: ").toLowerCase();
        boolean completed = completedAnswer.equals("y") || completedAnswer.equals("yes")
                || completedAnswer.equals("д") || completedAnswer.equals("да");
        try {
            Exchange exchange = exchangeRepo.save(new Exchange(completed, deliveryMethod));
            System.out.printf("Создан обмен: id=%d, доставка=%s, завершён=%b%n",
                    exchange.getId(), deliveryMethod == null ? "—" : deliveryMethod, exchange.isTransferCompleted());
        } catch (Exception e) {
            System.out.println("Не удалось создать обмен: " + e.getMessage());
        }
    }

    private void createPlantIssue(Scanner scanner) {
        listPlants();
        Plant plant = askPlant(scanner);
        if (plant == null) {
            return;
        }
        listIssues();
        Issue issue = askIssue(scanner);
        if (issue == null) {
            return;
        }
        String treatment = ask(scanner, "Рекомендация по лечению (Enter — пропустить): ");
        try {
            PlantIssue link = plantIssueRepo.save(new PlantIssue(plant, issue, treatment.isEmpty() ? null : treatment));
            System.out.printf("Создана связь: растение=%s, проблема=%s %s%n",
                    plant.getName(), issue.getName(), link.getId());
        } catch (Exception e) {
            System.out.println("Не удалось создать связь: " + e.getMessage());
        }
    }

    public void demoCreateSample() {
        printHeader("CREATE — Создание записей");

        long suffix = System.currentTimeMillis() % 100000;

        Gardener gardener = gardenerRepo.save(new Gardener("Тестовый_Садовод_" + suffix, BigDecimal.valueOf(4.40)));
        System.out.printf("Создан садовод: id=%d, %s%n", gardener.getId(), gardener.getUsername());

        Plant plant = plantRepo.save(new Plant("Тестовое растение " + suffix));
        System.out.printf("Создано растение: id=%d, %s%n", plant.getId(), plant.getName());

        Variety variety = varietyRepo.save(new Variety(plant, "Тестовый сорт " + suffix, "Солнце, умеренный полив", BigDecimal.valueOf(4.30)));
        System.out.printf("Создан сорт: id=%d, %s%n", variety.getId(), variety.getName());

        Issue issue = issueRepo.findById(1).orElseThrow();
        PlantIssue link = plantIssueRepo.save(new PlantIssue(plant, issue, "Промывание листьев и мыльный раствор"));
        System.out.printf("Связь с проблемой создана: %s%n", link.getId());

        Seed seed = seedRepo.save(new Seed(variety, gardener, 2024, "Собран на учебной грядке", 3));
        System.out.printf("Добавлены семена: id=%d, пакетиков=%d%n", seed.getId(), seed.getPacketsCount());

        Exchange exchange = exchangeRepo.save(new Exchange(false, "mail"));
        exchangeItemRepo.save(new ExchangeItem(exchange, seed, 1));
        System.out.printf("Создан обмен: id=%d, доставка=%s%n", exchange.getId(), exchange.getDeliveryMethod());

        printDivider();
    }

    public void demoRead() {
        printHeader("READ — Чтение данных");

        System.out.println("Все садоводы:");
        System.out.printf("     %-5s %-24s %-8s%n", "ID", "Username", "Рейтинг");
        System.out.println("     " + "─".repeat(40));
        for (Gardener gardener : gardenerRepo.findAll()) {
            System.out.printf("     %-5d %-24s %-8s%n",
                    gardener.getId(), truncate(gardener.getUsername(), 23), gardener.getRating());
        }

        System.out.println("\nВсе растения:");
        System.out.printf("     %-5s %-24s%n", "ID", "Название");
        System.out.println("     " + "─".repeat(31));
        for (Plant plant : plantRepo.findAll()) {
            System.out.printf("     %-5d %-24s%n", plant.getId(), truncate(plant.getName(), 23));
        }

        System.out.println("\nВсе сорта:");
        System.out.printf("     %-5s %-18s %-24s %-8s%n", "ID", "Растение", "Название", "Рейтинг");
        System.out.println("     " + "─".repeat(58));
        for (Variety variety : varietyRepo.findAllWithPlant()) {
            System.out.printf("     %-5d %-18s %-24s %-8s%n",
                    variety.getId(),
                    truncate(variety.getPlant().getName(), 17),
                    truncate(variety.getName(), 23),
                    variety.getRating());
        }

        System.out.println("\nПоиск садовода по id=1:");
        gardenerRepo.findById(1).ifPresentOrElse(
                g -> System.out.println("     " + g),
                () -> System.out.println("     Не найден"));

        System.out.println("\nПоиск садовода по username=Елена_Садовод:");
        gardenerRepo.findByUsername("Елена_Садовод").ifPresentOrElse(
                g -> System.out.println("     " + g),
                () -> System.out.println("     Не найден"));

        System.out.println("\nПоиск связи растение-проблема: plant=1, issue=1:");
        plantIssueRepo.findByKey(1, 1).ifPresentOrElse(
                pi -> System.out.println("     " + pi),
                () -> System.out.println("     Не найдено"));

        printDivider();
    }

    public void demoUpdate() {
        printHeader("UPDATE — Обновление данных");

        gardenerRepo.findById(1).ifPresent(gardener -> {
            BigDecimal oldRating = gardener.getRating();
            gardener.setRating(BigDecimal.valueOf(4.95));
            gardenerRepo.update(gardener);
            System.out.printf("Обновлён рейтинг садовода id=1: %s → %s%n", oldRating, gardener.getRating());
        });

        varietyRepo.findById(1).ifPresent(variety -> {
            String oldName = variety.getName();
            String newName = oldName.endsWith(" учебный") ? oldName : oldName + " учебный";
            variety.setName(newName);
            varietyRepo.update(variety);
            System.out.printf("Обновлён сорт id=1: '%s' → '%s'%n", oldName, variety.getName());
        });

        printDivider();
    }

    public void demoDelete() {
        printHeader("DELETE — Удаление данных");

        long suffix = System.currentTimeMillis() % 100000;
        Gardener temp = gardenerRepo.save(new Gardener("Удалить_" + suffix, BigDecimal.valueOf(3.50)));
        System.out.printf("Создан временный садовод id=%d%n", temp.getId());

        boolean deleted = gardenerRepo.deleteById(temp.getId());
        System.out.printf("Удалён садовод id=%d (успех=%b)%n", temp.getId(), deleted);

        boolean notFound = gardenerRepo.deleteById(99999);
        System.out.printf("Удаление несуществующего id=99999 (успех=%b)%n", notFound);

        printDivider();
    }

    public void demoBatch() {
        printHeader("BATCH — Массовая вставка");

        long suffix = System.currentTimeMillis() % 100000;
        Plant plant = plantRepo.save(new Plant("Batch растение " + suffix));
        System.out.printf("Создано растение: id=%d, %s%n", plant.getId(), plant.getName());

        List<PlantIssue> items = new ArrayList<>();
        items.add(new PlantIssue(plant, issueRepo.findById(1).orElseThrow(), "Мыльный раствор"));
        items.add(new PlantIssue(plant, issueRepo.findById(6).orElseThrow(), "Бордоская жидкость"));
        items.add(new PlantIssue(plant, issueRepo.findById(7).orElseThrow(), "Опрыскивание серой"));

        long start = System.nanoTime();
        int inserted = plantIssueRepo.batchInsert(items);
        long elapsed = (System.nanoTime() - start) / 1_000_000;
        System.out.printf("Вставлено %d связей за %d мс%n", inserted, elapsed);

        plantRepo.deleteById(plant.getId());
        System.out.printf("Растение id=%d удалено, связи удалены каскадно%n", plant.getId());

        printDivider();
    }

    public void demoTransaction() {
        printHeader("TRANSACTION — Регистрация обмена");
        System.out.println("Попытка создать обмен: seed=1 (1 пакетик) и seed=4 (1 пакетик)");

        Integer exchangeId = null;
        try {
            exchangeId = exchangeItemRepo.registerExchange("courier", 1, 1, 4, 1);
            System.out.printf("Обмен создан! id=%d%n", exchangeId);
            System.out.println("Позиции обмена:");
            for (ExchangeItem item : exchangeItemRepo.findByExchange(exchangeId)) {
                System.out.printf("     seed=%d, packets=%d%n",
                        item.getId().getSeedId(), item.getPacketsCount());
            }

            System.out.println("Повторная попытка с заведомо большим количеством пакетиков...");
            try {
                exchangeItemRepo.registerExchange("courier", 1, 999, 4, 999);
            } catch (IllegalStateException e) {
                System.out.printf("Ожидаемая ошибка: %s%n", e.getMessage());
            }
        } finally {
            if (exchangeId != null) {
                exchangeItemRepo.deleteExchangeWithRestock(exchangeId);
                System.out.printf("Тестовый обмен id=%d удалён, пакетики восстановлены%n", exchangeId);
            }
        }

        printDivider();
    }

    public void runAll() {
        demoCreateSample();
        demoRead();
        demoUpdate();
        demoDelete();
        demoBatch();
        demoTransaction();
    }

    private static final BigDecimal BAD_DECIMAL = new BigDecimal("-1");
    private static final Integer BAD_INT = Integer.MIN_VALUE;

    private String ask(Scanner scanner, String label) {
        System.out.print(label);
        return scanner.nextLine().trim();
    }

    private BigDecimal askRating(Scanner scanner, String label) {
        String value = ask(scanner, label);
        if (value.isEmpty()) {
            return null;
        }
        try {
            BigDecimal rating = new BigDecimal(value.replace(',', '.'));
            if (rating.compareTo(BigDecimal.ZERO) < 0 || rating.compareTo(new BigDecimal("5")) > 0) {
                System.out.println("Рейтинг должен быть в диапазоне 0..5.");
                return BAD_DECIMAL;
            }
            return rating;
        } catch (NumberFormatException e) {
            System.out.println("Неверное число.");
            return BAD_DECIMAL;
        }
    }

    private Integer askOptionalInt(Scanner scanner, String label) {
        String value = ask(scanner, label);
        if (value.isEmpty()) {
            return null;
        }
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            System.out.println("Неверное число.");
            return BAD_INT;
        }
    }

    private Plant askPlant(Scanner scanner) {
        Integer id = askOptionalInt(scanner, "ID растения: ");
        if (id == BAD_INT) {
            return null;
        }
        if (id == null) {
            System.out.println("Нужно указать ID растения.");
            return null;
        }
        Plant plant = plantRepo.findById(id).orElse(null);
        if (plant == null) {
            System.out.println("Растение с id=" + id + " не найдено.");
        }
        return plant;
    }

    private Variety askVariety(Scanner scanner) {
        Integer id = askOptionalInt(scanner, "ID сорта: ");
        if (id == BAD_INT) {
            return null;
        }
        if (id == null) {
            System.out.println("Нужно указать ID сорта.");
            return null;
        }
        Variety variety = varietyRepo.findById(id).orElse(null);
        if (variety == null) {
            System.out.println("Сорт с id=" + id + " не найден.");
        }
        return variety;
    }

    private Gardener askGardener(Scanner scanner) {
        Integer id = askOptionalInt(scanner, "ID садовода: ");
        if (id == BAD_INT) {
            return null;
        }
        if (id == null) {
            System.out.println("Нужно указать ID садовода.");
            return null;
        }
        Gardener gardener = gardenerRepo.findById(id).orElse(null);
        if (gardener == null) {
            System.out.println("Садовод с id=" + id + " не найден.");
        }
        return gardener;
    }

    private Issue askIssue(Scanner scanner) {
        Integer id = askOptionalInt(scanner, "ID проблемы: ");
        if (id == BAD_INT) {
            return null;
        }
        if (id == null) {
            System.out.println("Нужно указать ID проблемы.");
            return null;
        }
        Issue issue = issueRepo.findById(id).orElse(null);
        if (issue == null) {
            System.out.println("Проблема с id=" + id + " не найдена.");
        }
        return issue;
    }

    private void listPlants() {
        System.out.println("Доступные растения:");
        for (Plant plant : plantRepo.findAll()) {
            System.out.printf("     %-4d %s%n", plant.getId(), plant.getName());
        }
    }

    private void listVarieties() {
        System.out.println("Доступные сорта:");
        for (Variety variety : varietyRepo.findAllWithPlant()) {
            System.out.printf("     %-4d %s (%s)%n", variety.getId(), variety.getName(), variety.getPlant().getName());
        }
    }

    private void listGardeners() {
        System.out.println("Доступные садоводы:");
        for (Gardener gardener : gardenerRepo.findAll()) {
            System.out.printf("     %-4d %s%n", gardener.getId(), gardener.getUsername());
        }
    }

    private void listIssues() {
        System.out.println("Доступные проблемы:");
        for (Issue issue : issueRepo.findAll()) {
            System.out.printf("     %-4d %s (%s)%n", issue.getId(), issue.getName(), issue.isPest() ? "вредитель" : "болезнь");
        }
    }

    public static String truncate(String s, int max) {
        if (s == null) {
            return "";
        }
        return s.length() > max ? s.substring(0, max - 1) + "…" : s;
    }

    private static void printHeader(String title) {
        System.out.println();
        System.out.println("╔" + "═".repeat(title.length() + 4) + "╗");
        System.out.println("║  " + title + "  ║");
        System.out.println("╚" + "═".repeat(title.length() + 4) + "╝");
    }

    private static void printDivider() {
        System.out.println("─".repeat(80));
    }
}
