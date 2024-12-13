package org.jenson.y2024.d13;

import org.jenson.utils.Reader;

import java.util.stream.Gatherers;

import static org.jenson.utils.Printer.printDay;

public class Part2 {

    public static void main(String[] args) {
        printDay(12);

        long totalCost = Reader.readFileLineByLineToText(2024, 13).gather(Gatherers.windowFixed(4)).map(machineInput -> {
            String aInput = machineInput.get(0);
            String bInput = machineInput.get(1);
            String prizeInput = machineInput.get(2);
            Button a = new Button(Integer.parseInt(aInput.substring(aInput.indexOf("X+") + 2, aInput.indexOf(","))),
                    Integer.parseInt(aInput.substring(aInput.indexOf("Y+") + 2)), 3);
            Button b = new Button(Integer.parseInt(bInput.substring(bInput.indexOf("X+") + 2, bInput.indexOf(","))),
                    Integer.parseInt(bInput.substring(bInput.indexOf("Y+") + 2)), 1);
            Prize prize = new Prize(10000000000000L + Long.parseLong(prizeInput.substring(prizeInput.indexOf("X=") + 2, prizeInput.indexOf(","))),
                    10000000000000L + Long.parseLong(prizeInput.substring(prizeInput.indexOf("Y=") + 2)));
            return new SlotMachine(a, b, prize);
        }).parallel().mapToLong(SlotMachine::getMinimalCost).sum();

        System.out.println(totalCost);
    }

    private record SlotMachine(Button A, Button B, Prize prize) {
        public long getMinimalCost() {
            // https://forums.codeguru.com/showthread.php?459814-RESOLVED-Algorithm-for-solving-2-equations-2-unknowns
            //a * Ax + b * Bx = prize.x
            //a * Ay + b * By = prize.y
            // => Ay*a * Ay*Ax + Ay*b * Ay*Bx = Ay*prize.x
            // => Ax*a * Ax*Ay + Ax*b * Ax*By = Ax*prize.y
            // ==> (Ay*b * Ay*Bx) - (Ax*b * Ax*By) = Ay*prize.x - Ax*prize.y
            // ==> b = (Ay*prize.x - Ax*prize.y) / (Ay*Bx - Ax*By)
            long b = (A.yPlus() * prize.x() - A.xPlus() * prize.y()) / ((long) A.yPlus() * B.xPlus() - (long) A.xPlus() * B.yPlus());
            long a = (prize.x() - b * B.xPlus()) / A.xPlus();
            if (a * A.yPlus() + b * B.yPlus() != prize.y()) {
                return 0;
            }
            if ((prize.x() - b * B.xPlus) % A.xPlus() != 0) {
                return 0;
            }
            return A.cost() * a + B.cost() * b;
        }
    }

    private record Button(int xPlus, int yPlus, int cost) {}

    private record Prize(long x, long y) {}
}
