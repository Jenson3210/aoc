package org.jenson.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

public class ArrayUtils<T> {

    T [][] array;

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

    public int width() {
        return array[0].length;
    }

    public int height() {
        return array.length;
    }

    public T get(int i, int j) {
        return array[i][j];
    }

    public T northOrNull(int rowIndex, int seatIndex) {
        return directionOrNull(rowIndex, seatIndex, this::north);
    }

    public T eastOrNull(int rowIndex, int seatIndex) {
        return directionOrNull(rowIndex, seatIndex, this::east);
    }

    public T southOrNull(int rowIndex, int seatIndex) {
        return directionOrNull(rowIndex, seatIndex, this::south);
    }

    public T westOrNull(int rowIndex, int seatIndex) {
        return directionOrNull(rowIndex, seatIndex, this::west);
    }

    public T northEastOrNull(int rowIndex, int seatIndex) {
        return directionOrNull(rowIndex, seatIndex, this::northEast);
    }

    public T northWestOrNull(int rowIndex, int seatIndex) {
        return directionOrNull(rowIndex, seatIndex, this::northWest);
    }

    public T southEastOrNull(int rowIndex, int seatIndex) {
        return directionOrNull(rowIndex, seatIndex, this::southEast);
    }

    public T southWestOrNull(int rowIndex, int seatIndex) {
        return directionOrNull(rowIndex, seatIndex, this::southWest);
    }

    public T north(int rowIndex, int seatIndex) {
        if (rowIndex > 0) {
            return array[rowIndex - 1][seatIndex];
        }
        throw new IndexOutOfBoundsException();
    }

    public T east(int rowIndex, int seatIndex) {
        if (seatIndex < array[0].length) {
            return array[rowIndex][seatIndex + 1];
        }
        throw new IndexOutOfBoundsException();
    }

    public T south(int rowIndex, int seatIndex) {
        if (rowIndex < array.length) {
            return array[rowIndex + 1][seatIndex];
        }
        throw new IndexOutOfBoundsException();
    }

    public T west(int rowIndex, int seatIndex) {
        if (seatIndex > 0) {
            return array[rowIndex][seatIndex - 1];
        }
        throw new IndexOutOfBoundsException();
    }

    public T northWest(int rowIndex, int seatIndex) {
        if (rowIndex > 0 && seatIndex > 0) {
            return array[rowIndex - 1][seatIndex -1];
        }
        throw new IndexOutOfBoundsException();
    }

    public T northEast(int rowIndex, int seatIndex) {
        if (rowIndex > 0 && seatIndex < array[0].length) {
            return array[rowIndex - 1][seatIndex + 1];
        }
        throw new IndexOutOfBoundsException();
    }

    public T southWest(int rowIndex, int seatIndex) {
        if (rowIndex < array.length && seatIndex > 0) {
            return array[rowIndex + 1][seatIndex -1];
        }
        throw new IndexOutOfBoundsException();
    }

    public T southEast(int rowIndex, int seatIndex) {
        if (rowIndex < array.length && seatIndex < array[0].length) {
            return array[rowIndex + 1][seatIndex + 1];
        }
        throw new IndexOutOfBoundsException();
    }

    public List<T> allNorth(int rowIndex, int seatIndex) {
        return allInDirection(rowIndex, seatIndex, this::north, -1, 0);
    }

    public List<T> allEast(int rowIndex, int seatIndex) {
        return allInDirection(rowIndex, seatIndex, this::east, 0, 1);
    }

    public List<T> allSouth(int rowIndex, int seatIndex) {
        return allInDirection(rowIndex, seatIndex, this::south, 1, 0);
    }

    public List<T> allWest(int rowIndex, int seatIndex) {
        return allInDirection(rowIndex, seatIndex, this::west, 0, -1);
    }

    public List<T> allNorthEast(int rowIndex, int seatIndex) {
        return allInDirection(rowIndex, seatIndex, this::northEast, -1, 1);
    }

    public List<T> allNorthWest(int rowIndex, int seatIndex) {
        return allInDirection(rowIndex, seatIndex, this::northWest, -1, -1);
    }

    public List<T> allSouthEast(int rowIndex, int seatIndex) {
        return allInDirection(rowIndex, seatIndex, this::southEast, 1, 1);
    }

    public List<T> allSouthWest(int rowIndex, int seatIndex) {
        return allInDirection(rowIndex, seatIndex, this::southWest, 1, -1);
    }

    private T directionOrNull(int rowIndex, int seatIndex, BiFunction<Integer, Integer, T> directionHelper) {
        try {
            return directionHelper.apply(rowIndex, seatIndex);
        } catch (IndexOutOfBoundsException ex) {
            return null;
        }
    }

    private List<T> allInDirection(int rowIndex, int seatIndex, BiFunction<Integer, Integer, T> directionHelper, int rowIncrement, int seatIncrement) {
        List<T> toReturn = new ArrayList<>();
        T item;
        boolean inGrid = true;
        int row = rowIndex;
        int seat = seatIndex;

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
}