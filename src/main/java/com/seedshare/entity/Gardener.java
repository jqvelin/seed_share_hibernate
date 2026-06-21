package com.seedshare.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "gardener")
public class Gardener {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Column(precision = 3, scale = 2)
    private BigDecimal rating;

    protected Gardener() {}

    public Gardener(String username, BigDecimal rating) {
        this.username = username;
        this.rating = rating;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public BigDecimal getRating() { return rating; }
    public void setRating(BigDecimal rating) { this.rating = rating; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Gardener g)) return false;
        return Objects.equals(username, g.username);
    }

    @Override
    public int hashCode() { return Objects.hashCode(username); }

    @Override
    public String toString() {
        return String.format("Gardener{id=%d, '%s', rating=%s}", id, username, rating);
    }
}
