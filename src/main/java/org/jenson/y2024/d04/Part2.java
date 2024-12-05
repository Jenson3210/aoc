package org.jenson.y2024.d04;

import org.jenson.utils.ArrayUtils;
import org.jenson.utils.Reader;

import java.util.Objects;
import java.util.stream.Stream;

import static org.jenson.utils.Printer.printDay;

public class Part2 {

    public static void main(String[] args) {
        printDay(4);

        ArrayUtils<Character> objectArrayUtils = ArrayUtils.ofMap(Reader.readFileLineByLineToText(2024, 4).toList());
        long occurences = 0;
        for (int i = 0; i < objectArrayUtils.width(); i++) {
            for (int j = 0; j < objectArrayUtils.height(); j++) {
                if (objectArrayUtils.get(i, j) == 'A') {
                    String diagonal1 = Stream.of(objectArrayUtils.northEastOrNull(i, j), objectArrayUtils.southWestOrNull(i, j))
                            .filter(Objects::nonNull)
                            .map(String::valueOf)
                            .reduce("", String::concat);

                    if (!("MS".equalsIgnoreCase(diagonal1) || "SM".equalsIgnoreCase(diagonal1))) {
                        continue;
                    }

                    String diagonal2 = Stream.of(objectArrayUtils.northWestOrNull(i, j), objectArrayUtils.southEastOrNull(i, j))
                            .filter(Objects::nonNull)
                            .map(String::valueOf)
                            .reduce("", String::concat);

                    if ("MS".equalsIgnoreCase(diagonal2) || "SM".equalsIgnoreCase(diagonal2)) {
                        occurences++;
                    }
                }
            }
        }

        System.out.println(occurences);
    }
}
