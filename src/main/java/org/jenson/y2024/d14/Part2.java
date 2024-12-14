package org.jenson.y2024.d14;

import org.jenson.utils.MathUtils;
import org.jenson.utils.Reader;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.jenson.utils.Printer.printDay;

public class Part2 {

    public static void main(String[] args) {
        printDay(14);

        XandY mapSize = new XandY(101, 103);

        int seconds = 0;

        while (true) {
            int s = seconds;
            Map<XandY, Long> robots = Reader.readFileLineByLineToText(2024, 14)
                    .map(Robot::new)
                    .map(r -> r.positionAfter(mapSize, s))
                    .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

            if (robots.values().stream().noneMatch(v -> v > 1)) {
                break;
            }
            seconds++;
        }

        System.out.println(seconds);
    }

    private record XandY(int x, int y) {}

    private record Robot(XandY start, XandY velocity) {
        public Robot(String input) {
            String[] split = input.split("=");
            String[] p = split[1].split(",");
            String[] py = p[1].split(" ");
            String[] v = split[2].split(",");
            this(new XandY(Integer.parseInt(p[0]), Integer.parseInt(py[0])), new XandY(Integer.parseInt(v[0]), Integer.parseInt(v[1])));
        }

        public XandY positionAfter(XandY mapSize, long seconds) {
            long loopsAfter = loopsAfter(mapSize);
            int significantSeconds = (int) (seconds % loopsAfter);
            int newCol = newPosition(start.x(), velocity.x(), significantSeconds, mapSize.x());
            int newRow = newPosition(start.y(), velocity.y(), significantSeconds, mapSize.y());

            return new XandY(newCol, newRow);
        }

        private int newPosition(int start, int velocity, int seconds, int mapSize) {
            if (velocity == 0) {
                return start;
            }
            if (velocity > 0) {
                return (start + velocity * seconds) % (mapSize);
            }
            int newLocation = (start + velocity * seconds);
            if (newLocation >= 0) {
                return newLocation;
            }

            int i = mapSize - (Math.abs(newLocation)  % mapSize);
            if (i == mapSize) {
                return 0;
            }
            return i;
        }

        private long loopsAfter(XandY mapSize) {
            int xLoops = mapSize.x() / (int) MathUtils.greatestCommonDivisor(mapSize.x(), Math.abs(velocity.x()));
            int yLoops = mapSize.y() / (int) MathUtils.greatestCommonDivisor(mapSize.y(), Math.abs(velocity.y()));
            if (xLoops == 0 || yLoops == 0) {
                return Math.max(xLoops, yLoops);
            }
            return MathUtils.leastCommonMultiple(xLoops, yLoops);
        }
    }

}
