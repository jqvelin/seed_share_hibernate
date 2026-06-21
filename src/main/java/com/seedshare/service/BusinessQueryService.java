package com.seedshare.service;

import com.seedshare.util.HibernateUtil;
import jakarta.persistence.EntityManager;

import java.math.BigDecimal;
import java.util.List;

public class BusinessQueryService {

    public void topVarieties() {
        printHeader("Топ сортов по рейтингу");
        try (EntityManager em = HibernateUtil.createEntityManager()) {
            List<Object[]> results = em.createQuery("""
                    SELECT v.plant.name, v.name, v.rating
                    FROM Variety v
                    WHERE v.rating IS NOT NULL
                    ORDER BY v.rating DESC, v.name
                    """, Object[].class)
                    .setMaxResults(5)
                    .getResultList();

            System.out.printf("     %-4s %-18s %-26s %-8s%n", "#", "Растение", "Сорт", "Рейтинг");
            System.out.println("     " + "─".repeat(58));
            int rank = 1;
            for (Object[] row : results) {
                System.out.printf("     %-4s %-18s %-26s %-8s%n",
                        "#" + rank, truncate((String) row[0], 17), truncate((String) row[1], 25), row[2]);
                rank++;
            }
        }
        printDivider();
    }

    public void gardenerReliability() {
        printHeader("Надёжность садоводов");
        try (EntityManager em = HibernateUtil.createEntityManager()) {
            List<Object[]> results = em.createQuery("""
                    SELECT g.username, COUNT(DISTINCT e.id), AVG(f.gardenerRating)
                    FROM ExchangeItem ei
                    JOIN ei.seed s
                    JOIN s.gardener g
                    JOIN ei.exchange e
                    JOIN Feedback f ON f.exchange = e
                    WHERE f.gardenerRating IS NOT NULL
                    GROUP BY g.id, g.username
                    ORDER BY AVG(f.gardenerRating) DESC, COUNT(DISTINCT e.id) DESC, g.username
                    """, Object[].class).getResultList();

            System.out.printf("     %-24s %-10s %-10s%n", "Садовод", "Обменов", "Средняя");
            System.out.println("     " + "─".repeat(44));
            for (Object[] row : results) {
                System.out.printf("     %-24s %-10d %-10.2f%n",
                        truncate((String) row[0], 23), (long) row[1], (double) row[2]);
            }
        }
        printDivider();
    }

    public void exchangesByDelivery() {
        printHeader("Обмены по способу доставки");
        try (EntityManager em = HibernateUtil.createEntityManager()) {
            List<Object[]> results = em.createQuery("""
                    SELECT e.deliveryMethod, COUNT(e)
                    FROM Exchange e
                    GROUP BY e.deliveryMethod
                    ORDER BY COUNT(e) DESC, e.deliveryMethod
                    """, Object[].class).getResultList();

            System.out.printf("     %-16s %-10s%n", "Доставка", "Обменов");
            System.out.println("     " + "─".repeat(28));
            for (Object[] row : results) {
                System.out.printf("     %-16s %-10d%n", row[0], (long) row[1]);
            }
        }
        printDivider();
    }

    public void treatmentsForPlant(int plantId) {
        printHeader("Рекомендации по лечению для растения id=" + plantId);
        try (EntityManager em = HibernateUtil.createEntityManager()) {
            List<Object[]> results = em.createQuery("""
                    SELECT pi.plant.name, pi.issue.name, pi.issue.pest, pi.treatment
                    FROM PlantIssue pi
                    WHERE pi.plant.id = :plantId
                    ORDER BY pi.issue.name
                    """, Object[].class)
                    .setParameter("plantId", plantId)
                    .getResultList();

            System.out.printf("     %-24s %-12s %-50s%n", "Проблема", "Тип", "Лечение");
            System.out.println("     " + "─".repeat(88));
            if (results.isEmpty()) {
                System.out.println("     (нет данных)");
            }
            for (Object[] row : results) {
                System.out.printf("     %-24s %-12s %-50s%n",
                        truncate((String) row[1], 23),
                        (boolean) row[2] ? "Вредитель" : "Болезнь",
                        truncate((String) row[3], 49));
            }
        }
        printDivider();
    }

    public void availableSeeds() {
        printHeader("Семена в наличии");
        try (EntityManager em = HibernateUtil.createEntityManager()) {
            List<Object[]> results = em.createQuery("""
                    SELECT s.id, s.variety.plant.name, s.variety.name, s.gardener.username,
                           s.harvestYear, s.packetsCount
                    FROM Seed s
                    WHERE s.packetsCount > 0
                    ORDER BY s.packetsCount DESC, s.variety.plant.name, s.variety.name
                    """, Object[].class).getResultList();

            System.out.printf("     %-5s %-14s %-24s %-24s %-8s %-8s%n",
                    "ID", "Растение", "Сорт", "Садовод", "Год", "Пакеты");
            System.out.println("     " + "─".repeat(85));
            for (Object[] row : results) {
                System.out.printf("     %-5d %-14s %-24s %-24s %-8s %-8d%n",
                        (int) row[0],
                        truncate((String) row[1], 13),
                        truncate((String) row[2], 23),
                        truncate((String) row[3], 23),
                        row[4] != null ? row[4] : "—",
                        (int) row[5]);
            }
        }
        printDivider();
    }

    public void gardenerExchangeHistory(int gardenerId) {
        printHeader("История обменов садовода id=" + gardenerId);
        try (EntityManager em = HibernateUtil.createEntityManager()) {
            List<Object[]> results = em.createQuery("""
                    SELECT g.username, e.id, e.deliveryMethod, e.transferCompleted,
                           s.variety.plant.name, s.variety.name, ei.packetsCount
                    FROM ExchangeItem ei
                    JOIN ei.seed s
                    JOIN s.gardener g
                    JOIN ei.exchange e
                    WHERE g.id = :gardenerId
                    ORDER BY e.id, s.variety.name
                    """, Object[].class)
                    .setParameter("gardenerId", gardenerId)
                    .getResultList();

            System.out.printf("     %-8s %-14s %-12s %-14s %-24s %-8s%n",
                    "Обмен", "Доставка", "Статус", "Растение", "Сорт", "Пакеты");
            System.out.println("     " + "─".repeat(82));
            if (results.isEmpty()) {
                System.out.println("     (нет данных)");
            }
            for (Object[] row : results) {
                System.out.printf("     %-8d %-14s %-12s %-14s %-24s %-8d%n",
                        (int) row[1],
                        row[2],
                        (boolean) row[3] ? "завершён" : "в пути",
                        truncate((String) row[4], 13),
                        truncate((String) row[5], 23),
                        (int) row[6]);
            }
        }
        printDivider();
    }

    public void pendingExchanges() {
        printHeader("Незавершённые обмены");
        try (EntityManager em = HibernateUtil.createEntityManager()) {
            List<Object[]> results = em.createQuery("""
                    SELECT e.id, e.deliveryMethod, COUNT(ei)
                    FROM Exchange e
                    LEFT JOIN ExchangeItem ei ON ei.exchange = e
                    WHERE e.transferCompleted = false
                    GROUP BY e.id, e.deliveryMethod
                    ORDER BY e.id
                    """, Object[].class).getResultList();

            System.out.printf("     %-8s %-16s %-10s%n", "Обмен", "Доставка", "Позиций");
            System.out.println("     " + "─".repeat(36));
            for (Object[] row : results) {
                System.out.printf("     %-8d %-16s %-10d%n", (int) row[0], row[1], (long) row[2]);
            }
        }
        printDivider();
    }

    public void topActiveGardeners() {
        printHeader("Самые активные садоводы");
        try (EntityManager em = HibernateUtil.createEntityManager()) {
            List<Object[]> results = em.createQuery("""
                    SELECT g.username, COUNT(DISTINCT e.id), SUM(ei.packetsCount)
                    FROM ExchangeItem ei
                    JOIN ei.seed s
                    JOIN s.gardener g
                    JOIN ei.exchange e
                    GROUP BY g.id, g.username
                    HAVING COUNT(DISTINCT e.id) >= 2
                    ORDER BY COUNT(DISTINCT e.id) DESC, SUM(ei.packetsCount) DESC, g.username
                    """, Object[].class).getResultList();

            System.out.printf("     %-24s %-10s %-12s%n", "Садовод", "Обменов", "Пакетиков");
            System.out.println("     " + "─".repeat(46));
            for (Object[] row : results) {
                System.out.printf("     %-24s %-10d %-12d%n",
                        truncate((String) row[0], 23), (long) row[1], (long) row[2]);
            }
        }
        printDivider();
    }

    public void runAll() {
        topVarieties();
        gardenerReliability();
        exchangesByDelivery();
        treatmentsForPlant(1);
        availableSeeds();
        gardenerExchangeHistory(1);
        pendingExchanges();
        topActiveGardeners();
    }

    private void printHeader(String title) {
        System.out.println();
        System.out.println("╔" + "═".repeat(title.length() + 4) + "╗");
        System.out.println("║  " + title + "  ║");
        System.out.println("╚" + "═".repeat(title.length() + 4) + "╝");
    }

    private void printDivider() {
        System.out.println("─".repeat(80));
    }

    private static String truncate(String s, int max) {
        if (s == null) {
            return "";
        }
        return s.length() > max ? s.substring(0, max - 1) + "…" : s;
    }
}
