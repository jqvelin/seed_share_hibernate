package com.seedshare.repository;

import com.seedshare.entity.PlantIssue;
import com.seedshare.entity.PlantIssueId;
import com.seedshare.util.HibernateUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import java.util.List;
import java.util.Optional;

public class PlantIssueRepository extends GenericRepository<PlantIssue, PlantIssueId> {

    public PlantIssueRepository() { super(PlantIssue.class); }

    public Optional<PlantIssue> findByKey(int plantId, int issueId) {
        try (EntityManager em = HibernateUtil.createEntityManager()) {
            return Optional.ofNullable(em.find(PlantIssue.class, new PlantIssueId(plantId, issueId)));
        }
    }

    public int batchInsert(List<PlantIssue> items) {
        EntityManager em = HibernateUtil.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            for (int i = 0; i < items.size(); i++) {
                em.persist(items.get(i));
                if (i > 0 && i % 25 == 0) {
                    em.flush();
                    em.clear();
                }
            }
            tx.commit();
            return items.size();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }
}
