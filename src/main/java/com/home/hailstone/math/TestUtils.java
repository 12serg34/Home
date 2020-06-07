package com.home.hailstone.math;

import java.util.function.IntFunction;

class TestUtils {
    static boolean testOrdering(IntFunction<Integer> function, int size) {
        for (int i = 0; i < size; i++) {
            int current = function.apply(i);
            int next = function.apply(i + 1);
            if (current >= next) {
                System.out.println("function isn't ordered: index = " + i
                        + ", current = " + current
                        + " , next = " + next);
                return false;
            }
        }
        return true;
    }

    static String testFunction(int[] expectedValues, IntFunction<Integer> function) {
        for (int i = 0; i < expectedValues.length; i++) {
            int expected = expectedValues[i];
            int actual = function.apply(i);
            if (actual != expected) {
                return "function was passed until index = " + i
                        + ", expected = " + expected + " , actual = " + actual;
            }
        }
        return "function was passed";
    }
}
