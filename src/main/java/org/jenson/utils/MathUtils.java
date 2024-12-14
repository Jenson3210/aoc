package org.jenson.utils;

public class MathUtils {

    private MathUtils() {}

    public static long leastCommonMultiple(long a, long b) {
        if (a == 0 || b == 0)
            return 0;
        else {
            long gcd = greatestCommonDivisor(a, b);
            return Math.abs(a * b) / gcd;
        }
    }

    public static long greatestCommonDivisor(long a, long b) {
        if (a == 0 || b == 0) {
            return a + b;
        } else {
            long absNumber1 = Math.abs(a);
            long absNumber2 = Math.abs(b);
            long biggerValue = Math.max(absNumber1, absNumber2);
            long smallerValue = Math.min(absNumber1, absNumber2);
            return greatestCommonDivisor(biggerValue % smallerValue, smallerValue);
        }
    }
}
