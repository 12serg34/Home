package com.home.hailstone.expirement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.IntFunction;
import java.util.stream.IntStream;

public class FindLimitF12y2iy0 {
    public static void main(String[] args) {
        new FindLimitF12y2iy0().run();
    }

    private void run() {
        List<IntFunction<Integer>> functions = new ArrayList<>(6);
        functions.add(l -> limitOfLineFunction(36, 2, l));
        functions.add(l -> limitOfLineFunction(54, 15, l));
        functions.add(l -> limitOfLineFunction(72, 28, l));
        functions.add(l -> limitOfLineFunction(18, 11, l));
        functions.add(l -> limitOfLineFunction(36, 26, l));
        functions.add(l -> limitOfLineFunction(18, 17, l));

        IntFunction<Integer> maxFunction = max(functions);
        int[] maxValues = IntStream.range(0, 100)
                .map(maxFunction::apply)
                .toArray();
        System.out.println(Arrays.toString(maxValues));
    }

    /**
     * Find limit of function f(x) = a * x + b, i.e. max x: f(x) <= l
     * It's should be equals |(l - b) / a|
     *
     * @param a factor
     * @param b term
     * @param l limit
     * @return max x
     */
    private int limitOfLineFunction(int a, int b, int l) {
        return (l - b) / a;
    }

    private IntFunction<Integer> max(List<IntFunction<Integer>> functions) {
        return x -> {
            int maxValue = Integer.MIN_VALUE;
            for (IntFunction<Integer> function : functions) {
                maxValue = Math.max(maxValue, function.apply(x));
            }
            return maxValue;
        };
    }
}
