package com.home.hailstone.math;

import java.util.ArrayList;
import java.util.List;
import java.util.function.IntFunction;

public class Util {

    public static int gamma(int x) {
        return x % 3 == 2 ? 1 : 0;
    }

    public static int mod2(int x) {
        return x % 2;
    }

    public static int invertMod2(int x) {
        return 1 - mod2(x);
    }

    public static int forward1(int x) {
        return 2 * x + 1;
    }

    public static int reverse1(int x) {
        assert mod2(x) != 0;
        return (x - 1) / 2;
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

    public static List<List<Integer>> split(int[] index, int numberToSplit) {
        List<List<Integer>> splitIndex = new ArrayList<>(numberToSplit);
        for (int i = 0; i < numberToSplit; i++) {
            splitIndex.add(new ArrayList<>());
        }
        for (int i = 0; i < index.length; i++) {
            splitIndex.get(i % numberToSplit).add(index[i]);
        }
        return splitIndex;
    }

    public static List<List<Integer>> split(List<Integer> index, int numberToSplit) {
        List<List<Integer>> splitIndex = new ArrayList<>(numberToSplit);
        for (int i = 0; i < numberToSplit; i++) {
            splitIndex.add(new ArrayList<>());
        }
        for (int i = 0; i < index.size(); i++) {
            splitIndex.get(i % numberToSplit).add(index.get(i));
        }
        return splitIndex;
    }

    public static List<Integer> diff(List<Integer> list) {
        List<Integer> diff = new ArrayList<>(list.size() - 1);
        for (int i = 0; i < list.size() - 1; i++) {
            diff.add(list.get(i + 1) - list.get(i));
        }
        return diff;
    }

    public static StringBuilder palindromeCoefficient(String argument, int index) {
        StringBuilder builder = new StringBuilder();
        builder.append("S")
                .append(index)
                .append("(")
                .append(argument)
                .append(")");
        return builder;
    }

    public static PalindromeFunction merge(List<PalindromeFunction> functions) {
        PalindromeFunction example = functions.get(0);
        List<CycleFunction> coefficients = new ArrayList<>(example.getSize());
        for (int i = 0; i < example.getSize(); i++) {
            CycleFunction cycle = new CycleFunction();
            for (PalindromeFunction function : functions) {
                cycle.extend(function.getCoefficient(i));
            }
            coefficients.add(cycle);
        }
        return new PalindromeFunction(example.getArgument(), coefficients);
    }
}
