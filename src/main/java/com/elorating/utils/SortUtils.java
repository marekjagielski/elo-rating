package com.elorating.utils;

import org.springframework.data.domain.Sort;

public class SortUtils {

    public static Sort getSort(String direction) {
        if (direction != null && direction.length() > 0) {
            try {
                return Sort.by(Sort.Direction.fromString(direction), "date");
            } catch (IllegalArgumentException e) {
                return Sort.by(Sort.Direction.DESC, "date");
            }
        }
        return Sort.by(Sort.Direction.DESC, "date");
    }

    public static Sort getSortAscending() {
        return getSort("asc");
    }

    public static Sort getSortDescending() {
        return getSort("desc");
    }
}
