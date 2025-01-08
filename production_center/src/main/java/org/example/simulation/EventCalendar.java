package org.example.simulation;

import org.example.entities.ProductionCenter;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class EventCalendar {
    private List<CalendarNode> list;
    private final int workersQuantity;
    private final int detailsQuantity;
    private double currentTime;
    private int availableWorkers;
    public EventCalendar(int workersQuantity, int detailsQuantity) {
        this.workersQuantity = workersQuantity;
        this.detailsQuantity = detailsQuantity;
        list = new ArrayList<>();
        currentTime = 0.0;
        availableWorkers = workersQuantity;
    }
    public void initialize(ProductionCenter startCenter) {
        for (int i = 0; i < workersQuantity; i++) {
            list.add(new CalendarNode(false, 0.0, false));
        }
        list.add(new CalendarNode(true, 0.0, true));
        int firstCenterMaxWorkers = startCenter.getMaxWorkersCount();
        int assigningWorkersQuantity = Math.min(availableWorkers, firstCenterMaxWorkers);
        List<CalendarNode> workingPeople = list.stream()
                .filter(this::isNodeAvailableWorker).toList();
        for (int i = 0; i < assigningWorkersQuantity; i++) {
            CalendarNode worker = workingPeople.get(i);
            worker.setState(true);
            worker.setTime(currentTime + startCenter.getPerformance());
            worker.setWorkingCenter(startCenter);
        }
        startCenter.setBuffer(detailsQuantity);
        startCenter.setWorkers(assigningWorkersQuantity);
        availableWorkers -= assigningWorkersQuantity;
        startCenter.setBuffer(startCenter.getBuffer() - assigningWorkersQuantity);
    }
    public CalendarNode getNextEvent() {
        CalendarNode minimal = list.stream().filter(CalendarNode::isState)
                .min(Comparator.comparingDouble(CalendarNode::getTime)).get();
        currentTime = minimal.getTime();
        return minimal;
    }
    public void assignWorker(ProductionCenter productionCenter, CalendarNode worker) {
        worker.setState(true);
        worker.setTime(currentTime + productionCenter.getPerformance());
        worker.setWorkingCenter(productionCenter);
        productionCenter.assignWorker();
        availableWorkers--;
    }
    public void releaseWorkerFromCenter(CalendarNode worker) {
        worker.setState(false);
        ProductionCenter center = worker.getWorkingCenter();
        center.produceDetail();
        center.releaseWorker();
        availableWorkers++;
    }
    public void printCalendar() {
        System.out.println(new DecimalFormat("#.##").format(currentTime).replace(",", "."));
        for (int i = 0; i < list.size(); i++) {
            int j = i + 1;
            System.out.println(j + " " + list.get(i));
        }
    }
    public int getAvailableWorkers() {
        return availableWorkers;
    }
    private boolean isNodeAvailableWorker(CalendarNode x) {
        return !x.isTimestampElsePersonRelease() && !x.isState();
    }
    public CalendarNode callAvailableWorker() {
        return list.stream().filter(this::isNodeAvailableWorker).findFirst().get();
    }
    protected void setList(List<CalendarNode> list) {
        this.list = list;
    }
    protected double getCurrentTime() {
        return currentTime;
    }
}
