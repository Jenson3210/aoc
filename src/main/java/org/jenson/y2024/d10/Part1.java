package org.jenson.y2024.d10;

import org.jenson.utils.ArrayUtils;
import org.jenson.utils.Reader;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.jenson.utils.Printer.printDay;

public class Part1 {

    public static void main(String[] args) {
        printDay(10);

        List<String> input = Reader.readFileLineByLineToText(2024, 10).toList();
        ArrayUtils<Field> map = ArrayUtils.positionAwareMap(input, (Field::new));
        long count = map.cells()
                .filter(field -> field.value == 0)
                .mapToInt(field -> {
                    Set<Field> reachable = new HashSet<>();
                    getPath(map, field.row, field.col, field.value, reachable);
                    return reachable.size();
                })
                .sum();

        System.out.println(count);
    }

    private static void getPath(ArrayUtils<Field> map, int row, int col, int value, Set<Field> paths) {
        map.nesw(row, col).stream().filter(field -> field.value == value + 1).forEach(field -> {
            if (field.value == 9) {
                paths.add(field);
            } else {
                getPath(map, field.row, field.col, field.value, paths);
            }
        });
    }

    private static class Field {
        private final int row;
        private final int col;
        private final int value;

        public Field(int row, int col, Character value) {
            this.row = row;
            this.col = col;
            if (value == '.') {
                this.value = -1;
            } else {
                this.value = value - '0';
            }
        }
    }
}
