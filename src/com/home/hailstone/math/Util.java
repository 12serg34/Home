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

    public static void splitAndPrint(int[] index, int numberToSplit) {
        List<List<Integer>> splitIndex = split(index, numberToSplit);
        for (int i = 0; i < numberToSplit; i++) {
            System.out.println("splitIndex" + i + ": " + splitIndex.get(i));
        }
        for (int i = 0; i < numberToSplit; i++) {
            System.out.println("diff of splitIndex" + i + ": " + diff(splitIndex.get(i)));
        }
    }

    public static void split(List<Integer> values, SplitHierarchy hierarchy) {
        int size = hierarchy.getSize();
        List<List<Integer>> splitValues = new ArrayList<>(size);
        boolean isSimple = hierarchy.isSimple();
        boolean hasDiffs = hierarchy.hasDiffs();
        if (size == 1) {
            if (isSimple) {
                if (hasDiffs) {
                    diff(values, hierarchy.getDiffDepth(0));
                }
            }
            return;
        } else {
            for (int i = 0; i < size; i++) {
                splitValues.add(new ArrayList<>());
            }
            for (int i = 0; i < values.size(); i++) {
                splitValues.get(i % size).add(values.get(i));
            }
        }

        for (int i = 0; i < size; i++) {
            List<Integer> branch = splitValues.get(i);
            System.out.println(branch);
            if (isSimple) {
                if (hasDiffs) {
                    diff(branch, hierarchy.getDiffDepth(i));
                }
            } else {
                split(branch, hierarchy.getChild(i));
            }
        }
    }

    public static List<Integer> diff(List<Integer> list) {
        List<Integer> diff = new ArrayList<>(list.size() - 1);
        for (int i = 0; i < list.size() - 1; i++) {
            diff.add(list.get(i + 1) - list.get(i));
        }
        return diff;
    }

    private static void diff(List<Integer> list, int depth) {
        if (depth < 1) {
            return;
        }
        List<Integer> diff = new ArrayList<>(list.size() - 1);
        for (int i = 0; i < list.size() - 1; i++) {
            diff.add(list.get(i + 1) - list.get(i));
        }
        System.out.println(diff);
        diff(diff, depth - 1);
    }
}
