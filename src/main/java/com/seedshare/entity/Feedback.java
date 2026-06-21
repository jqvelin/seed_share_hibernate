package com.seedshare.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "feedback")
public class Feedback {

    @EmbeddedId
    private FeedbackId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("exchangeId")
    @JoinColumn(name = "exchange_id")
    private Exchange exchange;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("gardenerId")
    @JoinColumn(name = "gardener_id")
    private Gardener gardener;

    @Column
    private String comment;

    @Column(name = "photo_url")
    private String photoUrl;

    @Column(name = "gardener_rating", precision = 3, scale = 2)
    private BigDecimal gardenerRating;

    @Column(name = "plant_rating", precision = 3, scale = 2)
    private BigDecimal plantRating;

    protected Feedback() {}

    public Feedback(Exchange exchange, Gardener gardener, String comment, String photoUrl,
                    BigDecimal gardenerRating, BigDecimal plantRating) {
        this.id = new FeedbackId(exchange.getId(), gardener.getId());
        this.exchange = exchange;
        this.gardener = gardener;
        this.comment = comment;
        this.photoUrl = photoUrl;
        this.gardenerRating = gardenerRating;
        this.plantRating = plantRating;
    }

    public FeedbackId getId() { return id; }
    public void setId(FeedbackId id) { this.id = id; }
    public Exchange getExchange() { return exchange; }
    public void setExchange(Exchange exchange) { this.exchange = exchange; }
    public Gardener getGardener() { return gardener; }
    public void setGardener(Gardener gardener) { this.gardener = gardener; }
    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }
    public String getPhotoUrl() { return photoUrl; }
    public void setPhotoUrl(String photoUrl) { this.photoUrl = photoUrl; }
    public BigDecimal getGardenerRating() { return gardenerRating; }
    public void setGardenerRating(BigDecimal gardenerRating) { this.gardenerRating = gardenerRating; }
    public BigDecimal getPlantRating() { return plantRating; }
    public void setPlantRating(BigDecimal plantRating) { this.plantRating = plantRating; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Feedback f)) return false;
        return Objects.equals(id, f.id);
    }

    @Override
    public int hashCode() { return Objects.hashCode(id); }

    @Override
    public String toString() {
        return String.format("Feedback{%s, gardenerRating=%s, plantRating=%s}", id, gardenerRating, plantRating);
    }
}
