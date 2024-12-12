package org.jenson.y2024.d12;

import org.jenson.utils.ArrayUtils;
import org.jenson.utils.Reader;

import java.util.HashSet;
import java.util.Set;

import static org.jenson.utils.Printer.printDay;

public class Part1 {

    public static void main(String[] args) {
        printDay(12);

        ArrayUtils<Field> map = ArrayUtils.positionAwareMap(Reader.readFileLineByLineToText(2024, 12).toList(), Field::new);
        Crops crops = new Crops(map);
        Set<Region> regions = crops.getRegions();
        long sum = regions.stream().mapToLong(region -> region.perimeter(map) * region.plants().size()).sum();

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
        long perimeter(ArrayUtils<Field> map) {
            return plants.stream().flatMap(field -> map.neswGrowingOutsideMap(field.row, field.col, Field::new).stream()).filter(field -> field.value == null || field.value != value).count();
        }
    }

    private record Field(int row, int col, Character value) {}
}
