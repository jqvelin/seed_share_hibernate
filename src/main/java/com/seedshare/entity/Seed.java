package com.seedshare.entity;

import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "seed")
public class Seed {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "variety_id", nullable = false)
    private Variety variety;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gardener_id", nullable = false)
    private Gardener gardener;

    @Column(name = "harvest_year")
    private Integer harvestYear;

    @Column
    private String lineage;

    @Column(name = "packets_count", nullable = false)
    private int packetsCount = 1;

    protected Seed() {}

    public Seed(Variety variety, Gardener gardener, Integer harvestYear, String lineage, int packetsCount) {
        this.variety = variety;
        this.gardener = gardener;
        this.harvestYear = harvestYear;
        this.lineage = lineage;
        this.packetsCount = packetsCount;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public Variety getVariety() { return variety; }
    public void setVariety(Variety variety) { this.variety = variety; }
    public Gardener getGardener() { return gardener; }
    public void setGardener(Gardener gardener) { this.gardener = gardener; }
    public Integer getHarvestYear() { return harvestYear; }
    public void setHarvestYear(Integer harvestYear) { this.harvestYear = harvestYear; }
    public String getLineage() { return lineage; }
    public void setLineage(String lineage) { this.lineage = lineage; }
    public int getPacketsCount() { return packetsCount; }
    public void setPacketsCount(int packetsCount) { this.packetsCount = packetsCount; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Seed s)) return false;
        return Objects.equals(id, s.id);
    }

    @Override
    public int hashCode() { return Objects.hashCode(id); }

    @Override
    public String toString() {
        return String.format("Seed{id=%d, harvestYear=%s, packets=%d}", id, harvestYear, packetsCount);
    }
}
