package org.jenson.y2024.d08;

import org.jenson.utils.ArrayUtils;
import org.jenson.utils.ListUtils;
import org.jenson.utils.Pair;
import org.jenson.utils.Reader;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.jenson.utils.Printer.printDay;

public class Part1 {

    public static void main(String[] args) {
        printDay(8);

        List<String> input = Reader.readFileLineByLineToText(2024, 8).toList();
        ArrayUtils<Field> map = ArrayUtils.positionAwareMap(input, (Field::new));

        Map<Character, List<Field>> antennas = map.cells().filter(cell -> cell.antenna != null).collect(Collectors.groupingBy(Field::getAntenna));
        antennas.entrySet().forEach(entry -> {
            Character antennaChar = entry.getKey();
            List<Field> antennaFields = entry.getValue();
            Set<Pair<Field, Field>> anntenaPairs = ListUtils.allPossibleCombinations(antennaFields);
            anntenaPairs.forEach(pair -> {
                Field field1 = pair.getFirst();
                Field field2 = pair.getSecond();
                Distance distance = new Distance(field1.row - field2.row, field1.col - field2.col);

                Stream.of(
                                Pair.of(field1.row + distance.rowDiff, field1.col + distance.colDif),
                                Pair.of(field2.row + distance.rowDiff, field2.col + distance.colDif),
                                Pair.of(field1.row - distance.rowDiff, field1.col - distance.colDif),
                                Pair.of(field2.row - distance.rowDiff, field2.col - distance.colDif)
                        )
                        .filter(coordinates -> map.hasPosition(coordinates.getFirst(), coordinates.getSecond()))
                        .filter(coordinates -> map.get(coordinates.getFirst(), coordinates.getSecond()).antenna != antennaChar)
                        .forEach(coordinates -> map.get(coordinates.getFirst(), coordinates.getSecond()).antiNode = true);
            });
        });

//        map.print();
        System.out.println(map.cells().filter(Field::isAntiNode).count());
    }

    private record Distance(int rowDiff, int colDif) {
    }

    private static class Field {
        final int row;
        final int col;
        final Character antenna;
        boolean antiNode = false;

        public Field(int row, int col, Character antenna) {
            this.row = row;
            this.col = col;
            if (antenna != '.') {
                this.antenna = antenna;
            } else {
                this.antenna = null;
            }
        }

        public Character getAntenna() {
            return antenna;
        }

        public boolean isAntiNode() {
            return antiNode;
        }

        @Override
        public String toString() {
            return antenna != null ? antenna.toString() : antiNode ? "#" : ".";
        }
    }
}
