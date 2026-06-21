package com.seedshare.repository;

import com.seedshare.entity.Seed;

public class SeedRepository extends GenericRepository<Seed, Integer> {

    public SeedRepository() { super(Seed.class); }
}
