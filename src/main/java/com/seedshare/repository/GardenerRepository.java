package com.seedshare.repository;

import com.seedshare.entity.Gardener;
import com.seedshare.util.HibernateUtil;
import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.Optional;

public class GardenerRepository extends GenericRepository<Gardener, Integer> {

    public GardenerRepository() { super(Gardener.class); }

    public Optional<Gardener> findByUsername(String username) {
        try (EntityManager em = HibernateUtil.createEntityManager()) {
            List<Gardener> result = em.createQuery(
                    "FROM Gardener g WHERE g.username = :username", Gardener.class)
                    .setParameter("username", username)
                    .getResultList();
            return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
        }
    }
}
