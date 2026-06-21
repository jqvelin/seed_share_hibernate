package com.seedshare.repository;

import com.seedshare.entity.Plant;
import com.seedshare.util.HibernateUtil;
import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.Optional;

public class PlantRepository extends GenericRepository<Plant, Integer> {

    public PlantRepository() { super(Plant.class); }

    public Optional<Plant> findByName(String name) {
        try (EntityManager em = HibernateUtil.createEntityManager()) {
            List<Plant> result = em.createQuery(
                    "FROM Plant p WHERE p.name = :name", Plant.class)
                    .setParameter("name", name)
                    .getResultList();
            return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
        }
    }
}
