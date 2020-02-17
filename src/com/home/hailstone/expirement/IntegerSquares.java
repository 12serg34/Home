package com.home.hailstone.expirement;

import com.home.hailstone.math.CycleFunction;
import com.home.hailstone.math.FunctionAnalyzer;
import com.home.hailstone.math.PalindromeFunction;
import com.home.hailstone.math.Util;
import com.home.hailstone.math.Value;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.IntFunction;
import java.util.stream.Collectors;

public class IntegerSquares {

    /*
        I can't get a formula for 3x+2. Also, 3x+1 was very strange and unvalaible for analyzing. I just simply would
        use my particular coefficients
     */
    /*
        [
            [0, 0, 0, 0, 0, 0, 0, 0, 0]*S0(b),
            [1, 8, 7, 2, 5, 4, 3, 2, 1]*S0(b) + [3, 8, 7, 3, 5, 4, 3, 2, 1]*S1(b),

            [4, 10, 11, 6, 13, 14, 8, 16, 17]*S0(b) + [6, 9, 9, 6, 9, 9, 6, 9, 9]*S1(b),
            [8, 26, 25, 10, 23, 22, 12, 20, 19]*S0(b) + [6, 9, 9, 6, 9, 9, 6, 9, 9]*S1(b),

            [8, 18, 18, 8, 18, 18, 8, 18, 18]*S0(b),
            [8, 18, 18, 8, 18, 18, 8, 18, 18]*S0(b)
        ]
     */

    private FunctionAnalyzer analyzer;
    private int limit;

    public static void main(String[] args) {
        new IntegerSquares().run();
    }

    @SuppressWarnings("Convert2MethodRef")
    private void run() {
        System.out.println("Let's find integer squares for line functions a*x + b");
        limit = 10000;
        analyzer = new FunctionAnalyzer();

        List<PalindromeFunction> palindromeFunctions = new ArrayList<>();
        {
            for (int b = 0; b < 40; b++) {
                System.out.print(b + " ");
                palindromeFunctions.add(analyze(36, b * b, 2));
//                int finalI = b;
//                System.out.println(Arrays.toString(IntStream.range(0, 100)
//                        .map(y -> f1(y, finalI))
//                        .toArray()));
//                System.out.println();
            }
            System.out.println(analyze(palindromeFunctions, 9));
        }
        {
            int[][] m = new int[36][36];
            for (int i = 0; i < 36; i++) {
                for (int j = 0; j < 36; j++) {
                    int v = i * i - j * j;
                    m[i][j] = (v + 36) % 36 == 0 ? 0 : 1;
                }
            }
            System.out.println(Arrays.deepToString(m));
        }
        {
            System.out.println("cycles");
            for (int b = 0; b < 18; b++) {
                System.out.println(getZeroOfCycle(6, b, 18));
            }
        }
        {
            System.out.println("another cycles");
            for (int k0 = 1; k0 < 18; k0++) {
                int finalK0 = k0;
                for (int k1 = 0; k1 < 18; k1++) {
                    int finalK1 = k1;
                    List<Integer> zeroOfCycle2 = getZeroOfCycle2(x -> 9 * x + finalK0 * (x % 2) + finalK1, 18);
                    if (!zeroOfCycle2.isEmpty()) {
                        System.out.printf("k0 = %1d, k1 = %2d, zeros at %3s\n", k0, k1, zeroOfCycle2);
                    }
                }
            }
        }
    }

    private PalindromeFunction analyze(int a, int b, int period) {
        List<Integer> spaceOfDefinition = Util.getSpaceOfDefinition(x -> Math.sqrt(a * x + b), limit);
//        System.out.printf("SoD of %1d*x+%2d: %3s\n", a, b, spaceOfDefinition);
        PalindromeFunction analyze = Util.merge(analyzer.analyze("y", spaceOfDefinition, period));
        System.out.printf("x(y) = %1s\n", analyze);
        return analyze;
    }

    private List<PalindromeFunction> analyze(List<PalindromeFunction> palindromes, int period) {
        int size = checkAndGetSameSize(palindromes);
        CycleFunction cycleExample = palindromes.get(0).getCoefficient(0).getAs();
        int totalSize = size * cycleExample.getSize();
        List<List<Integer>> coefficients = new ArrayList<>(totalSize);
        for (int i = 0; i < totalSize; i++) {
            coefficients.add(new ArrayList<>(palindromes.size()));
        }
        for (int i = 0; i < palindromes.size(); i++) {
            PalindromeFunction function = palindromes.get(i);
            for (int j = 0; j < size; j++) {
                CycleFunction coefficient = function.getCoefficient(j).getAs();
                for (int k = 0; k < coefficient.getSize(); k++) {
                    Value value = coefficient.getElement(k).getAs();
                    coefficients.get(j * coefficient.getSize() + k).add(value.getValue());
                }
            }
        }
        return coefficients.stream()
                .map(data -> Util.merge(analyzer.analyze("b", data, period)))
                .collect(Collectors.toList());
    }

    private int checkAndGetSameSize(List<PalindromeFunction> palindromes) {
        int size = palindromes.get(0).getSize();
        for (int i = 1; i < palindromes.size(); i++) {
            if (palindromes.get(i).getSize() != size) {
                throw new RuntimeException("you are wrong! function: " + palindromes.get(i) + ", size: " + size);
            }
        }
        return size;
    }

    /**
     * Get y-th integer number in SoD of sqrt(x + b)
     *
     * @param y index
     * @param b parameter
     * @return (y + / sqrt ( b)\) ^ 2 - b
     */
    private int f1(int y, int b) {
        int t = y + ceilSqrt(b);
        return t * t - b;
    }

    /**
     * Get y-th integer number in SoD of sqrt(2*x + b)
     *
     * @param y index
     * @param b parameter
     * @return (y + / sqrt ( b)\) ^ 2 - b
     */
    private int f2(int y, int b) {
        int t = 2 * y + ceilSqrt(b);
        return (t * t - b) / 2;
    }

    private int ceilSqrt(int x) {
        return (int) Math.ceil(Math.sqrt(x));
    }

    /**
     * find x for statement (a*x + b) % 18 = 0
     */
    private List<Integer> getZeroOfCycle(int a, int b, int t) {
        List<Integer> result = new ArrayList<>();
        for (int x = 0; x < t; x++) {
            int st = a * x + b;
            if (st % t == 0) {
                result.add(x);
            }
        }
        return result;
    }

    private List<Integer> getZeroOfCycle2(IntFunction<Integer> function, int t) {
        List<Integer> result = new ArrayList<>();
        for (int x = 0; x < t; x++) {
            int st = function.apply(x);
            if (st % t == 0) {
                result.add(x);
            }
        }
        return result;
    }
}
