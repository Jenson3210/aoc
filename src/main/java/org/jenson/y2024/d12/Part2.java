package org.jenson.y2024.d12;

import org.jenson.utils.ArrayUtils;
import org.jenson.utils.Reader;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.jenson.utils.Printer.printDay;

public class Part2 {

    public static void main(String[] args) {
        printDay(12);

        ArrayUtils<Field> map = ArrayUtils.positionAwareMap(Reader.readFileLineByLineToText(2024, 12).toList(), Field::new);
        Crops crops = new Crops(map);
        Set<Region> regions = crops.getRegions();
        long sum = regions.stream().mapToLong(region -> region.sides(map) * region.plants().size()).sum();

        System.out.println(sum);
    }

    private record Crops (ArrayUtils<Field> map) {
        public Set<Region> getRegions() {
            Set<Region> regions = new HashSet<>();
            map.cells().forEach(field -> {
                Set<Field> currentPlants = new HashSet<>();
                currentPlants.add(field);
                addSurroundingPlants(field, currentPlants);
                regions.add(new Region(field.value, currentPlants));
            });
            return regions;
        }

        private void addSurroundingPlants(Field field, Set<Field> plants) {
            map.nesw(field.row, field.col).stream().filter(f -> f.value == field.value).forEach(f -> {
                if (plants.add(f)) {
                    addSurroundingPlants(f, plants);
                }
            });
        }
    }

    private record Region(char value, Set<Field> plants) {
        long sides(ArrayUtils<Field> map) {
            Map<Integer, List<Border>> northBounds = plants.stream().map(field -> new Border(field, map.northOr(field.row, field.col, Field::new))).filter(field -> field.from().value == null || field.from().value != value).collect(Collectors.groupingBy(border -> border.from().row()));
            return sides((row, col) -> map.northOr(row, col, Field::new), Field::col, Field::row) +
                    sides((row, col) -> map.eastOr(row, col, Field::new), Field::row, Field::col) +
                    sides((row, col) -> map.southOr(row, col, Field::new), Field::col, Field::row) +
                    sides((row, col) -> map.westOr(row, col, Field::new), Field::row, Field::col);
        }

        private long sides(BiFunction<Integer, Integer, Field> direction, Function<Field, Integer> rowColExtractor, Function<Field, Integer> groupByExtractor) {
            Map<Integer, List<Border>> bounds = plants.stream().map(field -> new Border(field, direction.apply(field.row(), field.col()))).filter(field -> field.to().value == null || field.to().value != value).collect(Collectors.groupingBy(border ->  groupByExtractor.apply(border.from())));
            long sum = bounds.values().stream().mapToLong(borders -> {
                long parts = 1;
                borders.sort(Comparator.comparing(border -> rowColExtractor.apply(border.from())));
                int previousCol = rowColExtractor.apply(borders.getFirst().to()) - 1;
                for (Border border : borders) {
                    int col = rowColExtractor.apply(border.to());
                    if (col - previousCol > 1) {
                        parts++;
                    }
                    previousCol = col;
                }
                return parts;
            }).sum();
            return sum;
        }
    }

    private record Border(Field from, Field to) {}

    private record Field(int row, int col, Character value) {}
}
