package org.jenson.utils;

public class InputValidator {

    private InputValidator() {}

    public static boolean isNumeric(String str) {
        return str.matches("-?\\d+(\\.\\d+)?");
    }
}
