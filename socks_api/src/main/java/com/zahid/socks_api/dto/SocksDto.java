package com.zahid.socks_api.dto;

public class SocksDto {
    private String color;
    private int cotton;
    private int quantity;

    public SocksDto() {}

    public SocksDto(String color, int cotton, int quantity) {
        this.color = color;
        this.cotton = cotton;
        this.quantity = quantity;
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
