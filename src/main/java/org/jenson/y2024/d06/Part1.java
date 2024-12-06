package org.jenson.y2024.d06;

import org.jenson.utils.ArrayUtils;
import org.jenson.utils.Reader;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.jenson.utils.Printer.printDay;

public class Part1 {

    public static void main(String[] args) {
        printDay(6);

        GuardPath path = new GuardPath(Reader.readFileLineByLineToText(2024, 6).toList());

        Collection<Field> walk = path.walk();

        System.out.println(walk.size());
    }

    private static class GuardPath {
        private final ArrayUtils<Field> map;

        public GuardPath(List<String> map) {
            this.map = ArrayUtils.positionAwareMap(map, ((row, col, value) -> new Field(row, col, value == '#', value == '^')));
        }

        private Collection<Field> walk() {
            Set<Field> path = new HashSet<>();

            map.findFirst(Field::startPosition).ifPresent(startPosition -> {
                Direction direction = Direction.NORTH;
                Field current = startPosition;
                path.add(current);
                while (current != null) {
                    direction = getDirection(current, direction);
                    current = move(current, direction);
                    if (current != null) {
                        path.add(current);
                    }
                }
            });

            return path;
        }

        private Direction getDirection(Field current, Direction direction) {
            Field next = switch (direction) {
                case NORTH -> map.northOrNull(current.row(), current.col());
                case EAST -> map.eastOrNull(current.row(), current.col());
                case SOUTH -> map.southOrNull(current.row(), current.col());
                case WEST -> map.westOrNull(current.row(), current.col());
            };
            if (next != null && next.blocked()) {
                direction = switch (direction) {
                    case NORTH -> Direction.EAST;
                    case EAST -> Direction.SOUTH;
                    case SOUTH -> Direction.WEST;
                    case WEST -> Direction.NORTH;
                };
            }
            return direction;
        }

        private Field move(Field current, Direction direction) {
            return switch (direction) {
                case NORTH -> map.northOrNull(current.row(), current.col());
                case EAST -> map.eastOrNull(current.row(), current.col());
                case SOUTH -> map.southOrNull(current.row(), current.col());
                case WEST -> map.westOrNull(current.row(), current.col());
            };
        }
    }

    private enum Direction {
        NORTH, EAST, SOUTH, WEST
    }

    private record Field(int row, int col, boolean blocked, boolean startPosition){};
}
