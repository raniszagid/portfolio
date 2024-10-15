package com.example.carservice.model;

public class Service {
    private int id;
    private String name;
    private float costOur;
    private float costForeign;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getCostOur() {
        return costOur;
    }

    public void setCostOur(float costOur) {
        this.costOur = costOur;
    }

    public float getCostForeign() {
        return costForeign;
    }

    public void setCostForeign(float costForeign) {
        this.costForeign = costForeign;
    }
}
