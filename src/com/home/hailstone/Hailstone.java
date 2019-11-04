package com.home.hailstone;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class Hailstone {

    @Test
    void test1() {
        IntStream.iterate(2, x -> 3 * x)
                .limit(10)
                .forEach(x -> System.out.print(x + ", "));
        System.out.println();
        IntStream.iterate(4, x -> x * x *x)
                .limit(5)
                .forEach(x -> System.out.print(x + ", "));
    }

    @Test
    void test2() {
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

    @Test
    void test3() {
        int limit = 15;
        Set<Item> items = new TreeSet<>(Comparator.comparing(item -> item.value));
        for (int p1 = 0; p1 < limit; p1++) {
            for (int p2 = 0; p2 < limit; p2++) {
                Item item = new Item();
                item.p1 = p1;
                item.p2 = p2;
                calculateValue2(item);
                items.add(item);
            }
        }
        System.out.println(items);
    }

    @Test
    void test4(String[] args) {
        IntStream.range(0, 100)
                .map(v -> (int) Math.sqrt(v))
                .boxed()
                .collect(Collectors.groupingBy(Integer::intValue))
                .forEach((k, v) -> System.out.println(v.size()));
    }

    @Test
    void test5() {
        IntStream.range(0, 100)
                .map(v -> {
                    int n = (int) ((Math.sqrt(1 + 8 * v) - 1) / 2);
                    return v - n * (n + 1) / 2;
                })
                .forEach(v -> System.out.print(v + " "));
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

    class Item {
        int p1;
        int p2;
        int value;

        @Override
        public String toString() {
            return "(" + p1 + ", " + p2 + ")";
        }
    }

    private void calculateValue2(Item item) {
        int a = 1 << item.p1;
        int b = 1 << item.p2;
        item.value = a * b + 3 * b + 9;
    }
}

