package com.seedshare.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class PlantIssueId implements Serializable {

    @Column(name = "plant_id")
    private Integer plantId;

    @Column(name = "issue_id")
    private Integer issueId;

    protected PlantIssueId() {}

    public PlantIssueId(Integer plantId, Integer issueId) {
        this.plantId = plantId;
        this.issueId = issueId;
    }

    public Integer getPlantId() { return plantId; }
    public void setPlantId(Integer plantId) { this.plantId = plantId; }
    public Integer getIssueId() { return issueId; }
    public void setIssueId(Integer issueId) { this.issueId = issueId; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PlantIssueId k)) return false;
        return Objects.equals(plantId, k.plantId) && Objects.equals(issueId, k.issueId);
    }

    @Override
    public int hashCode() { return Objects.hash(plantId, issueId); }

    @Override
    public String toString() { return String.format("PlantIssueId{plant=%d, issue=%d}", plantId, issueId); }
}
