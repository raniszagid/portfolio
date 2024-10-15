package com.zahid.indexes;

import org.junit.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.List;


public class PortTest {
    @Test
    public void testConvertArrayToNumberSequence() {
        Port port = new Port(new String[]{"1,3-5", "2", "3-4"});
        List<List<Integer>> currentResult = port.convertArrayToNumberSequence();
        List<List<Integer>> expectedResult = Arrays.asList(Arrays.asList(1,3,4,5),Arrays.asList(2),
                Arrays.asList(3,4));
        assertEquals(currentResult, expectedResult);
    }

    @Test
    public void testGetUniqueGroups() {
        Port port = new Port(new String[]{"1,3-5", "2", "3-4"});
        List<List<Integer>> currentResult = port.getUniqueGroups();
        List<List<Integer>> expectedResult = Arrays.asList(Arrays.asList(1,2,3),
                Arrays.asList(1,2,4),
                Arrays.asList(3,2,3),
                Arrays.asList(3,2,4),
                Arrays.asList(4,2,3),
                Arrays.asList(4,2,4),
                Arrays.asList(5,2,3),
                Arrays.asList(5,2,4));
        assertEquals(currentResult, expectedResult);
    }
}
