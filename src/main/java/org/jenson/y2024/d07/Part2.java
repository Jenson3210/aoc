package org.jenson.y2024.d07;

import org.jenson.utils.Reader;

import static org.jenson.utils.Printer.printDay;

public class Part2 {

    public static void main(String[] args) {
        printDay(7);

        Long sum = Reader.readFileLineByLineToText(2024, 7).filter(Part2::canBeOperated).mapToLong(Part2::getValue).sum();
        System.out.println(sum);
    }

    private static boolean canBeOperated(long expectedValue, long value, String line) {
        String[] values = line.split(" ", 2);
        int number = Integer.parseInt(values[0]);
        if (values.length == 1) {
            return expectedValue == value + number || expectedValue == value * number || expectedValue == Long.parseLong(value + line);
        }
        return canBeOperated(expectedValue, value + number, values[1])
                || canBeOperated(expectedValue, value * number, values[1])
                || canBeOperated(expectedValue, Long.parseLong(value + "" + number), values[1]);
    }

    private static boolean canBeOperated(String line) {
        return canBeOperated(getValue(line), 0, line.split(": ")[1]);
    }

    private static long getValue(String line) {
        return Long.parseLong(line.split(": ")[0]);
    }
}
