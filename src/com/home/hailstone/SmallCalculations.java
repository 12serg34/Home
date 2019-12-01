package com.home.hailstone;

import java.util.Arrays;
import java.util.stream.IntStream;

public class SmallCalculations {
    public static void main(String[] args) {
        new SmallCalculations().run();
    }

    private void run() {
        int[] index = IntStream.range(0, 18).toArray();
        System.out.println(Arrays.toString(index));
        System.out.println(Arrays.toString(Arrays.stream(index)
                .map(x -> (x * x) % 18)
                .toArray()));
        //4, 14

        System.out.println(Arrays.toString(IntStream.of(4, 14)
                .map(x -> (x * x - 16) % 18)
                .toArray()));
    }
}
