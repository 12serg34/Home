package com.home.hailstone.expirement;

import com.home.hailstone.math.FunctionAnalyzer;
import com.home.hailstone.math.PalindromeFunction;
import com.home.hailstone.math.Util;
import com.home.hailstone.math.Value;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

public class IntegerSquares {

    /*
        I can't get a formula for 3x+2. Also, 3x+1 was very strange and unvalaible for analyzing. I just simply would
        use my particular coefficients
     */

    private FunctionAnalyzer analyzer;
    private int limit;

    public static void main(String[] args) {
        new IntegerSquares().run();
    }

    @SuppressWarnings("Convert2MethodRef")
    private void run() {
        System.out.println("Let's find integer squares for line functions a*x + b");
        limit = 1000;
        analyzer = new FunctionAnalyzer();

        {
            for (int b = 0; b < 10; b++) {
                analyze(1, b, 1);
                int finalI = b;
                System.out.println(Arrays.toString(IntStream.range(0, 100)
                        .map(y -> f1(y, finalI))
                        .toArray()));
                System.out.println();
            }
        }
        {
            for (int b = 0; b < 10; b++) {
                PalindromeFunction palindrome = analyze(2, b, 2);
                int k0 = ((Value) palindrome.getCoefficient(0)).getValue();
                int k1 = ((Value) palindrome.getCoefficient(1)).getValue();
                int k2 = ((Value) palindrome.getCoefficient(2)).getValue();
                int k3 = k1 - k2 / 2;
                int k4 = 2 * k0 * k2 - k3 * k3;
                System.out.printf("k3 = %1d, k4 = %2d\n", k3, k4);
                int finalI = b;
                System.out.println(Arrays.toString(IntStream.range(0, 100)
                        .map(y -> f2(y, finalI))
                        .toArray()));
                System.out.println();
            }
        }

    }

    private PalindromeFunction analyze(int a, int b, int period) {
        List<Integer> spaceOfDefinition = Util.getSpaceOfDefinition(x -> Math.sqrt(a * x + b), limit);
        System.out.printf("SoD of %1d*x+%2d: %3s\n", a, b, spaceOfDefinition);
        List<PalindromeFunction> analyze = analyzer.analyze("y", spaceOfDefinition, period);
        System.out.printf("x(y) = %1s\n", analyze);
        return analyze.get(0);
    }

    /**
     * Get y-th integer number in SoD of sqrt(x + b)
     *
     * @param y index
     * @param b parameter
     * @return (y + /sqrt(b)\) ^ 2 - b
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
     * @return (y + /sqrt(b)\) ^ 2 - b
     */
    private int f2(int y, int b) {
        int t = 2 * y + ceilSqrt(b);
        return (t * t - b) / 2;
    }

    private int ceilSqrt(int x) {
        return (int) Math.ceil(Math.sqrt(x));
    }
}
