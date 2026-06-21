package com.seedshare.util;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Statement;
import java.util.stream.Collectors;

public class DatabaseResetUtil {

    private static final Logger log = LoggerFactory.getLogger(DatabaseResetUtil.class);

    private DatabaseResetUtil() {}

    public static void reset() {
        log.info("Сброс БД к начальному состоянию...");
        String ddl = readSqlFile("ddl.sql");
        String dml = readSqlFile("dml.sql");

        EntityManager em = HibernateUtil.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.unwrap(Session.class).doWork(connection -> {
                try (Statement stmt = connection.createStatement()) {
                    stmt.execute("DROP SCHEMA IF EXISTS seed_share CASCADE");
                    stmt.execute(ddl);
                    stmt.execute(dml);
                }
            });
            tx.commit();
            log.info("БД сброшена и заполнена исходными данными");
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    private static String readSqlFile(String fileName) {
        try (InputStream is = DatabaseResetUtil.class.getClassLoader().getResourceAsStream(fileName)) {
            if (is == null) {
                throw new RuntimeException("SQL-файл не найден: " + fileName);
            }
            return new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))
                    .lines()
                    .collect(Collectors.joining("\n"));
        } catch (Exception e) {
            throw new RuntimeException("Ошибка чтения SQL-файла: " + fileName, e);
        }
    }
}
