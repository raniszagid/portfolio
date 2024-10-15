package com.example.carservice.model;

public class MasterResult {
    private String masterName;
    private int quantity;

    public MasterResult() {
    }

    public MasterResult(String masterName, int quantity) {
        this.masterName = masterName;
        this.quantity = quantity;
    }

    public String getMasterName() {
        return masterName;
    }

    public void setMasterName(String masterName) {
        this.masterName = masterName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
