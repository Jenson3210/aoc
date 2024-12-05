package org.jenson.y2024.d05;

import org.jenson.utils.Reader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static org.jenson.utils.Printer.printDay;

public class Part2 {

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
                LinkedList<Integer> pages = new LinkedList<>(Arrays.stream(s.split(",")).map(Integer::parseInt).toList());
                boolean valid = rules.stream().allMatch(rule -> rule.isValid(pages));
                if (valid) {
                    continue;
                }
                while (!valid) {
                    List<Rule> applicableRules = rules.stream().filter(rule -> rule.appliesTo(pages)).toList();

                    for (Rule rule : applicableRules) {
                        int pageIx = pages.indexOf(rule.page);
                        int runsBeforeIx = pages.indexOf(rule.runsBefore);
                        if (pageIx > runsBeforeIx) {
                            pages.remove(pageIx);
                            pages.add(runsBeforeIx, rule.page);
                        }
                    }

                    valid = rules.stream().allMatch(rule -> rule.isValid(pages));
                }
                sum += pages.get(pages.size() / 2);
            }
        }

        System.out.println(sum);
    }

    private record Rule(int page, int runsBefore) {
        boolean isValid(List<Integer> pages) {
            if (!appliesTo(pages)) {
                return true;
            }
            int pageIx = pages.indexOf(page);
            int runsBeforeIx = pages.indexOf(runsBefore);

            return pageIx < runsBeforeIx;
        }

        boolean appliesTo(List<Integer> pages) {
            return pages.contains(page) && pages.contains(runsBefore);
        }
    }
}
