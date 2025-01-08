package org.example;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class MainTests {
    @Test
    public void test_checkScenario() {
        assertFalse(Main.checkScenario(-1,3));
        assertFalse(Main.checkScenario(1, -2));
        assertFalse(Main.checkScenario(41, 333));
        assertFalse(Main.checkScenario(34, 2001));
        assertTrue(Main.checkScenario(40, 2000));
    }
    @Test
    public void test_checkPerformance() {
        assertFalse(Main.checkPerformance(-0.1));
        assertFalse(Main.checkPerformance(10.1));
        assertTrue(Main.checkPerformance(10.0));
    }
}
