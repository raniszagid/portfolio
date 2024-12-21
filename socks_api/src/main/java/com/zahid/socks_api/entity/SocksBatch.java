package com.zahid.socks_api.entity;

import jakarta.persistence.*;
@Entity
@Table(name = "socks_batch")
public class SocksBatch {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "color")
    private String color;
    @Column(name = "cotton")
    private int cotton;
    @Column(name = "quantity")
    private int quantity;
    public SocksBatch() {}

    public SocksBatch(int id, String color, int cotton, int quantity) {
        this.id = id;
        this.color = color;
        this.cotton = cotton;
        this.quantity = quantity;
    }

    public SocksBatch(String color, int cotton, int quantity) {
        this.color = color;
        this.cotton = cotton;
        this.quantity = quantity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getCotton() {
        return cotton;
    }

    public void setCotton(int cotton) {
        this.cotton = cotton;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
