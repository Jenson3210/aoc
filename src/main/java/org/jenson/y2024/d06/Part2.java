package org.jenson.y2024.d06;

import org.jenson.utils.ArrayUtils;
import org.jenson.utils.Reader;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Function;
import java.util.function.Predicate;

import static org.jenson.utils.Printer.printDay;

public class Part2 {

    public static void main(String[] args) {
        printDay(6);

        GuardPath path = new GuardPath(Reader.readFileLineByLineToText(2024, 6).toList());
        Collection<Step> walk = path.walk();
        Set<Position> obstacles = new HashSet<>();
        for (Step step : walk) {
            Position nextField = switch (step.direction) {
                case NORTH -> new Position(step.position.row() - 1, step.position.col());
                case EAST -> new Position(step.position.row(), step.position.col() + 1);
                case SOUTH -> new Position(step.position.row() + 1, step.position.col());
                case WEST -> new Position(step.position.row(), step.position.col() - 1);
            };
            if (path.hasPosition(nextField)) {
                if (obstacles.contains(nextField) || path.map.get(nextField.row(), nextField.col()).blocked() || path.map.get(nextField.row(), nextField.col()).startPosition()) {
                    continue;
                }
                GuardPath blocked = path.clone(field -> {
                    if (field.position().equals(nextField)) {
                        return new Field(field.position(), true, false);
                    }
                    return field;
                });
                if (blocked.hasLoop()) {
                    obstacles.add(nextField);
                }
            }
        }

        System.out.println(obstacles.size());
    }

    private static class GuardPath {
        private final ArrayUtils<Field> map;

        public GuardPath(List<String> map) {
            this.map = ArrayUtils.positionAwareMap(map, ((row, col, value) -> new Field(new Position(row, col), value == '#', value == '^')));
        }

        public GuardPath(ArrayUtils<Field> map) {
            this.map = map;
        }

        public GuardPath clone(Function<Field, Field> mapper) {
            return new GuardPath(map.clone(mapper));
        }

        public boolean hasPosition(Position position) {
            return map.hasPosition(position.row(), position.col());
        }

        private Collection<Step> walk() {
            List<Step> path = new ArrayList<>();

            map.findFirst(Field::startPosition).ifPresent(startPosition -> {
                Field current = startPosition;
                Direction direction = getDirection(current, Direction.NORTH);
                path.add(new Step(current.position, direction));
                while (current != null) {
                    current = move(current, direction);
                    if (current != null) {
                        direction = getDirection(current, direction);
                        if (path.contains(new Step(current.position, direction))) {
                            break;
                        }
                        path.add(new Step(current.position, direction));
                    }
                }
            });

            return path;
        }

        private boolean hasLoop() {
            Set<Step> path = new HashSet<>();

            Optional<Field> startField = map.findFirst(Field::startPosition);
            if (startField.isPresent()) {
                Direction direction = Direction.NORTH;
                Field current = startField.get();
                while (current != null) {
                    direction = getDirection(current, direction);
                    current = move(current, direction);
                    if (current != null) {
                        boolean added = path.add(new Step(current.position, direction));
                        if (!added) {
                            return true;
                        }
                    }
                }
            }

            return false;
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
                return getDirection(current, direction);
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

    private record Step(Position position, Direction direction) {}

    private record Position(int row, int col) {
        @Override
        public String toString() {
            return "Position[" + "row=" + (row + 1) + ", col=" + (col + 1) + ']';
        }
    }

    private enum Direction {
        NORTH, EAST, SOUTH, WEST
    }

    private record Field(Position position, boolean blocked, boolean startPosition) {
        int row() {
            return position.row();
        }

        int col() {
            return position.col();
        }
    }
}
