package com.home.hailstone.expirement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.IntFunction;
import java.util.stream.Collectors;
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

        int size = 100;
        int[] index = IntStream.range(0, size).toArray();
        System.out.println("index: " + Arrays.toString(index));

        int[] function = calculateFunction(this::function, index);
        System.out.println("function: " + Arrays.toString(function));

        int[] sortedFunction = Arrays.stream(function)
                .sorted()
                .toArray();
        System.out.println("sorted function: " + Arrays.toString(sortedFunction));

        int[] sortedIndex = calculateFunction(this::reverse, sortedFunction);
        System.out.println("index of sorted function: " + Arrays.toString(sortedIndex));

        int[] diffFromIndex = new int[size];
        for (int i = 0; i < size; i++) {
            diffFromIndex[i] = sortedIndex[i] - index[i];
        }
        System.out.println("diffFromIndex: " + Arrays.toString(diffFromIndex));

        List<Integer> indexesOfDiffOnEvenPlaces = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            if (i % 2 == 0) {
                indexesOfDiffOnEvenPlaces.add(diffFromIndex[i]);
            }
        }
        System.out.println("indexesOfDiffOnEvenPlaces: " + indexesOfDiffOnEvenPlaces);

        List<Integer> indexesOfDiffOnOddPlaces = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            if (i % 2 == 1) {
                indexesOfDiffOnOddPlaces.add(diffFromIndex[i]);
            }
        }
        System.out.println("indexesOfDiffOnOddPlaces: " + indexesOfDiffOnOddPlaces);

        int[] guessOfSortedIndexes = Arrays.stream(index)
                .map(this::guessOfSortedIndexesFunction)
                .toArray();
        System.out.println("guess of sorted indexes: " + Arrays.toString(guessOfSortedIndexes));

        int[] guessOfSortedFunction = calculateFunction(this::function, guessOfSortedIndexes);
        System.out.println("guess of sorted function: " + Arrays.toString(guessOfSortedFunction));
        System.out.println("Success! sorted and guess equals till some limit(for size=100 it's 203, because of " +
                "limitation with size");

        System.out.println("\nAttempt to guess of sorted function itself after putting of f(m(x))");
        int[] guessOfSortedFunctionAgain = Arrays.stream(index)
                .map(this::guessOfSortedFunction)
                .toArray();
        System.out.println("guess of sorted function again: " + Arrays.toString(guessOfSortedFunctionAgain));

        System.out.println("test of testGammaOfGuessOfSortedFunction: " + testGammaOfGuessOfSortedFunction());
        System.out.println("test of testGuessOfSortedIndexesFunction: " + testGuessOfSortedIndexesFunction());
        System.out.println("test of testGuessOfSortedFunction: " + testGuessOfSortedFunction());

        buildSortIndexFunction(this::guessOfSortedIndexesFunction,
                x -> powOf2(invertMod2(gamma(x)) + 1) * (x / 3) + (2 * x) % 3,
                100, 3);
    }

    private int[] calculateFunction(IntFunction<Integer> function, int[] x) {
        return Arrays.stream(x)
                .map(function::apply)
                .toArray();
    }

    private int function(int x) {
        return forward1(x) * powOf2(invertMod2(gamma(x)));
    }

    private int reverse(int x) {
        return reverse1(cycle(x, x / 2, x));
    }

    private int guessOfSortedIndexesFunction(int x) {
        return x + cycle(x,
                -1 * (x / 2 + 1) / 2,
                x / 2 + 1
        );
    }

    private int guessOfSortedFunction(int x) {
        return 3 * x + 2 * cycle(x, cycle(x / 2, 1, 0), cycle(x / 2, 1, 1));
    }

    private void buildSortIndexFunction(IntFunction<Integer> function, IntFunction<Integer> guessIndex, int limit, int numberToSplit) {
        System.out.println("\nwe starts to sort our functions!");
        int[] index = IntStream.range(0, limit).toArray();
        int[] appliedFunction = calculateFunction(function, index);
        List<Item> sortedItems = sort(function, index);
        int[] sortedFunction = sortedItems.stream()
                .mapToInt(Item::getValue)
                .toArray();
        int[] sortedIndex = sortedItems.stream()
                .mapToInt(Item::getIndex)
                .toArray();
        List<List<Integer>> splitIndex = split(sortedIndex, numberToSplit);
        System.out.println("index: " + Arrays.toString(index));
        System.out.println("function: " + Arrays.toString(appliedFunction));
        System.out.println("sorted function: " + Arrays.toString(sortedFunction));
        System.out.println("sorted index: " + Arrays.toString(sortedIndex));
        for (int i = 0; i < numberToSplit; i++) {
            System.out.println("splitIndex" + i + ": " + splitIndex.get(i));
        }
        System.out.println("tests results: \nguess index ordering: " + testOrdering(x -> function.apply(guessIndex.apply(x)), limit));
        System.out.println("guess index values: " + testFunction(sortedIndex, guessIndex));
    }

    private List<List<Integer>> split(int[] index, int numberToSplit) {
        List<List<Integer>> splitIndex = new ArrayList<>(numberToSplit);
        for (int i = 0; i < numberToSplit; i++) {
            splitIndex.add(new ArrayList<>());
        }
        for (int i = 0; i < index.length; i++) {
            splitIndex.get(i % numberToSplit).add(index[i]);
        }
        return splitIndex;
    }

    private List<Item> sort(IntFunction<Integer> function, int[] index) {
        return Arrays.stream(index)
                .mapToObj(x -> new Item(x, function.apply(x)))
                .sorted(Comparator.comparing(Item::getValue))
                .collect(Collectors.toList());
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

    private boolean testOrdering(IntFunction<Integer> sortFunction, int limit) {
        for (int i = 1; i < limit; i++) {
            int current = sortFunction.apply(i);
            int previous = sortFunction.apply(i - 1);
            if (current <= previous) {
                System.out.println("function isn't sorted: index = " + i + ", current = " + current + " , previous = " + previous);
                return false;
            }
        }
        return true;
    }

    private int testFunction(int[] expected, IntFunction<Integer> actual) {
        for (int i = 0; i < expected.length; i++) {
            int expectedValue = expected[i];
            int functionalValue = actual.apply(i);
            if (functionalValue != expectedValue) {
                System.out.println("function are equals until index = " + i + ", expected = " + expectedValue + " , actual = " + functionalValue);
                return i;
            }
        }
        return expected.length;
    }

    private boolean testGammaOfGuessOfSortedFunction() {
        return testFunctionEquals(
                x -> invertMod2(gamma(guessOfSortedIndexesFunction(x))),
                x -> invertMod2(x % 2),
                100
        );
    }

    private boolean testGuessOfSortedIndexesFunction() {
        return testFunctionEquals(
                this::guessOfSortedIndexesFunction,
                x -> 3 * cycle(x, 1, 2) * (x / 4) + cycle(x, cycle(x / 2, 0, 1), cycle(x / 2, 2, 5)),
                100
        );
    }

    private boolean testGuessOfSortedFunction() {
        return testFunctionEquals(
                x -> function(guessOfSortedIndexesFunction(x)),
                this::guessOfSortedFunction,
                100
        );
    }

    class Item {
        int index;
        int value;

        private Item(int index, int value) {
            this.index = index;
            this.value = value;
        }

        private int getValue() {
            return value;
        }

        private int getIndex() {
            return index;
        }

        @Override
        public String toString() {
            return String.valueOf(index);
        }
    }
}
