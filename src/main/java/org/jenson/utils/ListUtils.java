package org.jenson.utils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ListUtils {

    private ListUtils() {}

    public static <T> Set<Pair<T, T>> allPossibleCombinations(List<T> list) {
        Set<Pair<T, T>> set = new HashSet<>();
        for (int i = 0; i < list.size(); i++) {
            for (int j = i + 1; j < list.size(); j++) {
                set.add(Pair.of(list.get(i), list.get(j)));
            }
        }

        return set;
    }
}
