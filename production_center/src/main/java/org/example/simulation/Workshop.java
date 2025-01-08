package org.example.simulation;

import org.example.entities.OutputEntity;
import org.example.entities.ProductionCenter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Workshop {
    private final int workersCount;
    private final int detailsCount;
    private final List<ProductionCenter> centers;
    private List<OutputEntity> protocol;

    public Workshop(int workersCount, int detailsCount, List<ProductionCenter> centers) {
        this.workersCount = workersCount;
        this.detailsCount = detailsCount;
        this.centers = centers;
        protocol = new ArrayList<>();
    }
    public void run() {
        int releasedQuantity = 0;
        EventCalendar eventCalendar = new EventCalendar(workersCount, detailsCount);
        eventCalendar.initialize(getFirst());
        while (releasedQuantity < detailsCount) {
            CalendarNode currentEvent = eventCalendar.getNextEvent();
            if (currentEvent.isTimestampElsePersonRelease()) {
                takeReadings(currentEvent.getTime());
                currentEvent.changeTime(1);
            }
            else {
                ProductionCenter currentCenter = currentEvent.getWorkingCenter();
                eventCalendar.releaseWorkerFromCenter(currentEvent);
                if (isLast(currentCenter)) {
                    releasedQuantity++;
                }
                else {
                    int number = currentCenter.getProducedDetails();
                    ProductionCenter destination = currentCenter.getChildren().get((number - 1) % currentCenter.getInput());
                    destination.takeDetail();
                }
                if (currentCenter.getBuffer() > 0) {
                    if (currentCenter.getWorkers() > 0 &&
                    currentCenter.getChildren().stream().anyMatch(c -> c.getBuffer() > currentCenter.getBuffer())) {
                        eventCalendar.assignWorker(getNewWorkPlace(), currentEvent);
                    } else {
                        eventCalendar.assignWorker(currentCenter, currentEvent);
                    }
                }
                else {
                    if (isAvailableCenterExist()) {
                        eventCalendar.assignWorker(getNewWorkPlace(), currentEvent);
                    }
                }
                if (checkIdle(eventCalendar.getAvailableWorkers())) {
                    CalendarNode worker = eventCalendar.callAvailableWorker();
                    eventCalendar.assignWorker(getNewWorkPlace(), worker);
                }
                //printByStep(eventCalendar);
            }
        }
        takeReadings(eventCalendar.getNextEvent().getTime());
    }
    protected ProductionCenter getFirst() {
        return centers.stream().filter(c -> c.getOutput() == 0).findAny().get();
    }
    protected boolean isLast(ProductionCenter center) {
        return center.getInput() == 0;
    }
    private void takeReadings(double time) {
        for (ProductionCenter prod : centers) {
            protocol.add(new OutputEntity(time, prod.getId(), prod.getWorkers(), prod.getBuffer()));
        }
    }
    public List<OutputEntity> getResult() {
        return protocol;
    }
    protected ProductionCenter getNewWorkPlace() {
        return centers.stream().filter(this::getCentersToWork)
                .max(Comparator.comparing(ProductionCenter::getBuffer)).get();
    }
    protected boolean checkIdle(int availableWorkers) {
        if (availableWorkers == 0) return false;
        return isAvailableCenterExist();
    }
    private boolean isAvailableCenterExist() {
        return centers.stream().anyMatch(this::getCentersToWork);
    }
    protected boolean getCentersToWork(ProductionCenter center) {
        return center.getBuffer() > 0 && center.getWorkers() < center.getMaxWorkersCount();
    }
    private void printByStep(EventCalendar eventCalendar) {
        eventCalendar.printCalendar();
        System.out.println("***************");
        for (ProductionCenter c : centers) {
            System.out.println(c.getId() + " " + c.getWorkers() + " " + c.getBuffer());
        }
        System.out.println();
    }
}
