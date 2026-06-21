package com.seedshare.repository;

import com.seedshare.entity.Variety;
import com.seedshare.util.HibernateUtil;
import jakarta.persistence.EntityManager;

import java.util.List;

public class VarietyRepository extends GenericRepository<Variety, Integer> {

    public VarietyRepository() { super(Variety.class); }

    public List<Variety> findAllWithPlant() {
        try (EntityManager em = HibernateUtil.createEntityManager()) {
            return em.createQuery(
                    "SELECT v FROM Variety v JOIN FETCH v.plant ORDER BY v.id", Variety.class)
                    .getResultList();
        }
    }
}
