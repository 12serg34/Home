package com.home.stepic.algorithm;

import java.util.ArrayList;
import java.util.Scanner;

public class ContinuousBackpack {

    public static void main(String[] args) {
        new ContinuousBackpack().run();
    }

    public void run() {
        Scanner inputConsole = new Scanner(System.in);
        int count = inputConsole.nextInt();
        int capacity = inputConsole.nextInt();

        ArrayList<Thing> things = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            Thing thing = new Thing();
            thing.setCost(inputConsole.nextInt());
            thing.setVolume(inputConsole.nextInt());
            things.add(thing);
        }

        things.sort((o1, o2) -> (int)Math.signum(o2.getRelativeCost() - o1.getRelativeCost()));

        double costSum = 0.0f;
        double volumeFree = capacity;
        for (Thing thing : things) {
            if (volumeFree >= thing.getVolume()) {
                volumeFree -= thing.getVolume();
                costSum += thing.getCost();
            } else {
                costSum += volumeFree * thing.getRelativeCost();
                break;
            }
        }

        System.out.printf("%1.3f", costSum);
    }
}