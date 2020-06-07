package com.home.hailstone.expirement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.IntFunction;
import java.util.stream.IntStream;

import static java.util.Collections.max;

/*
        Let's index a statement sqrt(36*x + k0) = k1
     */
public class IntegerSquares {
    private static final int[] k0Mod36 = new int[]{0, 1, 4, 9, 13, 16, 25, 28};
    private static final Map<Integer, Integer> k0Mod36Tok3Mod8 = new HashMap<>(8);
    private static final List<List<Integer>> k7 = new ArrayList<>(8);
    private static final List<List<Integer>> k8 = new ArrayList<>(8);
    private static final List<List<Integer>> k11 = new ArrayList<>(18);
    private static final List<List<Integer>> k5 = new ArrayList<>(8);

    static {
        init();
    }

    private static void init() {
        List<Integer> k4Cycle = new ArrayList<>(8);
        for (int k3 = 0; k3 < 8; k3++) {
            int k0 = k0Mod36[k3];
            List<Integer> k5Row = getZeroOfCycle(k1 -> k1 * k1 - k0, 36);
            int k4 = k5Row.size();
            k4Cycle.add(k4);
            k5.add(k5Row);
        }

        for (int i = 0; i < 18; i++) {
            k11.add(new ArrayList<>(2));
        }
        for (int k3 = 0; k3 < 8; k3++) {
            for (int k2 = 0; k2 < k4Cycle.get(k3); k2++) {
                Integer k5Value = k5.get(k3).get(k2);
                k11.get(k5Value % 18).add(k5Value);
            }
        }

        for (int k10 = 0; k10 < 18; k10++) {
            List<Integer> k11Row = k11.get(k10);
            ArrayList<Integer> k7Row = getMoreThenMaxCycle(k11Row, 36);
            ArrayList<Integer> k8Row = getFirstMoreOrEqualCycle(k11Row, 36);
            k7.add(k7Row);
            k8.add(k8Row);
        }

        for (int k3Mod8 = 0; k3Mod8 < 8; k3Mod8++) {
            k0Mod36Tok3Mod8.put(k0Mod36[k3Mod8], k3Mod8);
        }
    }

    public static void main(String[] args) {
        Calculator calculator = new Calculator();
        System.out.println(calculator.k1(445, 7, 0));

//        int k3 = 100;
//        int k3Mod8 = k3 % 8;
//        int k9 = 0;
//        int k0 = 36 * (k3 / 8) + k0Mod36[k3Mod8];
//        System.out.println("k0 = " + k0);
//        int k6 = ceilSqrt(k0);
//        int k6Mod36 = k6 % 36;
//        int k4 = k4Cycle.get(k3Mod8);
//        int k2 = k4 * ((k9 / k4) + (k6 / 36) + k7.get(k3Mod8).get(k6Mod36))
//                + (k9 + k8Cycle.get(k3Mod8).get(k6Mod36)) % k4;
//        int k1 = 36 * (k2 / k4) +
//                System.out.println("k1(100) = " + k1);
//        double x = (k1 * k1 - k0) / 36.0;
//        System.out.println("x = " + x);
    }

    private static void printTwoDimensionCycle(List<List<Integer>> cycle) {
        cycle.forEach(inner -> System.out.printf("%1s,\n", inner));
    }

    private static ArrayList<Integer> getMoreThenMaxCycle(List<Integer> cycle, int period) {
        int maxValue = max(cycle);
        ArrayList<Integer> result = new ArrayList<>(period);
        for (int i = 0; i < period; i++) {
            result.add(maxValue >= i ? 0 : 1);
        }
        return result;
    }

    private static ArrayList<Integer> getFirstMoreOrEqualCycle(List<Integer> cycle, int period) {
        ArrayList<Integer> result = new ArrayList<>(period);
        for (int i = 0; i < period; i++) {
            result.add(getFirstMoreOrEqual(cycle, i));
        }
        return result;
    }

    private static int getFirstMoreOrEqual(List<Integer> cycle, int limit) {
        return IntStream.range(0, cycle.size())
                .filter(i -> cycle.get(i) >= limit)
                .findFirst()
                .orElse(0);
    }

    private static int ceilSqrt(int x) {
        return (int) Math.ceil(Math.sqrt(x));
    }

    /**
     * find x for statement f(x % t) % t = 0
     */
    private static List<Integer> getZeroOfCycle(IntFunction<Integer> function, int t) {
        List<Integer> result = new ArrayList<>();
        for (int x = 0; x < t; x++) {
            int st = function.apply(x);
            if (st % t == 0) {
                result.add(x);
            }
        }
        return result;
    }

    public static class Calculator {
        public int k1(int k0, int k10, int k12) {
            int k6 = k6(k0);
            int k3Mod8 = k3Mod8(k0);
            return 36 * ((k12 / 2) + (k6 / 36) + k7(k10, k6)) + k11(k10, (k12 + k8(k10, k6)) % 2);
        }

        public int k6(int k0) {
            return ceilSqrt(k0);
        }

        public int k7(int k10, int k6) {
            return k7.get(k10).get(k6 % 36);
        }

        public int k8(int k10, int k6) {
            return k8.get(k10).get(k6 % 36);
        }

        public int k3Mod8(int k0) {
            return k0Mod36Tok3Mod8.get(k0 % 36);
        }

        public int k5(int k3Mod8, int k11) {
            return k5.get(k3Mod8).get(k11);
        }

        public int k11(int k10, int k12) {
            return k11.get(k10).get(k12 % 2);
        }
    }
}
