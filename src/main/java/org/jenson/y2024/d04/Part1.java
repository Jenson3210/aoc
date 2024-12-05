package org.jenson.y2024.d04;

import org.jenson.utils.ArrayUtils;
import org.jenson.utils.Reader;

import java.util.List;

import static org.jenson.utils.Printer.printDay;

public class Part1 {

    public static void main(String[] args) {
        printDay(4);

        ArrayUtils<Character> objectArrayUtils = ArrayUtils.ofMap(Reader.readFileLineByLineToText(2024, 4).toList());
        long occurences = 0;
        for (int i = 0; i < objectArrayUtils.width(); i++) {
            for (int j = 0; j < objectArrayUtils.height(); j++) {
                if (objectArrayUtils.get(i, j) == 'X') {
                    occurences += allDirections(objectArrayUtils, i, j).stream()
                            .filter(Part1::startsWithMAS)
                            .count();
                }
            }
        }

        System.out.println(occurences);
    }

    private static List<List<Character>> allDirections(ArrayUtils<Character> objectArrayUtils, int i, int j) {
        return List.of(
                objectArrayUtils.allNorth(i, j),
                objectArrayUtils.allNorthEast(i, j),
                objectArrayUtils.allEast(i, j),
                objectArrayUtils.allSouthEast(i, j),
                objectArrayUtils.allSouth(i, j),
                objectArrayUtils.allSouthWest(i, j),
                objectArrayUtils.allWest(i, j),
                objectArrayUtils.allNorthWest(i, j)
        );
    }

    private static boolean startsWithMAS(List<Character> characters) {
        return "MAS".equalsIgnoreCase(characters.stream().map(String::valueOf).limit(3).reduce("", String::concat));
    }
}
