package org.jenson.y2024.d01;

import org.jenson.utils.Reader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.jenson.utils.Printer.printDay;

public class Part1 {

    public static void main(String[] args) {
        printDay(1);

        List<Integer> leftList = new ArrayList<>();
        List<Integer> rightList = new ArrayList<>();

        String[] split = Reader.readFileToText(2024, 1).split("\\s+");
        for (int i = 0; i < split.length; i++) {
            if (i % 2 == 0) {
                leftList.add(Integer.parseInt(split[i]));
            } else {
                rightList.add(Integer.parseInt(split[i]));
            }
        }

        Collections.sort(leftList);
        Collections.sort(rightList);

        long distance = 0L;
        for (int i = 0; i < leftList.size(); i++) {
            distance += Math.abs(leftList.get(i) - rightList.get(i));
        }

        System.out.println(distance);
    }
}
