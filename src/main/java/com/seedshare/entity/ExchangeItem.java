package com.seedshare.entity;

import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "exchange_item")
public class ExchangeItem {

    @EmbeddedId
    private ExchangeItemId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("exchangeId")
    @JoinColumn(name = "exchange_id")
    private Exchange exchange;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("seedId")
    @JoinColumn(name = "seed_id")
    private Seed seed;

    @Column(name = "packets_count", nullable = false)
    private int packetsCount;

    protected ExchangeItem() {}

    public ExchangeItem(Exchange exchange, Seed seed, int packetsCount) {
        this.id = new ExchangeItemId(exchange.getId(), seed.getId());
        this.exchange = exchange;
        this.seed = seed;
        this.packetsCount = packetsCount;
    }

    public ExchangeItemId getId() { return id; }
    public void setId(ExchangeItemId id) { this.id = id; }
    public Exchange getExchange() { return exchange; }
    public void setExchange(Exchange exchange) { this.exchange = exchange; }
    public Seed getSeed() { return seed; }
    public void setSeed(Seed seed) { this.seed = seed; }
    public int getPacketsCount() { return packetsCount; }
    public void setPacketsCount(int packetsCount) { this.packetsCount = packetsCount; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ExchangeItem ei)) return false;
        return Objects.equals(id, ei.id);
    }

    @Override
    public int hashCode() { return Objects.hashCode(id); }

    @Override
    public String toString() {
        return String.format("ExchangeItem{%s, packets=%d}", id, packetsCount);
    }
}
