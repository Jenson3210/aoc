package org.jenson.y2024.d17;

import org.jenson.utils.Reader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.jenson.utils.Printer.printDay;

public class Part1 {

    public static void main(String[] args) {
        printDay(17);

        long registerA = -1, registerB = -1, registerC = -1;
        List<Integer> instructions = List.of();
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
                instructions = Arrays.stream(s.substring(9).split(",")).map(Integer::valueOf).toList();
            }
        }

        if (registerA == -1 || registerB == -1 || registerC == -1 || instructions.isEmpty()) {
            throw new IllegalArgumentException("Invalid input");
        }

        Computer computer = new Computer(registerA, registerB, registerC, instructions);
        System.out.println(computer.execute().stream().map(String::valueOf).collect(Collectors.joining(",")));
    }

    private static class Computer {

        private long registerA, registerB, registerC;
        private final List<Integer> instructions;

        public Computer(long registerA, long registerB, long registerC, List<Integer> instructions) {
            this.registerA = registerA;
            this.registerB = registerB;
            this.registerC = registerC;
            this.instructions = instructions;
        }

        List<Long> execute() {
            int i = 0;
            List<Long> values = new ArrayList<>();
            while (i < instructions.size()) {
                int opcode = instructions.get(i);
                switch (opcode) {
                              //Dividing a number 𝑥 by 2^𝑛 is equivalent to performing a right shift by n bits
                    case 0 -> registerA >>= getOperand(instructions.get(i + 1));
                              //Bitwise XOR
                    case 1 -> registerB ^= instructions.get(i + 1);
                    case 2 -> registerB = getOperand(instructions.get(i + 1)) & 0x7;
                    case 3 -> {
                        if (registerA != 0) {
                            // jumps by setting the instruction pointer
                            i = instructions.get(i + 1) - 2; // - 2 -> if this instruction jumps, the instruction pointer is not increased by 2 after this instruction.
                        }
                    }
                              //Bitwise XOR
                    case 4 -> registerB ^= registerC;
                              // 7 is 0111 in binary, which acts as the mask to isolate the last 3 bits.
                    case 5 -> values.add(getOperand(instructions.get(i + 1)) & 0x7);
                              //Dividing a number 𝑥 by 2^𝑛 is equivalent to performing a right shift by n bits
                    case 6 -> registerB = registerA >> getOperand(instructions.get(i + 1));
                              //Dividing a number 𝑥 by 2^𝑛 is equivalent to performing a right shift by n bits
                    case 7 -> registerC = registerA >> getOperand(instructions.get(i + 1));
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
    }
}
