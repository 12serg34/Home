package com.home.hailstone;

import java.util.Arrays;
import java.util.stream.IntStream;

public class Hailstone {
    public static void main(String[] args) {
        new Hailstone().run();
    }

    private void run() {
        int[] ints = IntStream.iterate(
                7,
                x -> cut(3 * x  + 1))
                .limit(100)
                .filter(x -> x > 1)
                .toArray();
        System.out.println(Arrays.toString(ints));
    }

    private int cut(int x) {
        if (x == 0) {
            return 0;
        }
        if (x == 1) {
            return 1;
        }
        if (x % 2 == 0) {
            return cut(x >> 1);
        }
        return x;
    }
}
