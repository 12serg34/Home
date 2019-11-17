package com.home.hailstone.math;

import java.math.BigInteger;
import java.util.*;
import java.util.function.IntFunction;
import java.util.stream.IntStream;

import static com.home.hailstone.math.TestUtils.testFunction;
import static com.home.hailstone.math.TestUtils.testOrdering;
import static java.math.BigInteger.ONE;
import static java.math.BigInteger.ZERO;
import static java.util.stream.Collectors.*;

public class BigIntegerUtil {
    private static final BigInteger TWO = BigInteger.valueOf(2);
    public static final BigInteger THREE = BigInteger.valueOf(3);

    public static int gamma(int x) {
        return x % 3 == 2 ? 1 : 0;
    }

    public static int cycle(BigInteger x, int... cycle) {
        return cycle[x.mod(BigInteger.valueOf(cycle.length)).intValue()];
    }

    private static BigInteger mod2(BigInteger x) {
        return x.mod(TWO);
    }

    public static BigInteger invertMod2(BigInteger x) {
        return ONE.subtract(mod2(x));
    }

    private static BigInteger mod3(BigInteger x) {
        return x.mod(THREE);
    }

    public static BigInteger forward1(BigInteger x) {
        return TWO.multiply(x).add(ONE);
    }

    public static BigInteger reverse1(BigInteger x) {
        assert !mod2(x).equals(ZERO);
        return x.subtract(ONE).divide(TWO);
    }

    public static BigInteger forward2(BigInteger x) {
        return (x.multiply(THREE)
                .add(mod2(x))
        ).divide(TWO);
    }

    public static BigInteger reverse2(BigInteger x) {
//        assert x > -1;
//        assert mod3(x) != 1;
        return x.multiply(BigInteger.valueOf(4))
                .subtract(mod3(x))
                .divide(BigInteger.valueOf(6));
    }

    public static BigInteger powOf2(int x) {
        assert x > -1;
        return TWO.pow(x);
    }

    public static IntFunction<Integer> max(List<IntFunction<Integer>> functions) {
        return x -> {
            int maxValue = Integer.MIN_VALUE;
            for (IntFunction<Integer> function : functions) {
                maxValue = Math.max(maxValue, function.apply(x));
            }
            return maxValue;
        };
    }

    public static void buildSortIndexFunction(IntFunction<Integer> function, IntFunction<Integer> guessIndex, int size,
                                              int numberToSplit) {
        int[] index = IntStream.range(0, size).toArray();
        int[] appliedFunction = applyFunction(function, index);
        List<Item> sortedItems = sort(function, index);
        int[] sortedFunction = Item.extractValues(sortedItems);
        int[] sortedIndex = Item.extractIndexes(sortedItems);
        List<List<Integer>> splitIndex = split(sortedIndex, numberToSplit);
        System.out.println("index: " + Arrays.toString(index));
        System.out.println("function: " + Arrays.toString(appliedFunction));
        System.out.println("sorted function: " + Arrays.toString(sortedFunction));
        System.out.println("sorted index: " + Arrays.toString(sortedIndex));
        for (int i = 0; i < numberToSplit; i++) {
            System.out.println("splitIndex" + i + ": " + splitIndex.get(i));
        }
        System.out.println("tests results: \nfunction from guess of sorted index is ordered: " +
                testOrdering(x -> function.apply(guessIndex.apply(x)), size));
        System.out.println("guess of sorted index equals sorted index: " + testFunction(sortedIndex, guessIndex));
    }

    private static int[] applyFunction(IntFunction<Integer> function, int[] x) {
        return Arrays.stream(x)
                .map(function::apply)
                .toArray();
    }

    private static List<Item> sort(IntFunction<Integer> function, int[] index) {
        return Arrays.stream(index)
                .mapToObj(x -> new Item(x, function.apply(x)))
                .sorted(Comparator.comparing(Item::getValue))
                .collect(toList());
    }

    private static List<List<Integer>> split(int[] index, int numberToSplit) {
        List<List<Integer>> splitIndex = new ArrayList<>(numberToSplit);
        for (int i = 0; i < numberToSplit; i++) {
            splitIndex.add(new ArrayList<>());
        }
        for (int i = 0; i < index.length; i++) {
            splitIndex.get(i % numberToSplit).add(index[i]);
        }
        return splitIndex;
    }

    public static Map<Integer, List<Integer>> groupByValues(int[] values) {
        return IntStream.range(0, values.length)
                .mapToObj(i -> new Item(i, values[i]))
                .collect(groupingBy(Item::getValue, mapping(Item::getIndex, toList())));
    }
}
