package org.example.simulation;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EventCalendarTests {
    @Test
    public void test_getNextEvent() {
        CalendarNode a = new CalendarNode(false, 1.0, false);
        CalendarNode b = new CalendarNode(false, 2.0, true);
        CalendarNode c = new CalendarNode(true, 3.0, true);
        List<CalendarNode> list = List.of(a, b, c);
        EventCalendar eventCalendar = new EventCalendar(5,5);
        eventCalendar.setList(list);
        assertEquals(b, eventCalendar.getNextEvent());
        assertEquals(b.getTime(), eventCalendar.getCurrentTime());
    }
    @Test
    public void test_callAvailableWorker() {
        CalendarNode a = new CalendarNode(false, 5.0, false);
        CalendarNode b = new CalendarNode(false, 2.0, false);
        CalendarNode c = new CalendarNode(true, 3.0, true);
        List<CalendarNode> list = new ArrayList<>();
        list.add(c);
        list.add(a);
        list.add(b);
        EventCalendar eventCalendar = new EventCalendar(2,2);
        eventCalendar.setList(list);
        assertEquals(eventCalendar.callAvailableWorker(), a);
    }
}
