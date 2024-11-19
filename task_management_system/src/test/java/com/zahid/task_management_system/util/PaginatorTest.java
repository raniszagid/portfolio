package com.zahid.task_management_system.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;


public class PaginatorTest {
    @Test
    public void testPaginator() {
        List<Integer> list = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        List<Integer> actualResult = Paginator.paginate(list, 3, 4);
        List<Integer> expectedResult = List.of(9, 10);
        Assertions.assertEquals(expectedResult, actualResult);
    }
}
