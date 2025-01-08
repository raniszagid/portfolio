package org.example.util;

import org.example.entities.ProductionCenter;
import org.example.validator.ProductCenterDataException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class ConnectionManagerTests {
    @Test
    public void linkCenters_SelfConnection_ThrowsProductCenterDataException() {
        String id = "id";
        List<ProductionCenter> list = new ArrayList<>();
        ConnectionManager connectionManager = new ConnectionManager(list);
        Executable executable = () -> connectionManager.linkCenters(id, id);
        assertThrows(ProductCenterDataException.class, executable);
    }
    @Test
    public void linkCenters_NonExistentElement_ThrowsProductCenterDataException() {
        String existentId = "id1";
        List<ProductionCenter> list = List.of(new ProductionCenter(existentId,"1",2,2));
        String nonExistentId = "z";
        ConnectionManager connectionManager = new ConnectionManager(list);
        Executable executable = () -> connectionManager.linkCenters(existentId, nonExistentId);
        assertThrows(ProductCenterDataException.class, executable);
        executable = () -> connectionManager.linkCenters(nonExistentId, existentId);
        assertThrows(ProductCenterDataException.class, executable);
    }
}
