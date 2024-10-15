package com.example.carservice.model;

public class CostResult {
    private float foreign;
    private float russian;
    private float sum;

    public CostResult() {
    }

    public CostResult(float foreign, float russian, float sum) {
        this.foreign = foreign;
        this.russian = russian;
        this.sum = sum;
    }

    public float getForeign() {
        return foreign;
    }

    public void setForeign(float foreign) {
        this.foreign = foreign;
    }

    public float getRussian() {
        return russian;
    }

    public void setRussian(float russian) {
        this.russian = russian;
    }

    public float getSum() {
        return sum;
    }

    public void setSum(float sum) {
        this.sum = sum;
    }
}
