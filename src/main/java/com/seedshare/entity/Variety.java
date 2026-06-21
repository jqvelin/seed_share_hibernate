package com.seedshare.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "variety")
public class Variety {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plant_id", nullable = false)
    private Plant plant;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(name = "growing_conditions", length = 100)
    private String growingConditions;

    @Column(precision = 3, scale = 2)
    private BigDecimal rating;

    protected Variety() {}

    public Variety(Plant plant, String name, String growingConditions, BigDecimal rating) {
        this.plant = plant;
        this.name = name;
        this.growingConditions = growingConditions;
        this.rating = rating;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public Plant getPlant() { return plant; }
    public void setPlant(Plant plant) { this.plant = plant; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getGrowingConditions() { return growingConditions; }
    public void setGrowingConditions(String growingConditions) { this.growingConditions = growingConditions; }
    public BigDecimal getRating() { return rating; }
    public void setRating(BigDecimal rating) { this.rating = rating; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Variety v)) return false;
        return Objects.equals(id, v.id);
    }

    @Override
    public int hashCode() { return Objects.hashCode(id); }

    @Override
    public String toString() {
        return String.format("Variety{id=%d, '%s', rating=%s}", id, name, rating);
    }
}
