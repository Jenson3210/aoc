package org.jenson.y2024.d15;

import org.jenson.utils.ArrayUtils;
import org.jenson.utils.Pair;
import org.jenson.utils.Reader;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;

import static org.jenson.utils.Printer.printDay;

public class Part2 {

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
            List<String> map = input.stream()
                    .takeWhile(s -> !s.trim().isEmpty())
                    .map(s -> s.replaceAll("#", "##").replaceAll("O", "[]"). replaceAll("\\.", "..").replaceAll("@", "@."))
                    .toList();
            fields = ArrayUtils.positionAwareMap(map, Field::new);
            moves = input.stream().dropWhile(s -> !s.trim().isEmpty()).skip(1).toList();
        }

        public int move() {
            Field player = fields.cells().filter(Field::isPlayer).findFirst().orElseThrow();
            for (String move : moves) {
                for (char c : move.toCharArray()) {
                    switch (c) {
                        case '^' -> {
                            Field destination = fields.north(player.x, player.y);
                            if (!destination.isBox() && !destination.isWall()) {
                                destination.setPlayer(true);
                                player.setPlayer(false);
                                player = destination;
                            } else if (destination.isBox() && canMove(destination.siblings(fields), fields, f -> fields.north(f.x, f.y))) {
                                Pair<Field, Field> boxParts = destination.siblings(fields);
                                moveBox(boxParts, fields, f -> fields.north(f.x, f.y));
                                boxParts.getFirst().setBox(null);
                                boxParts.getSecond().setBox(null);
                                destination.setPlayer(true);
                                player.setPlayer(false);
                                player = destination;
                            }
                        }
                        case 'v' -> {
                            Field destination = fields.south(player.x, player.y);
                            if (!destination.isBox() && !destination.isWall()) {
                                destination.setPlayer(true);
                                player.setPlayer(false);
                                player = destination;
                            } else if (destination.isBox() && canMove(destination.siblings(fields), fields, f -> fields.south(f.x, f.y))) {
                                Pair<Field, Field> boxParts = destination.siblings(fields);
                                moveBox(boxParts, fields, f -> fields.south(f.x, f.y));
                                boxParts.getFirst().setBox(null);
                                boxParts.getSecond().setBox(null);
                                destination.setPlayer(true);
                                player.setPlayer(false);
                                player = destination;
                            }
                        }
                        case '<' -> {
                            Field destination = fields.west(player.x, player.y);
                            if (destination.isBox() && canMove(destination.siblings(fields), fields, f -> fields.west(f.x, f.y))) {
                                List<Field> toMove = fields.allWestWhile(player.x, player.y, Field::isBox);
                                for (int i = toMove.size() - 1; i >= 0; i--) {
                                    Field box = toMove.get(i);
                                    Field moveTo = fields.west(box.x, box.y);
                                    moveTo.setBox(box.boxPart);
                                }
                                destination.setBox(null);
                                destination.setPlayer(true);
                                player.setPlayer(false);
                                player = destination;
                            } else if (!destination.isBox() && !destination.isWall()) {
                                destination.setPlayer(true);
                                player.setPlayer(false);
                                player = destination;
                            }
                        }
                        case '>' -> {
                            Field destination = fields.east(player.x, player.y);
                            if (destination.isBox() && canMove(destination.siblings(fields), fields, f -> fields.east(f.x, f.y))) {
                                List<Field> toMove = fields.allEastWhile(player.x, player.y, Field::isBox);
                                for (int i = toMove.size() - 1; i >= 0; i--) {
                                    Field box = toMove.get(i);
                                    Field moveTo = fields.east(box.x, box.y);
                                    moveTo.setBox(box.boxPart);
                                }
                                destination.setBox(null);
                                destination.setPlayer(true);
                                player.setPlayer(false);
                                player = destination;
                            } else if (!destination.isBox() && !destination.isWall()) {
                                destination.setPlayer(true);
                                player.setPlayer(false);
                                player = destination;
                            }
                        }
                    }
                }
            }
            return fields.cells().mapToInt(Field::score).sum();
        }
    }

    private static boolean canMove(Pair<Field, Field> box, ArrayUtils<Field> map, Function<Field, Field> direction) {
        if (box == null) {
            return true;
        }
        Field open = direction.apply(box.getFirst());
        Field close = direction.apply(box.getSecond());
        if (open.isWall() || close.isWall()) {
            return false;
        }
        if (open.isBox() || close.isBox()) {
            if (box.getFirst().equals(open) || box.getSecond().equals(open)) {
                return canMove(close.siblings(map), map, direction);
            } else if (box.getFirst().equals(close) || box.getSecond().equals(close)) {
                return canMove(open.siblings(map), map, direction);
            } else {
                return canMove(open.siblings(map), map, direction) && canMove(close.siblings(map), map, direction);
            }
        }
        return true;
    }

    private static void moveBox(Pair<Field, Field> box, ArrayUtils<Field> map, Function<Field, Field> direction) {
        Field open = direction.apply(box.getFirst());
        Field close = direction.apply(box.getSecond());
        if (open.isWall() || close.isWall()) {
            return;
        }
        if (open.isBox() && close.isBox() && !Objects.equals(open.siblings(map), close.siblings(map))) {
            moveBox(Objects.requireNonNull(open.siblings(map)), map, direction);
            moveBox(Objects.requireNonNull(close.siblings(map)), map, direction);
        } else if (open.isBox()) {
            moveBox(Objects.requireNonNull(open.siblings(map)), map, direction);
        } else if (close.isBox()) {
            moveBox(Objects.requireNonNull(close.siblings(map)), map, direction);
        }
        open.setBox(box.getFirst().boxPart);
        close.setBox(box.getSecond().boxPart);
        if (!box.getFirst().equals(open) && !box.getFirst().equals(close)) {
            box.getFirst().setBox(null);
        }
        if (!box.getSecond().equals(open) && !box.getSecond().equals(close)) {
            box.getSecond().setBox(null);
        }
    }

    private static class Field {
        private final int x;
        private final int y;
        private boolean wall;
        private Character boxPart;
        private boolean player;

        public Field(int x, int y, Character value) {
            this.x = x;
            this.y = y;
            this.wall = value.equals('#');
            this.boxPart = value.equals('[') || value.equals(']') ? value : null;
            this.player = value.equals('@');
        }

        public int score() {
            if (isBox() && boxPart == '[') {
                return 100 * x + y;
            }
            return 0;
        }

        public boolean isWall() {
            return wall;
        }

        public boolean isBox() {
            return boxPart != null;
        }

        public Pair<Field, Field> siblings(ArrayUtils<Field> fields) {
            if (isBox()) {
                if (boxPart.equals('[')) {
                    return Pair.of(this, fields.get(x, y + 1));
                } else {
                    return Pair.of(fields.get(x, y - 1), this);
                }
            }
            return null;
        }

        public boolean isPlayer() {
            return player;
        }

        public void setWall(boolean wall) {
            this.wall = wall;
        }

        public void setBox(Character box) {
            this.boxPart = box;
        }

        public void setPlayer(boolean player) {
            this.player = player;
        }

        @Override
        public String toString() {
            if (wall) {
                return "#";
            } else if (isBox()) {
                return boxPart.toString();
            } else if (player) {
                return "@";
            } else {
                return ".";
            }
        }
    }
}
