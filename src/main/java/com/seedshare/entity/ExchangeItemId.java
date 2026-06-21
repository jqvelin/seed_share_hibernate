package com.seedshare.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class ExchangeItemId implements Serializable {

    @Column(name = "exchange_id")
    private Integer exchangeId;

    @Column(name = "seed_id")
    private Integer seedId;

    protected ExchangeItemId() {}

    public ExchangeItemId(Integer exchangeId, Integer seedId) {
        this.exchangeId = exchangeId;
        this.seedId = seedId;
    }

    public Integer getExchangeId() { return exchangeId; }
    public void setExchangeId(Integer exchangeId) { this.exchangeId = exchangeId; }
    public Integer getSeedId() { return seedId; }
    public void setSeedId(Integer seedId) { this.seedId = seedId; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ExchangeItemId k)) return false;
        return Objects.equals(exchangeId, k.exchangeId) && Objects.equals(seedId, k.seedId);
    }

    @Override
    public int hashCode() { return Objects.hash(exchangeId, seedId); }

    @Override
    public String toString() { return String.format("ExchangeItemId{exchange=%d, seed=%d}", exchangeId, seedId); }
}
