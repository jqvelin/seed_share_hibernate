package com.seedshare.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class FeedbackId implements Serializable {

    @Column(name = "exchange_id")
    private Integer exchangeId;

    @Column(name = "gardener_id")
    private Integer gardenerId;

    protected FeedbackId() {}

    public FeedbackId(Integer exchangeId, Integer gardenerId) {
        this.exchangeId = exchangeId;
        this.gardenerId = gardenerId;
    }

    public Integer getExchangeId() { return exchangeId; }
    public void setExchangeId(Integer exchangeId) { this.exchangeId = exchangeId; }
    public Integer getGardenerId() { return gardenerId; }
    public void setGardenerId(Integer gardenerId) { this.gardenerId = gardenerId; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FeedbackId k)) return false;
        return Objects.equals(exchangeId, k.exchangeId) && Objects.equals(gardenerId, k.gardenerId);
    }

    @Override
    public int hashCode() { return Objects.hash(exchangeId, gardenerId); }

    @Override
    public String toString() { return String.format("FeedbackId{exchange=%d, gardener=%d}", exchangeId, gardenerId); }
}
