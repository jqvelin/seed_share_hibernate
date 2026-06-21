package com.seedshare.entity;

import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "issue")
public class Issue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "is_pest", nullable = false)
    private boolean pest;

    @Column(nullable = false, unique = true)
    private String name;

    @Column
    private String description;

    protected Issue() {}

    public Issue(boolean pest, String name, String description) {
        this.pest = pest;
        this.name = name;
        this.description = description;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public boolean isPest() { return pest; }
    public void setPest(boolean pest) { this.pest = pest; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Issue i)) return false;
        return Objects.equals(name, i.name);
    }

    @Override
    public int hashCode() { return Objects.hashCode(name); }

    @Override
    public String toString() {
        return String.format("Issue{id=%d, '%s', %s}", id, name, pest ? "вредитель" : "болезнь");
    }
}
