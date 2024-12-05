package org.jenson.utils;

public class Printer {

    private Printer() {}

    public static void printDay(int day) {
        System.out.println();
        System.out.println("************************************");
        System.out.println("* %02d                               *".formatted(day));
        System.out.println("************************************");
    }
}
