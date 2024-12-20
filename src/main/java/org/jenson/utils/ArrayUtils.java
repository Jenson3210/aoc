package org.jenson.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class ArrayUtils<T> {

    T[][] array;

    public ArrayUtils(T[][] array) {
        this.array = array;
    }

    public static ArrayUtils<Character> ofMap(List<String> map) {
        Character[][] array = new Character[map.size()][map.get(0).length()];
        for (int i = 0; i < map.size(); i++) {
            array[i] = map.get(i).chars().mapToObj(c -> (char) c).toArray(Character[]::new);
        }
        return new ArrayUtils<>(array);
    }

    public static <T> ArrayUtils<T> positionAwareMap(List<String> map, PositionAwareFactory<T> factory) {
        T[][] array = (T[][]) new Object[map.size()][map.get(0).length()];
        for (int i = 0; i < map.size(); i++) {
            for (int j = 0; j < map.getFirst().length(); j++) {
                array[i][j] = factory.create(i, j, map.get(i).charAt(j));
            }
        }
        return new ArrayUtils<>(array);
    }

    public int width() {
        return array[0].length;
    }

    public int height() {
        return array.length;
    }

    public T get(int i, int j) {
        return array[i][j];
    }

    public T northOrNull(int rowIndex, int colIndex) {
        return directionOrNull(rowIndex, colIndex, this::north);
    }

    public T eastOrNull(int rowIndex, int colIndex) {
        return directionOrNull(rowIndex, colIndex, this::east);
    }

    public T southOrNull(int rowIndex, int colIndex) {
        return directionOrNull(rowIndex, colIndex, this::south);
    }

    public T westOrNull(int rowIndex, int colIndex) {
        return directionOrNull(rowIndex, colIndex, this::west);
    }

    public T northEastOrNull(int rowIndex, int colIndex) {
        return directionOrNull(rowIndex, colIndex, this::northEast);
    }

    public T northWestOrNull(int rowIndex, int colIndex) {
        return directionOrNull(rowIndex, colIndex, this::northWest);
    }

    public T southEastOrNull(int rowIndex, int colIndex) {
        return directionOrNull(rowIndex, colIndex, this::southEast);
    }

    public T southWestOrNull(int rowIndex, int colIndex) {
        return directionOrNull(rowIndex, colIndex, this::southWest);
    }

    public T northOr(int rowIndex, int colIndex, PositionAwareFactory<T> factory) {
        return directionOr(rowIndex, colIndex, -1, 0, factory);
    }

    public T eastOr(int rowIndex, int colIndex, PositionAwareFactory<T> factory) {
        return directionOr(rowIndex, colIndex, 0, 1, factory);
    }

    public T southOr(int rowIndex, int colIndex, PositionAwareFactory<T> factory) {
        return directionOr(rowIndex, colIndex, 1, 0, factory);
    }

    public T westOr(int rowIndex, int colIndex, PositionAwareFactory<T> factory) {
        return directionOr(rowIndex, colIndex, 0, -1, factory);
    }

    public T northEastOr(int rowIndex, int colIndex, PositionAwareFactory<T> factory) {
        return directionOr(rowIndex, colIndex, -1, 1, factory);
    }

    public T northWestOr(int rowIndex, int colIndex, PositionAwareFactory<T> factory) {
        return directionOr(rowIndex, colIndex, -1, -1, factory);
    }

    public T southEastOr(int rowIndex, int colIndex, PositionAwareFactory<T> factory) {
        return directionOr(rowIndex, colIndex, 1, 1, factory);
    }

    public T southWestOr(int rowIndex, int colIndex, PositionAwareFactory<T> factory) {
        return directionOr(rowIndex, colIndex, 1, -1, factory);
    }

    public T north(int rowIndex, int colIndex) {
        if (rowIndex > 0) {
            return array[rowIndex - 1][colIndex];
        }
        throw new IndexOutOfBoundsException();
    }

    public T east(int rowIndex, int colIndex) {
        if (colIndex < array[0].length) {
            return array[rowIndex][colIndex + 1];
        }
        throw new IndexOutOfBoundsException();
    }

    public T south(int rowIndex, int colIndex) {
        if (rowIndex < array.length) {
            return array[rowIndex + 1][colIndex];
        }
        throw new IndexOutOfBoundsException();
    }

    public T west(int rowIndex, int colIndex) {
        if (colIndex > 0) {
            return array[rowIndex][colIndex - 1];
        }
        throw new IndexOutOfBoundsException();
    }

    public T northWest(int rowIndex, int colIndex) {
        if (rowIndex > 0 && colIndex > 0) {
            return array[rowIndex - 1][colIndex - 1];
        }
        throw new IndexOutOfBoundsException();
    }

    public T northEast(int rowIndex, int colIndex) {
        if (rowIndex > 0 && colIndex < array[0].length) {
            return array[rowIndex - 1][colIndex + 1];
        }
        throw new IndexOutOfBoundsException();
    }

    public T southWest(int rowIndex, int colIndex) {
        if (rowIndex < array.length && colIndex > 0) {
            return array[rowIndex + 1][colIndex - 1];
        }
        throw new IndexOutOfBoundsException();
    }

    public T southEast(int rowIndex, int colIndex) {
        if (rowIndex < array.length && colIndex < array[0].length) {
            return array[rowIndex + 1][colIndex + 1];
        }
        throw new IndexOutOfBoundsException();
    }

    public List<T> allNorthWhile(int rowIndex, int colIndex, Predicate<T> condition) {
        return allInDirectionWhile(rowIndex, colIndex, this::north, -1, 0, condition);
    }

    public List<T> allEastWhile(int rowIndex, int colIndex, Predicate<T> condition) {
        return allInDirectionWhile(rowIndex, colIndex, this::east, 0, 1, condition);
    }

    public List<T> allSouthWhile(int rowIndex, int colIndex, Predicate<T> condition) {
        return allInDirectionWhile(rowIndex, colIndex, this::south, 1, 0, condition);
    }

    public List<T> allWestWhile(int rowIndex, int colIndex, Predicate<T> condition) {
        return allInDirectionWhile(rowIndex, colIndex, this::west, 0, -1, condition);
    }

    public List<T> allNorth(int rowIndex, int colIndex) {
        return allInDirection(rowIndex, colIndex, this::north, -1, 0);
    }

    public List<T> allEast(int rowIndex, int colIndex) {
        return allInDirection(rowIndex, colIndex, this::east, 0, 1);
    }

    public List<T> allSouth(int rowIndex, int colIndex) {
        return allInDirection(rowIndex, colIndex, this::south, 1, 0);
    }

    public List<T> allWest(int rowIndex, int colIndex) {
        return allInDirection(rowIndex, colIndex, this::west, 0, -1);
    }

    public List<T> allNorthEast(int rowIndex, int colIndex) {
        return allInDirection(rowIndex, colIndex, this::northEast, -1, 1);
    }

    public List<T> allNorthWest(int rowIndex, int colIndex) {
        return allInDirection(rowIndex, colIndex, this::northWest, -1, -1);
    }

    public List<T> allSouthEast(int rowIndex, int colIndex) {
        return allInDirection(rowIndex, colIndex, this::southEast, 1, 1);
    }

    public List<T> allSouthWest(int rowIndex, int colIndex) {
        return allInDirection(rowIndex, colIndex, this::southWest, 1, -1);
    }

    public Optional<T> findFirst(final Predicate<T> condition) {
        for (T[] ts : array) {
            for (int j = 0; j < array[0].length; j++) {
                if (condition.test(ts[j])) {
                    return Optional.of(ts[j]);
                }
            }
        }
        return Optional.empty();
    }

    public ArrayUtils<T> clone(final Function<T, T> mapper) {
        T[][] newArray = (T[][]) new Object[array.length][array[0].length];
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[0].length; j++) {
                newArray[i][j] = mapper.apply(array[i][j]);
            }
        }
        return new ArrayUtils<>(newArray);
    }

    public boolean hasPosition(final int row, final int col) {
        return row >= 0 && row < array.length && col >= 0 && col < array[0].length;
    }

    private T directionOrNull(int rowIndex, int colIndex, BiFunction<Integer, Integer, T> directionHelper) {
        try {
            return directionHelper.apply(rowIndex, colIndex);
        } catch (IndexOutOfBoundsException ex) {
            return null;
        }
    }

    private T directionOr(int rowIndex, int colIndex, int rowIncrement, int colIncrement, PositionAwareFactory<T> factory) {
        try {
            return get(rowIndex + rowIncrement, colIndex + colIncrement);
        } catch (IndexOutOfBoundsException ex) {
            return factory.create(rowIndex + rowIncrement, colIndex + colIncrement, null);
        }
    }

    private List<T> allInDirectionWhile(int rowIndex, int colIndex, BiFunction<Integer, Integer, T> directionHelper, int rowIncrement, int seatIncrement, Predicate<T> condition) {
        List<T> toReturn = new ArrayList<>();
        T item;
        boolean inGrid = true;
        int row = rowIndex;
        int seat = colIndex;

        while (inGrid) {
            try {
                item = directionHelper.apply(row, seat);
                if (item != null && condition.test(item)) {
                    toReturn.add(item);
                } else {
                    inGrid = false;
                }
                row += rowIncrement;
                seat += seatIncrement;
            } catch (IndexOutOfBoundsException ex) {
                inGrid = false;
            }
        }
        return toReturn;
    }

    private List<T> allInDirection(int rowIndex, int colIndex, BiFunction<Integer, Integer, T> directionHelper, int rowIncrement, int seatIncrement) {
        List<T> toReturn = new ArrayList<>();
        T item;
        boolean inGrid = true;
        int row = rowIndex;
        int seat = colIndex;

        while (inGrid) {
            try {
                item = directionHelper.apply(row, seat);
                if (item != null) {
                    toReturn.add(item);
                }
                row += rowIncrement;
                seat += seatIncrement;
            } catch (IndexOutOfBoundsException ex) {
                inGrid = false;
            }
        }
        return toReturn;
    }

    public Stream<T> cells() {
        return Stream.of(array).flatMap(Stream::of);
    }

    public void print() {
        for (T[] ts : array) {
            for (int j = 0; j < array[0].length; j++) {
                System.out.print(ts[j]);
            }
            System.out.println();
        }
    }

    public List<T> nesw(int rowIndex, int colIndex) {
        return Stream.of(
                northOrNull(rowIndex, colIndex),
                eastOrNull(rowIndex, colIndex),
                southOrNull(rowIndex, colIndex),
                westOrNull(rowIndex, colIndex)
        ).filter(Objects::nonNull).toList();
    }

    public List<T> neswGrowingOutsideMap(int rowIndex, int colIndex, PositionAwareFactory<T> factory) {
        return Stream.of(
                northOr(rowIndex, colIndex, factory),
                eastOr(rowIndex, colIndex, factory),
                southOr(rowIndex, colIndex, factory),
                westOr(rowIndex, colIndex, factory)
        ).toList();
    }

    public IntStream rowIndices() {
        return IntStream.range(0, array.length);
    }

    public IntStream colIndices() {
        return IntStream.range(0, array[0].length);
    }

    public List<Pair<Integer, Integer>> allCoordinates() {
        List<Pair<Integer, Integer>> coordinates = new ArrayList<>();
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[0].length; j++) {
                coordinates.add(Pair.of(i, j));
            }
        }
        return coordinates;
    }

    public interface PositionAwareFactory<T> {
        T create(int row, int col, Character value);
    }
}
