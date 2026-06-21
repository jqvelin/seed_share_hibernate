package com.seedshare.service;

import com.seedshare.entity.Plant;
import com.seedshare.repository.PlantRepository;

public class PlantCrudDemoService {

    private static final PlantRepository plantRepo = new PlantRepository();

    public static void run() {
        long suffix = System.currentTimeMillis() % 100000;
        Plant plant = plantRepo.save(new Plant("Кабачок " + suffix));
        System.out.println("Создано растение с id " + plant.getId());

        listAllPlants();

        plant.setName("Кабачок улучшенный " + suffix);
        plantRepo.update(plant);
        System.out.println("Название растения обновлено");

        listAllPlants();

        plantRepo.deleteById(plant.getId());
        System.out.println("Растение удалено");

        listAllPlants();
    }

    private static void listAllPlants() {
        for (Plant plant : plantRepo.findAll()) {
            System.out.println(plant.getId() + ": " + plant.getName());
        }
    }
}
