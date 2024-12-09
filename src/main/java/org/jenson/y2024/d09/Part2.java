package org.jenson.y2024.d09;

import org.jenson.utils.Reader;

import java.util.LinkedList;

import static org.jenson.utils.Printer.printDay;

public class Part2 {

    public static void main(String[] args) {
        printDay(9);

        int[] fileSystem = Reader.readFileLineToInt(2024, 9).toArray();
        LinkedList<Block> blocks = new LinkedList<>();

        for (int i = 0; i < fileSystem.length; i++) {
            blocks.add(new Block(i / 2, fileSystem[i], i % 2 == 0));
        }

        for (int i = blocks.size() - 1; i >= 0; i--) {
            Block block = blocks.get(i);
            if (block.file() && blocks.subList(0, i).stream().anyMatch(b -> !b.file() && b.length() >= block.length())) {
                Block emptySpace = new Block(-1, -1, false);
                Block moved = blocks.set(i, emptySpace);
                emptySpace.setLength(moved.length());
                for (int beforeIndex = 0; beforeIndex < blocks.size(); beforeIndex++) {
                    Block before = blocks.get(beforeIndex);
                    if (!before.file() && before.length() >= moved.length()) {
                        blocks.add(beforeIndex, moved);
                        before.setLength(before.length() - moved.length());
                        if (before.length() == 0) {
                            blocks.remove(beforeIndex + 1);
                        }
                        break;
                    }
                }

            }
        }
        long checksum = 0;
        int index = 0;
        for (int i = index; i < blocks.size(); i++) {
            Block block = blocks.get(i);
            for (int j = 0; j < block.length(); j++) {
                if (block.file()) {
                    checksum += (long) index * block.value();
                }
                index++;
            }
        }
        System.out.println(checksum);
    }

    private static class Block {
        int value;
        int length;
        boolean file;

        public Block(int value, int length, boolean file) {
            this.value = value;
            this.length = length;
            this.file = file;
        }

        public int value() {
            return value;
        }

        public int length() {
            return length;
        }

        public boolean file() {
            return file;
        }

        public void setLength(int length) {
            this.length = length;
        }

        @Override
        public String toString() {
            return "Block{" + "value=" + value + ", length=" + length + ", file=" + file + '}';
        }
    }
}
