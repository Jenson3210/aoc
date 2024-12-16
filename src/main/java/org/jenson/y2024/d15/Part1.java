package org.jenson.y2024.d15;

import org.jenson.utils.ArrayUtils;
import org.jenson.utils.Reader;

import java.util.List;
import java.util.function.BiFunction;

import static org.jenson.utils.Printer.printDay;

public class Part1 {

    public static void main(String[] args) {
        printDay(15);

        GPS gps = new GPS(Reader.readFileLineByLineToText(2024, 15).toList());
        int sum = gps.move();

        System.out.println(sum);
    }

    private static class GPS {
        private final ArrayUtils<Field> fields;
        private final List<String> moves;

        public GPS(List<String> input) {
            List<String> map = input.stream().takeWhile(s -> !s.trim().isEmpty()).toList();
            fields = ArrayUtils.positionAwareMap(map, Field::new);
            moves = input.stream().dropWhile(s -> !s.trim().isEmpty()).skip(1).toList();
        }

        public int move() {
            Field player = fields.cells().filter(Field::isPlayer).findFirst().orElseThrow();
            for (String move : moves) {
                for (char c : move.toCharArray()) {
                    BiFunction<Integer, Integer, List<Field>> direction = switch (c) {
                        case '^' -> (x, y) -> fields.allNorthWhile(x, y, field -> !field.isWall());
                        case 'v' -> (x, y) -> fields.allSouthWhile(x, y, field -> !field.isWall());
                        case '<' -> (x, y) -> fields.allWestWhile(x, y, field -> !field.isWall());
                        case '>' -> (x, y) -> fields.allEastWhile(x, y, field -> !field.isWall());
                        default -> throw new IllegalStateException("Unexpected value: " + c);
                    };
                    List<Field> walking = direction.apply(player.x, player.y);
                    int  boxes = (int) walking.stream().takeWhile(Field::isBox).count();
                    if (boxes == 0 && !walking.isEmpty()) {
                        Field walkTo = walking.getFirst();
                        walkTo.setPlayer(true);
                        player.setPlayer(false);
                        player = walkTo;
                    } else if (boxes < walking.size()) {
                        Field walkTo = walking.getFirst();
                        Field pushTo = walking.get(boxes);
                        walkTo.setPlayer(true);
                        walkTo.setBox(false);
                        pushTo.setBox(true);
                        player.setPlayer(false);
                        player = walkTo;
                    } else if (boxes != walking.size()) {
                        throw new IllegalStateException("Unexpected value: " + c);
                    }
                }
            }
            return fields.cells().filter(Field::isBox).mapToInt(Field::score).sum();
        }
    }

    private static class Field {
        private final int x;
        private final int y;
        private boolean wall;
        private boolean box;
        private boolean player;

        public Field(int x, int y, Character value) {
            this.x = x;
            this.y = y;
            this.wall = value.equals('#');
            this.box = value.equals('O');
            this.player = value.equals('@');
        }

        public int score() {
            return 100 * x + y;
        }

        public boolean isWall() {
            return wall;
        }

        public boolean isBox() {
            return box;
        }

        public boolean isPlayer() {
            return player;
        }

        public void setBox(boolean box) {
            this.box = box;
        }

        public void setPlayer(boolean player) {
            this.player = player;
        }

        @Override
        public String toString() {
            if (wall) {
                return "#";
            } else if (box) {
                return "O";
            } else if (player) {
                return "@";
            } else {
                return ".";
            }
        }
    }
}
