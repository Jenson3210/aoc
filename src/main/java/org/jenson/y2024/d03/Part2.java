package org.jenson.y2024.d03;

import org.jenson.utils.InputValidator;
import org.jenson.utils.Reader;

import static org.jenson.utils.Printer.printDay;

public class Part2 {

    public static void main(String[] args) {
        printDay(3);

        String memory = Reader.readFileToText(2024, 3);
        Long sum = 0L;
        boolean summing = true;

        // if we still find mul([0-9]+,[0-9]+) like operations in memory, we will continue to evaluate them
        while(memory.contains("mul(")) {
            // find the first occurrence of mul
            int mulIndex = memory.indexOf("mul(");
            String stillEnabledCheck = memory.substring(0, mulIndex);
            if (stillEnabledCheck.contains("do()") || stillEnabledCheck.contains("don't()")) {
                int doIndex = stillEnabledCheck.lastIndexOf("do()");
                int dontIndex = stillEnabledCheck.lastIndexOf("don't()");
                if (doIndex > dontIndex) {
                    summing = true;
                } else {
                    summing = false;
                }
            }

            memory = memory.substring(mulIndex + 4);

            // find the closing parenthesis of the mul operation
            int closingParenthesisIndex = memory.indexOf(")");
            if (closingParenthesisIndex == -1) {
                break;
            }
            String[] split = memory.substring(0, closingParenthesisIndex).split(",");
            if (split.length != 2 || !(InputValidator.isNumeric(split[0]) && InputValidator.isNumeric(split[1]))) {
                continue;
            }

            if (summing) {
                sum += Long.parseLong(split[0]) * Long.parseLong(split[1]);
            }
            memory = memory.substring(closingParenthesisIndex + 1);
        }

        System.out.println(sum);
    }
}
