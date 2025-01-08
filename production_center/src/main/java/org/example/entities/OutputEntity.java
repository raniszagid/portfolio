package org.example.entities;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class OutputEntity {
    private final double time;
    private final String productionCenter;
    private final int workersCount;
    private final int bufferCount;

    public OutputEntity(double time, String productionCenter, int workersCount, int bufferCount) {
        this.time = time;
        this.productionCenter = productionCenter;
        this.workersCount = workersCount;
        this.bufferCount = bufferCount;
    }
    private String writeTime() {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
        DecimalFormat decimalFormat = new DecimalFormat("0." + "0".repeat(1), symbols);
        return decimalFormat.format(time);
    }

    @Override
    public String toString() {
        String delimiter = ", ";
        return writeTime() + delimiter + productionCenter + delimiter + workersCount + delimiter + bufferCount;
    }
}
