package org.jenson.y2024.d05;

import org.jenson.utils.Reader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.jenson.utils.Printer.printDay;

public class Part1 {

    public static void main(String[] args) {
        printDay(5);

        List<String> list = Reader.readFileLineByLineToText(2024, 5).toList();
        boolean addingRules = true;
        List<Rule> rules = new ArrayList<>();
        long sum = 0;
        for (String s : list) {
            if (s.trim().isEmpty()) {
                addingRules = false;
                continue;
            }
            if (addingRules) {
                String[] split = s.split("\\|");
                rules.add(new Rule(Integer.parseInt(split[0]), Integer.parseInt(split[split.length - 1])));
            } else {
                List<Integer> pages = Arrays.stream(s.split(",")).map(Integer::parseInt).toList();
                boolean valid = rules.stream().allMatch(rule -> rule.isValid(pages));
                if (valid) {
                    sum += pages.get(pages.size() / 2);
                }
            }
        }

        System.out.println(sum);
    }

    private record Rule(int page, int runsBefore) {
        boolean isValid(List<Integer> pages) {
            if (!pages.contains(page) || !pages.contains(runsBefore)) {
                return true;
            }
            int pageIx = pages.indexOf(page);
            int runsBeforeIx = pages.indexOf(runsBefore);

            return pageIx < runsBeforeIx;
        }
    }
}
