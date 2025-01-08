package org.example.entities;

import org.example.validator.ProductCenterDataException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ProductionCenter {
    private final String id;
    private final String name;
    private final double performance;
    private final int maxWorkersCount;
    private int workers;
    private int buffer;
    private int input;
    private int output;
    private int producedDetails;
    private List<ProductionCenter> children;
    public ProductionCenter(String id, String name, double performance, int maxWorkersCount) {
        this.id = id;
        this.name = name;
        this.performance = performance;
        this.maxWorkersCount = maxWorkersCount;
        children = new ArrayList<>();
    }
    public String getId() {
        return id;
    }
    public double getPerformance() {
        return performance;
    }
    public int getMaxWorkersCount() {
        return maxWorkersCount;
    }
    public int getWorkers() {
        return workers;
    }

    public void setWorkers(int workers) {
        this.workers = workers;
    }

    public int getBuffer() {
        return buffer;
    }

    public void setBuffer(int buffer) {
        this.buffer = buffer;
    }

    public List<ProductionCenter> getChildren() {
        return children;
    }
    public void addChild(ProductionCenter child) {
        children.add(child);
    }

    public int getInput() {
        return input;
    }

    public int getOutput() {
        return output;
    }

    public void input() {
        this.input++;
    }
    public void output() {
        this.output++;
    }
    public int getProducedDetails() {
        return producedDetails;
    }
    public void produceDetail() {
        producedDetails++;
    }
    public void takeDetail() {
        buffer++;
    }
    public void assignWorker() {
        workers++;
        buffer--;
    }
    public void releaseWorker() {
        workers--;
    }
    @Override
    public String toString() {
        return "{" +
                "id='" + id + '\'' +
                ", input=" + input +
                ", output=" + output +
                '}';
    }
}
