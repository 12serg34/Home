package com.home.hailstone.expirement;

import java.util.Arrays;
import java.util.function.IntFunction;
import java.util.stream.IntStream;

import static com.home.hailstone.math.Util.*;

/**
 * This is a class to sort function: F1(x) * 2 ^ invert2(gamma(x))
 */
public class SortF1InvertGammaX {
    public static void main(String[] args) {
        new SortF1InvertGammaX().run();
    }

    private void run() {
        System.out.println("Hello it's a " + SortF1InvertGammaX.class);
        IntFunction<Integer> function = x -> forward1(x) * powOf2(invertMod2(gamma(x)));
        IntFunction<Integer> guessOfSortedIndexesFunction = x ->
                x + cycle(x,
                        -1 * (x / 2 + 1) / 2,
                        x / 2 + 1
                );

        System.out.println("--Build forward sort function");
        buildSortIndexFunction(function, guessOfSortedIndexesFunction, 100, 4);

        System.out.println("\n--Build reverse of sort function");
        buildSortIndexFunction(guessOfSortedIndexesFunction,
                x -> powOf2(invertMod2(gamma(x)) + 1) * (x / 3) + (2 * x) % 3,
                100, 3);

        System.out.println("\nAttempt to guess of sorted function itself after putting of f(m(x))");
        IntFunction<Integer> guessOfSortedFunction = x ->
                3 * x + 2 * cycle(x, cycle(x / 2, 1, 0), cycle(x / 2, 1, 1));
        int size = 100;
        int[] index = IntStream.range(0, size).toArray();
        int[] guessOfSortedFunctionAgain = Arrays.stream(index)
                .map(guessOfSortedFunction::apply)
                .toArray();
        System.out.println("guess of sorted function again: " + Arrays.toString(guessOfSortedFunctionAgain));

        System.out.println("test of testGammaOfGuessOfSortedFunction: " + testFunctionEquals(
                x2 -> invertMod2(gamma(guessOfSortedIndexesFunction.apply(x2))),
                x2 -> invertMod2(x2 % 2),
                100
        ));
        System.out.println("test of testGuessOfSortedIndexesFunction: " + testFunctionEquals(
                guessOfSortedIndexesFunction,
                x1 -> 3 * cycle(x1, 1, 2) * (x1 / 4) + cycle(x1, cycle(x1 / 2, 0, 1), cycle(x1 / 2, 2, 5)),
                100
        ));
        System.out.println("test of testGuessOfSortedFunction: " + testFunctionEquals(
                x -> function.apply(guessOfSortedIndexesFunction.apply(x)),
                guessOfSortedFunction,
                100
        ));
    }

    private boolean testFunctionEquals(IntFunction<Integer> function1, IntFunction<Integer> function2, int limit) {
        for (int i = 0; i < limit; i++) {
            int value1 = function1.apply(i);
            int value2 = function2.apply(i);
            if (value1 != value2) {
                System.out.println("functions are equals until index = " + i + ": value1 = " + value1 + " , value2 = " + value2);
                return false;
            }
        }
        return true;
    }
}
