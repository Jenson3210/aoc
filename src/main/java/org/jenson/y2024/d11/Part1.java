package org.jenson.y2024.d11;

import org.jenson.utils.Reader;

import static org.jenson.utils.Printer.printDay;

public class Part1 {

    public static void main(String[] args) {
        printDay(11);

        long count = Reader.readFileLineSeparatedToInt(2024, 11, " ").mapToLong(stone -> new Stone(String.valueOf(stone)).blink(0, 25)).sum();

        System.out.println(count);
    }

    private record Stone(String value) {
        public long blink(int i, int j) {
            if (i == j) {
                return 1;
            }
            if ("0".equals(value)) {
                return new Stone("1").blink(i + 1, j);
            } else if (value.length() % 2 == 0) {
                String firstPart = stripLeadingZeros(value.substring(0, value.length() / 2));
                String secondPart = stripLeadingZeros(value.substring(value.length() / 2));
                return new Stone(firstPart).blink(i + 1, j) + new Stone(secondPart).blink(i + 1, j);
            } else {
                return new Stone(String.valueOf(Long.parseLong(value) * 2024)).blink(i + 1, j);
            }
        }
    }

    public static String stripLeadingZeros(String value) {
        int left = value.indexOf('0');
        if (left == 0 && value.length() > 1) {
            return stripLeadingZeros(value.substring(1));
        }

        return value;
    }
}
