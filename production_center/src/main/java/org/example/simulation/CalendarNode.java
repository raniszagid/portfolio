package org.example.simulation;

import org.example.entities.ProductionCenter;

import java.text.DecimalFormat;

public class CalendarNode {
    private final boolean timestampElsePersonRelease;
    private double time;
    private boolean state;
    private ProductionCenter workingCenter;
    public CalendarNode(boolean timestampElsePersonRelease, double time, boolean state) {
        this.timestampElsePersonRelease = timestampElsePersonRelease;
        this.time = time;
        this.state = state;
    }
    public boolean isTimestampElsePersonRelease() {
        return timestampElsePersonRelease;
    }
    public double getTime() {
        return time;
    }
    public void setTime(double time) {
        this.time = time;
    }
    public void changeTime(double t) {
        time += t;
    }
    public boolean isState() {
        return state;
    }
    public void setState(boolean state) {
        this.state = state;
    }
    public ProductionCenter getWorkingCenter() {
        return workingCenter;
    }
    public void setWorkingCenter(ProductionCenter workingCenter) {
        this.workingCenter = workingCenter;
    }
    private String writeWorkerOrTimestamp() {
        return timestampElsePersonRelease ? "Timestamp" : "Worker";
    }
    @Override
    public String toString() {
        return writeWorkerOrTimestamp() + ", " +
                new DecimalFormat("#.##").format(time).replace(",", ".")
                + " " + state +
                ((state && !timestampElsePersonRelease) ? ", " + workingCenter.getId().charAt(24) : "");
    }
}
