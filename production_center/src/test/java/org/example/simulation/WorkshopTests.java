package org.example.simulation;

import org.example.entities.ProductionCenter;
import org.example.util.ConnectionManager;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class WorkshopTests {
    @Test
    public void test_getCentersToWork() {
        Workshop workshop = new Workshop(1,1,new ArrayList<>());
        ProductionCenter a = new ProductionCenter("a", "a",7.8, 7);
        a.setBuffer(0);
        assertFalse(workshop.getCentersToWork(a));
        a.setBuffer(1);
        a.setWorkers(7);
        assertFalse(workshop.getCentersToWork(a));
        a.setWorkers(6);
        assertTrue(workshop.getCentersToWork(a));
    }
    @Test
    public void test_checkIdle() {
        List<ProductionCenter> list = new ArrayList<>();
        Workshop workshop = new Workshop(4, 255, list);
        ProductionCenter a = new ProductionCenter("a", "a",7.8, 7);
        a.setBuffer(0);
        list.add(a);
        ProductionCenter b = new ProductionCenter("b", "b", 4.5, 5);
        b.setWorkers(5);
        list.add(b);
        assertFalse(workshop.checkIdle(0));
        assertFalse(workshop.checkIdle(1));
        ProductionCenter q = new ProductionCenter("q", "q", 3.4, 5);
        q.setBuffer(2);
        list.add(q);
        assertTrue(workshop.checkIdle(3));
    }
    @Test
    public void test_getNewWorkPlace() {
        int n = 7;
        ProductionCenter a = new ProductionCenter("a", "a",7.8, 7);
        a.setBuffer(n);
        ProductionCenter b = new ProductionCenter("b", "b", 4.5, 5);
        b.setBuffer(n + 2);
        List<ProductionCenter> list = List.of(a, b);
        Workshop workshop = new Workshop(2,22, list);
        ProductionCenter maxBufferProductionCenter = workshop.getNewWorkPlace();
        assertNotEquals(maxBufferProductionCenter, a);
        assertEquals(maxBufferProductionCenter, b);
    }
    @Test
    public void test_isLast() {
        ProductionCenter a = new ProductionCenter("a", "a",7.8, 7);
        ProductionCenter b = new ProductionCenter("b", "b", 4.5, 5);
        List<ProductionCenter> list = List.of(a, b);
        ConnectionManager connectionManager = new ConnectionManager(list);
        connectionManager.linkCenters(a.getId(), b.getId());
        Workshop workshop = new Workshop(3, 33, list);
        assertFalse(workshop.isLast(a));
        assertTrue(workshop.isLast(b));
    }
    @Test
    public void test_getFirst() {
        ProductionCenter a = new ProductionCenter("a", "a",7.8, 7);
        ProductionCenter b = new ProductionCenter("b", "b", 4.5, 5);
        List<ProductionCenter> list = List.of(a, b);
        ConnectionManager connectionManager = new ConnectionManager(list);
        connectionManager.linkCenters(a.getId(), b.getId());
        Workshop workshop = new Workshop(3, 33, list);
        assertEquals(a, workshop.getFirst());
        assertNotEquals(b, workshop.getFirst());
    }
}
