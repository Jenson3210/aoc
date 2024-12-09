package org.jenson.y2024.d09;

import org.jenson.utils.Reader;

import java.util.LinkedList;
import java.util.Objects;

import static org.jenson.utils.Printer.printDay;

public class Part1 {

    private static final Block EMPTY_BLOCK = new Block(-1, false);

    public static void main(String[] args) {
        printDay(9);

        int[] fileSystem = Reader.readFileLineToInt(2024, 9).toArray();
        LinkedList<Block> blocks = new LinkedList<>();

        for (int i = 0; i < fileSystem.length; i++) {
            boolean isFile = i % 2 == 0;
            for (int j = 0; j < fileSystem[i]; j++) {
                blocks.add(new Block(i / 2, isFile));
            }
        }

        while (blocks.contains(EMPTY_BLOCK)) {
            while (blocks.getLast().file() && blocks.contains(EMPTY_BLOCK)) {
                blocks.set(blocks.indexOf(EMPTY_BLOCK), blocks.pollLast());
            }
            while (!blocks.getLast().file()) {
                blocks.pollLast();
            }
        }

        long checksum = 0;
        for (int i = 0; i < blocks.size(); i++) {
            Block block = blocks.get(i);
            if (block.file()) {
                checksum += (long) i * block.value();
            }
        }
        System.out.println(checksum);
    }

    public record Block(int value, boolean file) {

        @Override
        public boolean equals(final Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            Block block = (Block) o;
            return file == block.file;
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(file);
        }
    }
}
