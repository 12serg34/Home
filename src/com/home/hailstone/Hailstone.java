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
                x -> cut(3 * x + 1))
                .limit(100)
                .filter(x -> x > 1)
                .toArray();
        System.out.println(Arrays.toString(ints));
        System.out.println(ints.length);

        int[] ints1 = stepBackFromWithLimit(1, 20);
        System.out.println(Arrays.toString(ints1));
        System.out.println(ints1.length);

        int[] ints2 = step(1, 3, 1)
                .sorted()
                .toArray();
        System.out.println(Arrays.toString(ints2));
        System.out.println(ints2.length);
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

    private int stepBackFrom(int base, int shift) {
        int denominator = base * (1 << shift) - 1;
        if (denominator % 3 != 0) {
            return -1;
        }
        return denominator / 3;
    }

    private int[] stepBackFromWithLimit(int base, int limit) {
        return IntStream.range(0, limit)
                .map(x -> stepBackFrom(base, x))
                .filter(x -> x > 0)
                .toArray();
    }

    private IntStream step(int base, int limit, int level) {
        IntStream intStream = IntStream.range(0, 20)
                .map(x -> stepBackFrom(base, x))
                .filter(x -> x > 0);
        if (level == 0) {
            return intStream;
        }
        return intStream.flatMap(x -> step(x, limit, level - 1));
    }
}
