package org.jenson.y2024.d13;

import org.jenson.utils.Reader;

import java.util.function.Function;
import java.util.stream.Gatherers;

import static org.jenson.utils.Printer.printDay;

public class Part1 {

    public static void main(String[] args) {
        printDay(12);

        int totalCost = Reader.readFileLineByLineToText(2024, 13).gather(Gatherers.windowFixed(4)).map(machineInput -> {
            String aInput = machineInput.get(0);
            String bInput = machineInput.get(1);
            String prizeInput = machineInput.get(2);
            Button a = new Button(Integer.parseInt(aInput.substring(aInput.indexOf("X+") + 2, aInput.indexOf(","))),
                    Integer.parseInt(aInput.substring(aInput.indexOf("Y+") + 2)), 3);
            Button b = new Button(Integer.parseInt(bInput.substring(bInput.indexOf("X+") + 2, bInput.indexOf(","))),
                    Integer.parseInt(bInput.substring(bInput.indexOf("Y+") + 2)), 1);
            Prize prize = new Prize(Integer.parseInt(prizeInput.substring(prizeInput.indexOf("X=") + 2, prizeInput.indexOf(","))),
                    Integer.parseInt(prizeInput.substring(prizeInput.indexOf("Y=") + 2)));
            return new SlotMachine(a, b, prize);
        }).mapToInt(SlotMachine::getMinimalCost).sum();

        System.out.println(totalCost);
    }

    private record SlotMachine(Button A, Button B, Prize prize) {
        public int getMinimalCost() {
            int furthestCoordinate = Math.max(Math.abs(prize.x), Math.abs(prize.y));
            int closestCoordinate = Math.min(Math.abs(prize.x), Math.abs(prize.y));
            Function<Button, Integer> biggestStepDirection = prize.x > prize.y ? Button::xPlus : Button::yPlus;
            Function<Button, Integer> otherStepDirection = prize.x > prize.y ? Button::yPlus : Button::xPlus;
            Button biggestStep = biggestStepDirection.apply(A) * A.cost > biggestStepDirection.apply(B) * B.cost ? A : B;
            Button otherStep = biggestStep == A ? B : A;

            int maxSteps = furthestCoordinate / biggestStepDirection.apply(biggestStep);

            if (maxSteps > 100) {
                maxSteps = 100;
            }
            for (int steps = maxSteps; steps > 0; steps--) {
                int remainderFurthestCoordinate = furthestCoordinate - (steps * biggestStepDirection.apply(biggestStep));
                int remainderClosestCoordinate = closestCoordinate - (steps * otherStepDirection.apply(biggestStep));
                if (remainderFurthestCoordinate % biggestStepDirection.apply(otherStep) == 0 && remainderClosestCoordinate % otherStepDirection.apply(otherStep) == 0) {
                    int smallestOtherStep = Math.min(remainderFurthestCoordinate / biggestStepDirection.apply(otherStep), remainderClosestCoordinate / otherStepDirection.apply(otherStep));
                    int biggestOtherStep = Math.max(remainderFurthestCoordinate / biggestStepDirection.apply(otherStep), remainderClosestCoordinate / otherStepDirection.apply(otherStep));

                    if (biggestOtherStep > 100 || biggestOtherStep % smallestOtherStep != 0) {
                        continue;
                    }
                    if (biggestStep == A) {
                        return new PushResult(A, steps, B, biggestOtherStep).cost();
                    } else {
                        return new PushResult(A, biggestOtherStep, B, steps).cost();
                    }
                }
            }

            return 0;
        }
    }

    private record PushResult(Button a, int aCount, Button b, int bCount) {
        int cost() {
            return (a.cost * aCount) + (b.cost * bCount);
        }

        void print() {
            System.out.println("result: (%s, %s)".formatted(aCount, bCount));
        }
    }

    private record Button(int xPlus, int yPlus, int cost) {}

    private record Prize(int x, int y) {}
}
