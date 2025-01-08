package org.example.validator;

import org.example.entities.ProductionCenter;
import org.example.util.ConnectionManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ProductCenterListValidatorTests {
    @Test
    public void test_checkDataSize() {
        List<ProductionCenter> list = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            list.add(new ProductionCenter(String.valueOf(i), "", 3,3));
        }
        ProductCenterListValidator validator = new ProductCenterListValidator(list);
        assertTrue(validator.checkDataSize());
        list.add(new ProductionCenter("z", "",4,5));
        assertFalse(validator.checkDataSize());
    }
    @Test
    public void test_checkUnique() {
        List<ProductionCenter> list = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            list.add(new ProductionCenter(String.valueOf(i), "", 3,3));
        }
        ProductCenterListValidator validator = new ProductCenterListValidator(list);
        assertTrue(validator.checkUnique());
        for (int i = 0; i < 5; i++) {
            list.add(new ProductionCenter(String.valueOf(i), "", 3,3));
        }
        assertFalse(validator.checkUnique());
    }
    @Test
    public void checkFirstAndLast_NotThrow() {
        ProductionCenter a = new ProductionCenter("a", "a", 4, 4);
        ProductionCenter b = new ProductionCenter("b","b", 3,4);
        List<ProductionCenter> list = new ArrayList<>();
        list.add(a);
        list.add(b);
        ConnectionManager connectionManager = new ConnectionManager(list);
        connectionManager.linkCenters(a.getId(), b.getId());
        ProductCenterListValidator validator = new ProductCenterListValidator(list);
        Executable executable = validator::checkFirstAndLastCenter;
        assertDoesNotThrow(executable);
        ProductionCenter c = new ProductionCenter("c", "c", 2,3);
        list.add(c);
        connectionManager.linkCenters(c.getId(), a.getId());
        executable = validator::checkFirstAndLastCenter;
        assertDoesNotThrow(executable);
    }
    @Test
    public void checkFirstAndLast_WithMultipleLast_ThrowsProductCenterDataException() {
        ProductionCenter a = new ProductionCenter("a", "a", 4, 4);
        ProductionCenter b = new ProductionCenter("b","b", 3,4);
        ProductionCenter c = new ProductionCenter("c", "c", 2,3);
        List<ProductionCenter> list = new ArrayList<>();
        list.add(a);
        list.add(b);
        list.add(c);
        ConnectionManager connectionManager = new ConnectionManager(list);
        connectionManager.linkCenters(a.getId(), b.getId());
        connectionManager.linkCenters(a.getId(), c.getId());
        ProductCenterListValidator validator = new ProductCenterListValidator(list);
        Executable executable = validator::checkFirstAndLastCenter;
        assertThrows(ProductCenterDataException.class, executable);
    }
    @Test
    public void checkFirstAndLast_WithMultipleFirst_ThrowsProductCenterDataException() {
        ProductionCenter a = new ProductionCenter("a", "a", 4, 4);
        ProductionCenter b = new ProductionCenter("b","b", 3,4);
        ProductionCenter c = new ProductionCenter("c", "c", 2,3);
        List<ProductionCenter> list = new ArrayList<>();
        list.add(a);
        list.add(b);
        list.add(c);
        ConnectionManager connectionManager = new ConnectionManager(list);
        connectionManager.linkCenters(a.getId(), b.getId());
        connectionManager.linkCenters(c.getId(), b.getId());
        ProductCenterListValidator validator = new ProductCenterListValidator(list);
        Executable executable = validator::checkFirstAndLastCenter;
        assertThrows(ProductCenterDataException.class, executable);
    }
    @Test
    public void checkCycleChains_NotThrow() {
        ProductionCenter a = new ProductionCenter("a", "a", 4, 4);
        ProductionCenter b = new ProductionCenter("b","b", 3,4);
        ProductionCenter c = new ProductionCenter("c", "c", 2,3);
        List<ProductionCenter> list = new ArrayList<>();
        list.add(a);
        list.add(b);
        list.add(c);
        ConnectionManager connectionManager = new ConnectionManager(list);
        connectionManager.linkCenters(a.getId(), b.getId());
        connectionManager.linkCenters(b.getId(), c.getId());
        ProductCenterListValidator validator = new ProductCenterListValidator(list);
        Executable executable = validator::checkCycleChains;
        assertDoesNotThrow(executable);
    }
    @Test
    public void checkCycleChains_WithCycle_ThrowsProductCenterDataException() {
        ProductionCenter a = new ProductionCenter("a", "a", 4, 4);
        ProductionCenter b = new ProductionCenter("b","b", 3,4);
        ProductionCenter c = new ProductionCenter("c", "c", 2,3);
        ProductionCenter d = new ProductionCenter("d","d", 3,4);
        ProductionCenter e = new ProductionCenter("e", "e", 2,3);
        List<ProductionCenter> list = new ArrayList<>();
        list.add(a);
        list.add(b);
        list.add(c);
        list.add(d);
        list.add(e);
        ConnectionManager connectionManager = new ConnectionManager(list);
        ProductCenterListValidator validator = new ProductCenterListValidator(list);
        connectionManager.linkCenters(a.getId(), b.getId());
        connectionManager.linkCenters(b.getId(), c.getId());
        connectionManager.linkCenters(d.getId(), b.getId());
        connectionManager.linkCenters(c.getId(), d.getId());
        connectionManager.linkCenters(d.getId(), e.getId());
        Executable executable = validator::checkCycleChains;
        assertThrows(ProductCenterDataException.class, executable);
    }
    @Test
    public void test_checkNotEmpty() {
        List<ProductionCenter> list = new ArrayList<>();
        ProductCenterListValidator validator = new ProductCenterListValidator(list);
        Executable executable = validator::checkNotEmpty;
        assertThrows(ProductCenterDataException.class, executable);
        list.add(new ProductionCenter("z", "z", 2.3, 54));
        executable = validator::checkNotEmpty;
        assertDoesNotThrow(executable);
    }
}
