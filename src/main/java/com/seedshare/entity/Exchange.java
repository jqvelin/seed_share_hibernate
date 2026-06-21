package com.seedshare.entity;

import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "exchange")
public class Exchange {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "transfer_completed", nullable = false)
    private boolean transferCompleted = false;

    @Column(name = "delivery_method")
    private String deliveryMethod;

    protected Exchange() {}

    public Exchange(boolean transferCompleted, String deliveryMethod) {
        this.transferCompleted = transferCompleted;
        this.deliveryMethod = deliveryMethod;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public boolean isTransferCompleted() { return transferCompleted; }
    public void setTransferCompleted(boolean transferCompleted) { this.transferCompleted = transferCompleted; }
    public String getDeliveryMethod() { return deliveryMethod; }
    public void setDeliveryMethod(String deliveryMethod) { this.deliveryMethod = deliveryMethod; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Exchange e)) return false;
        return Objects.equals(id, e.id);
    }

    @Override
    public int hashCode() { return Objects.hashCode(id); }

    @Override
    public String toString() {
        return String.format("Exchange{id=%d, completed=%b, delivery=%s}", id, transferCompleted, deliveryMethod);
    }
}
