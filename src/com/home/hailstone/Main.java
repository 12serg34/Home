package com.home.hailstone;

import java.util.ArrayList;
import java.util.Collection;

public class Main {
    public static void main(String[] args){
        new Main().run();
    }

    private void run(){
        int value = 27;
        printValue(value);
        while (value != 1){
            printValue(value = step(value));
        }
    }

    private void printValue(int value){
        System.out.printf("%4s: %12s %20s\n", value, binaryToString(toBinary(value)), countsToString(toCounts(value)));
    }

    private String binaryToString(Collection<Boolean> value){
        StringBuilder string = new StringBuilder();
        for (boolean bit: value){
            string.insert(0, bit? "1": "0");
        }
        return string.toString();
    }

    private String countsToString(ArrayList<Integer> counts){
        StringBuilder string = new StringBuilder();
        for (int count: counts){
            string.insert(0, count + " ");
        }
        return string.toString();
    }

    private ArrayList<Boolean> toBinary(int value){
        ArrayList<Boolean> binary = new ArrayList<>();
        binary.add(value % 2 == 1);
        while ((value >>= 1) > 0){
            binary.add(value % 2 == 1);
        }
        return binary;
    }

    private ArrayList<Integer> toCounts(int value){
        ArrayList<Boolean> binary = toBinary(value);
        ArrayList<Integer> counts = new ArrayList<>();
        boolean lastBit = true;
        int count = 0;
        for (boolean bit: binary){
            if (bit != lastBit){
                counts.add(count);
                lastBit = bit;
                count = 1;
            }
            else {
                count++;
            }
        }
        counts.add(count);
        return counts;
    }

    private int cut(int value){
        while (value % 2 == 0){
            value >>= 1;
        }
        return value;
    }

    private int step(int value){
        return cut(3 * value + 1);
    }
}
