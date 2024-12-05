package org.jenson.y2024.d02;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Gatherer;
import java.util.stream.Gatherers;

import static org.jenson.utils.Printer.printDay;
import static org.jenson.utils.Reader.readFileLineByLineSeparatedToIntStream;

public class Part2 {

    public static void main(String[] args) {
        printDay(2);

        long validReports = readFileLineByLineSeparatedToIntStream(2024, 2, " ")
                .filter(report -> {
                    Gatherer<Integer, ?, List<Integer>> sliding = Gatherers.windowSliding(2);
                    List<Integer> list = report.boxed().toList();
                    for (int removeIx = 0; removeIx < list.size(); removeIx++) {
                        List<Integer> levels = new ArrayList<>(list);
                        levels.remove(removeIx);

                        List<Integer> levelDiffs = levels.stream()
                                .gather(sliding)
                                .map(pair -> pair.getFirst() - pair.get(1))
                                .toList();
                        boolean allNegative = levelDiffs.stream().allMatch(i -> i < 0);
                        boolean allPositive = levelDiffs.stream().allMatch(i -> i > 0);
                        if ((allPositive || allNegative) && levelDiffs.stream().allMatch(i -> Math.abs(i) <= 3)) {
                            return true;
                        }
                    }
                    return false;
                })
                .count();

        System.out.println(validReports);
    }
}
