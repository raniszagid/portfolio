package org.example.util;

import org.example.entities.ProductionCenter;
import org.example.validator.ProductCenterDataException;

import java.util.List;
import java.util.Objects;

public class ConnectionManager {
    private final List<ProductionCenter> list;
    public ConnectionManager(List<ProductionCenter> list) {
        this.list = list;
    }
    public void linkCenters(String sourceCenter, String destinationCenter) {
        if (sourceCenter.equals(destinationCenter))
            throw new ProductCenterDataException(String.format("ПЦ \"%s\" связан сам с собой", sourceCenter));
        ProductionCenter source = list.stream().filter(c -> Objects.equals(c.getId(), sourceCenter)).findFirst()
                .orElseThrow(() -> new ProductCenterDataException(String.format(
                        "Не существует производственного центра с именем \"%s\"", sourceCenter)));
        ProductionCenter destination = list.stream().filter(c -> Objects.equals(c.getId(), destinationCenter))
                .findFirst().orElseThrow(() -> new ProductCenterDataException(String.format(
                        "Не существует производственного центра с именем \"%s\"", destinationCenter)));
        source.input();
        source.addChild(destination);
        destination.output();
    }
}
