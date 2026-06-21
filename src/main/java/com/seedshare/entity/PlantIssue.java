package com.seedshare.entity;

import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "plant_issue")
public class PlantIssue {

    @EmbeddedId
    private PlantIssueId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("plantId")
    @JoinColumn(name = "plant_id")
    private Plant plant;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("issueId")
    @JoinColumn(name = "issue_id")
    private Issue issue;

    @Column
    private String treatment;

    protected PlantIssue() {}

    public PlantIssue(Plant plant, Issue issue, String treatment) {
        this.id = new PlantIssueId(plant.getId(), issue.getId());
        this.plant = plant;
        this.issue = issue;
        this.treatment = treatment;
    }

    public PlantIssueId getId() { return id; }
    public void setId(PlantIssueId id) { this.id = id; }
    public Plant getPlant() { return plant; }
    public void setPlant(Plant plant) { this.plant = plant; }
    public Issue getIssue() { return issue; }
    public void setIssue(Issue issue) { this.issue = issue; }
    public String getTreatment() { return treatment; }
    public void setTreatment(String treatment) { this.treatment = treatment; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PlantIssue pi)) return false;
        return Objects.equals(id, pi.id);
    }

    @Override
    public int hashCode() { return Objects.hashCode(id); }

    @Override
    public String toString() {
        return String.format("PlantIssue{%s, treatment='%s'}", id, treatment);
    }
}
