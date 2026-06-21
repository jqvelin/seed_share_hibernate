package com.seedshare.repository;

import com.seedshare.entity.Exchange;
import com.seedshare.entity.ExchangeItem;
import com.seedshare.entity.ExchangeItemId;
import com.seedshare.entity.Seed;
import com.seedshare.util.HibernateUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import java.util.List;

public class ExchangeItemRepository extends GenericRepository<ExchangeItem, ExchangeItemId> {

    public ExchangeItemRepository() { super(ExchangeItem.class); }

    public List<ExchangeItem> findByExchange(int exchangeId) {
        try (EntityManager em = HibernateUtil.createEntityManager()) {
            return em.createQuery(
                    "FROM ExchangeItem ei WHERE ei.exchange.id = :id ORDER BY ei.seed.id", ExchangeItem.class)
                    .setParameter("id", exchangeId)
                    .getResultList();
        }
    }

    public int registerExchange(String deliveryMethod, int firstSeedId, int firstPackets,
                                int secondSeedId, int secondPackets) {
        EntityManager em = HibernateUtil.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();

            Seed first = em.find(Seed.class, firstSeedId);
            Seed second = em.find(Seed.class, secondSeedId);
            if (first == null || second == null) {
                throw new IllegalArgumentException("Семена не найдены");
            }
            if (first.getPacketsCount() < firstPackets) {
                throw new IllegalStateException("Недостаточно пакетиков у семян id=" + firstSeedId);
            }
            if (second.getPacketsCount() < secondPackets) {
                throw new IllegalStateException("Недостаточно пакетиков у семян id=" + secondSeedId);
            }

            Exchange exchange = new Exchange(false, deliveryMethod);
            em.persist(exchange);
            em.flush();

            em.persist(new ExchangeItem(exchange, first, firstPackets));
            em.persist(new ExchangeItem(exchange, second, secondPackets));

            first.setPacketsCount(first.getPacketsCount() - firstPackets);
            second.setPacketsCount(second.getPacketsCount() - secondPackets);

            tx.commit();
            return exchange.getId();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    public void deleteExchangeWithRestock(int exchangeId) {
        EntityManager em = HibernateUtil.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();

            List<ExchangeItem> items = em.createQuery(
                    "FROM ExchangeItem ei WHERE ei.exchange.id = :id", ExchangeItem.class)
                    .setParameter("id", exchangeId)
                    .getResultList();

            for (ExchangeItem item : items) {
                Seed seed = item.getSeed();
                seed.setPacketsCount(seed.getPacketsCount() + item.getPacketsCount());
                em.remove(item);
            }

            Exchange exchange = em.find(Exchange.class, exchangeId);
            if (exchange != null) {
                em.remove(exchange);
            }

            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }
}
