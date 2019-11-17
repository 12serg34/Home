package com.home.hailstone.math;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.IntFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.home.hailstone.math.TestUtils.testFunction;
import static com.home.hailstone.math.TestUtils.testOrdering;

public class Util {

    public static int gamma(int x) {
        return x % 3 == 2 ? 1 : 0;
    }

    public static int cycle(int x, int... cycle) {
        return cycle[x % cycle.length];
    }

    public static int mod2(int x) {
        return x % 2;
    }

    public static int invertMod2(int x) {
        return 1 - mod2(x);
    }

    public static int mod3(int x) {
        return x % 3;
    }

    public static int forward1(int x) {
        return 2 * x + 1;
    }

    public static int reverse1(int x) {
        assert mod2(x) != 0;
        return (x - 1) / 2;
    }

    public static int forward2(int x) {
        return (3 * x + x % 2) / 2;
    }

    public static int reverse2(int x) {
        assert x > -1;
        assert mod3(x) != 1;
        return (4 * x - mod3(x)) / 6;
    }

    public static int powOf2(int x) {
        assert x > -1;
        return 1 << x;
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
                .collect(Collectors.toList());
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

    public static void splitAndPrint(int[] index, int numberToSplit) {
        List<List<Integer>> splitIndex = split(index, numberToSplit);
        for (int i = 0; i < numberToSplit; i++) {
            System.out.println("splitIndex" + i + ": " + splitIndex.get(i));
        }
        for (int i = 0; i < numberToSplit; i++) {
            System.out.println("diff of splitIndex" + i + ": " + diff(splitIndex.get(i)));
        }
    }

    private static List<Integer> diff(List<Integer> list) {
        List<Integer> diff = new ArrayList<>();
        for (int i = 0; i < list.size() - 1; i++) {
            diff.add(list.get(i + 1) - list.get(i));
        }
        return diff;
    }
}
