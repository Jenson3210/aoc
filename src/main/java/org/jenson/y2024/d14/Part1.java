package org.jenson.y2024.d14;

import org.jenson.utils.MathUtils;
import org.jenson.utils.Reader;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.jenson.utils.Printer.printDay;

public class Part1 {

    public static void main(String[] args) {
        printDay(14);

//        XandY mapSize = new XandY(11, 7);
        XandY mapSize = new XandY(101, 103);

        int seconds = 100;

        Map<Integer, List<Integer>> robots = Reader.readFileLineByLineToText(2024, 14)
                .map(Robot::new)
                .map(r -> r.positionAfter(mapSize, seconds))
                .map(xAndY -> xAndY.quadrant(mapSize))
                .collect(Collectors.groupingBy(Function.identity()));

        IntStream.rangeClosed(1, 4).map(i -> robots.get(i).size()).reduce((a, b) -> a * b).ifPresent(System.out::println);
    }

    private record XandY(int x, int y) {
        public int quadrant(XandY mapSize) {
            if (x < mapSize.x() / 2 && y < mapSize.y() / 2) {
                return 1;
            }
            if (x > mapSize.x() / 2 && y < mapSize.y() / 2) {
                return 2;
            }
            if (x < mapSize.x() / 2 && y > mapSize.y() / 2) {
                return 3;
            }
            if (x > mapSize.x() / 2 && y > mapSize.y() / 2) {
                return 4;
            }
            return -1;
        }
    }

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
