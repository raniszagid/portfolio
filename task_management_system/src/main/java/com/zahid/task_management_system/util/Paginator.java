package com.zahid.task_management_system.util;

import java.util.ArrayList;
import java.util.List;

public class Paginator {
    public static <T> List<T> paginate(List<T> list, int pageNumber, int capacity) {
        int end = pageNumber * capacity;
        int lastIndex = Integer.min(list.size(), end);
        int start = end - capacity;
        return start < lastIndex ? list.subList(start, lastIndex) : new ArrayList<>();
    }
}
