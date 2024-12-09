package org.jenson.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

public class Reader {

    private Reader() {}

    static File readFile(int year, int day) {
        return new File(ClassLoader.getSystemResource(getFileName(year, day)).getFile());
    }

    public static String readFileToText(int year, int day) {
        return tryCatchOrThrow(() -> Files.readString(readFile(year, day).toPath()));
    }

    public static Stream<String> readFileLineByLineToText(int year, int day) {
        return tryCatchOrThrow(() -> Files.lines(readFile(year, day).toPath()));
    }

    public static IntStream readFileLineByLineToInt(int year, int day) {
        return readFileLineByLineToText(year, day).mapToInt(Integer::parseInt);
    }

    public static Stream<IntStream> readFileLineByLineSeparatedToIntStream(int year, int day, String separator) {
        return readFileLineByLineToText(year, day).map(line -> Arrays.stream(line.split(separator)).mapToInt(Integer::parseInt));
    }

    public static Stream<String> readFileLineCsvToText(int year, int day) {
        return Arrays.stream(readFileToText(year, day).split(","));
    }

    public static LongStream readFileLineCsvToLong(int year, int day) {
        return readFileLineCsvToText(year, day).mapToLong(Long::parseLong);
    }

    public static Stream<String> readFileLineToText(int year, int day) {
        return readFileToText(year, day).chars().mapToObj(c -> String.valueOf((char) c));
    }

    public static IntStream readFileLineToInt(int year, int day) {
        return readFileLineByLineSeparatedToIntStream(year, day, "").findFirst().orElseGet(IntStream::empty);
    }

    private static String getFileName(int year, int day) {
        return "%d/%02d.txt".formatted(year, day);
    }

    private interface ThrowsIOException<T> {
        T get() throws IOException;
    }

    private static <T> T tryCatchOrThrow(ThrowsIOException<T> supplier) {
        try {
            return supplier.get();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
