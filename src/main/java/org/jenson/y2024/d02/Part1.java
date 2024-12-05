package org.jenson.y2024.d02;

import java.util.List;
import java.util.stream.Gatherer;
import java.util.stream.Gatherers;

import static org.jenson.utils.Printer.printDay;
import static org.jenson.utils.Reader.readFileLineByLineSeparatedToIntStream;

public class Part1 {

    public static void main(String[] args) {
        printDay(2);

        long validReports = readFileLineByLineSeparatedToIntStream(2024, 2, " ")
                .filter(report -> {
                    Gatherer<Integer, ?, List<Integer>> sliding = Gatherers.windowSliding(2);
                    List<Integer> list = report.boxed()
                            .gather(sliding)
                            .map(pair -> pair.getFirst() - pair.get(1))
                            .toList();
                    boolean allNegative = list.stream().allMatch(i -> i < 0);
                    boolean allPositive = list.stream().allMatch(i -> i > 0);
                    return (allPositive || allNegative) && list.stream().allMatch(i -> Math.abs(i) <= 3);
                })
                .count();

        System.out.println(validReports);
    }
}
