package org.jenson.y2024.d17;

import org.jenson.utils.Reader;

import java.util.ArrayList;
import java.util.List;

import static org.jenson.utils.Printer.printDay;

public class Part2 {

    public static void main(String[] args) {
        printDay(17);

        long registerA = -1, registerB = -1, registerC = -1;
        String instructions = "";
        List<String> list = Reader.readFileLineByLineToText(2024, 17).toList();
        for (String s : list) {
            if (s.startsWith("Register")) {
                if (s.contains("A:")) {
                    registerA = Long.parseLong(s.substring(12));
                } else if (s.contains("B:")) {
                    registerB = Long.parseLong(s.substring(12));
                } else if (s.contains("C:")) {
                    registerC = Long.parseLong(s.substring(12));
                }
            }
            if (s.startsWith("Program: ")) {
                instructions = s.substring(9).replaceAll(",", "");
            }
        }

        if (registerA == -1 || registerB == -1 || registerC == -1 || instructions.isEmpty()) {
            throw new IllegalArgumentException("Invalid input");
        }

        Computer computer = new Computer(registerA, registerB, registerC, instructions);
        System.out.println(computer.reverseEngineer(1, 0L));
    }

    private static class Computer {

        private long registerA, registerB, registerC;
        private final String instructions;

        public Computer(long registerA, long registerB, long registerC, String instructions) {
            this.registerA = registerA;
            this.registerB = registerB;
            this.registerC = registerC;
            this.instructions = instructions;
        }

        long reverseEngineer(int currentDigit, long solvedDigits) {
            // Shift to the left in base-8
            long foundDigits = solvedDigits * 8;

            // Iterate through possible octal digits (0 to 7)
            for (long i = 0; i < 8; i++) {
                long candidate = foundDigits + i; // Append next octal digit
                List<Long> result = execute(candidate);

                if (compareSuffix(result, currentDigit)) {
                    if (currentDigit == instructions.length()) {
                        return candidate; // Solution found
                    }

                    long ret = reverseEngineer(currentDigit + 1, candidate);
                    if (ret != -1) {
                        return ret; // Return if solution is found
                    }
                }
            }
            return -1; // No solution at this branch
        }

        List<Long> execute(long candidate) {
            this.registerA = candidate;
            int i = 0;
            List<Long> values = new ArrayList<>();
            while (i < instructions.length()) {
                int opcode = instructions.charAt(i) - '0';
                switch (opcode) {
                    //Dividing a number 𝑥 by 2^𝑛 is equivalent to performing a right shift by n bits
                    case 0 -> registerA >>= getOperand(instructions.charAt(i + 1) - '0');
                    //Bitwise XOR
                    case 1 -> registerB ^= instructions.charAt(i + 1) - '0';
                    // 7 is 0111 in binary, which acts as the mask to isolate the last 3 bits.
                    case 2 -> registerB = getOperand(instructions.charAt(i + 1) - '0') & 0x7;
                    case 3 -> {
                        if (registerA != 0) {
                            // jumps by setting the instruction pointer
                            i = (instructions.charAt(i + 1) - '0') - 2; // - 2 -> if this instruction jumps, the instruction pointer is not increased by 2 after this instruction.
                        }
                    }
                    //Bitwise XOR
                    case 4 -> registerB ^= registerC;
                    // 7 is 0111 in binary, which acts as the mask to isolate the last 3 bits.
                    case 5 -> values.add(getOperand(instructions.charAt(i + 1) - '0') & 0x7);
                    //Dividing a number 𝑥 by 2^𝑛 is equivalent to performing a right shift by n bits
                    case 6 -> registerB = registerA >> getOperand(instructions.charAt(i + 1) - '0');
                    //Dividing a number 𝑥 by 2^𝑛 is equivalent to performing a right shift by n bits
                    case 7 -> registerC = registerA >> getOperand(instructions.charAt(i + 1) - '0');
                }
                i += 2;
            }
            return values;
        }

        long getOperand(int operand) {
            return switch (operand) {
                case 4 -> registerA;
                case 5 -> registerB;
                case 6 -> registerC;
                case 7 -> throw new RuntimeException("Not allowed");
                default -> operand;
            };
        }


        // Compare the last 'currentDigit' elements of result and program
        private boolean compareSuffix(List<Long> result, int currentDigit) {
            if (currentDigit > instructions.length() || currentDigit > result.size()) {
                return false;
            }

            for (int i = 0; i < currentDigit; i++) {
                if (result.get(result.size() - 1 - i) != instructions.charAt(instructions.length() - 1 - i) - '0') {
                    return false;
                }
            }
            return true;
        }
    }
}
