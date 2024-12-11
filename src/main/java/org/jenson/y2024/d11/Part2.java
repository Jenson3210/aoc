package org.jenson.y2024.d11;

import org.jenson.utils.Reader;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.jenson.utils.Printer.printDay;

public class Part2 {

    public static void main(String[] args) {
        printDay(11);

        Map<Key, Long> cache = new ConcurrentHashMap<>();
        List<Stone> list = Reader.readFileLineSeparatedToInt(2024, 11, " ").mapToObj(stone -> new Stone(stone, String.valueOf(stone))).toList();
        long count = list.parallelStream().mapToLong(stone -> stone.blink(0, 75, cache)).sum();

        System.out.println(count);
    }

    private record Key(long value, int loop) {
    }

    private record Stone(long value, String stringValue) {
        public long blink(int i, int j, Map<Key, Long> cache) {
            Key key = new Key(value, i);
            if (cache.containsKey(key)) {
                return cache.get(key);
            }
            long stones;
            if (i == j) {
                stones = 1L;
            } else if (value == 0) {
                stones = new Stone(1, "1").blink(i + 1, j, cache);
            } else if (stringValue.length() % 2 == 0) {
                String firstPart = stripLeadingZeros(stringValue.substring(0, stringValue.length() / 2));
                String secondPart = stripLeadingZeros(stringValue.substring(stringValue.length() / 2));
                stones = new Stone(Long.parseLong(firstPart), firstPart).blink(i + 1, j, cache) +  new Stone(Long.parseLong(secondPart), secondPart).blink(i + 1, j, cache);
            } else {
                long value = this.value * 2024;
                stones = new Stone(value, String.valueOf(value)).blink(i + 1, j, cache);
            }
            cache.put(key, stones);
            return stones;
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
