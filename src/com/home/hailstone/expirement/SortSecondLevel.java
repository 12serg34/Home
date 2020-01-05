package com.home.hailstone.expirement;

import com.home.hailstone.Item;
import com.home.hailstone.math.Function;
import com.home.hailstone.math.FunctionAnalyzer;

import java.math.BigInteger;
import java.util.*;
import java.util.function.BinaryOperator;
import java.util.function.IntFunction;
import java.util.stream.IntStream;

import static com.home.hailstone.math.BigIntegerUtil.*;
import static java.math.BigInteger.ZERO;
import static java.util.stream.Collectors.*;

/*
нужно найти формулу для ограничения первого уровня:
    эта формула должна быть решением нахождения максимального i, такого что:
        max i: x(x(0, i), 0) < limit
    иными словами, нужно аналитически найти максимальный индекс для числа первого уровня, такой, что хотя бы один его
    дочерний элемент(наименьший элемент это база) давал число подходящее под условие поиска(< limit)

    далее, используя ей и свойство увелечения значения от соседа к соседу в одном ряде, составить функцию для нахождения
    всех смерженных и отсортированных чисел второго уровня меньших определенного лимита.
    такая последовательность пригодна для анализа и является неменьшящейся начальной частью даже при
    перехеоде limit->бесконечность
    итого:
        1) ищем максимальное i первого уровня по алгоритму выше(аналитечиски)
        2) проходимся по числам первого уровня от 0 до i
        3) генерим для каждого следующее число начиная от базы, проверяем что оно меньше лимита, добавляем в общий сет
            для сортировки
        4) в случае встречи первого числа > лимита, переходим к следущему числу первого уровня(дальше числа
            будут только больше)

    при переходах доказательства от множества A к множеству B, важно обосновать, что если гипотеза верна в множестве B,
    то она будет верна и в множестве A

    по сути для сиракуз достаточно предоставлять функцию маппинга, которая будет менять множество(не повторяющеися) на другое
    неповторяющейся множество такого же размера, в том плане, не будет чисел, которые будут выкинуты или добавлены,
    а будет простая перестановка всех чисел.

 */

public class SortSecondLevel {

    private BigInteger limit;
    private List<List<Function>> functionsByValues;

    public static void main(String[] args) {
        new SortSecondLevel().run();
    }

    private void run() {
        println("It's a merge of two levels");
        int limitExponent = 30;
        println("Limit: " + limitExponent);
        limit = BigInteger.valueOf(10).pow(limitExponent);
        List<Item<BigInteger>> firstLevel = calculateFirstLevel();
        List<Item<BigInteger>> secondLevel = calculateSecondLevel(firstLevel);

        List<Item<BigInteger>> sorted = secondLevel.stream()
                .sorted(Comparator.comparing(Item::getValue))
                .collect(toList());
        println("sorted: " + sorted.stream().map(Item::getValue).collect(toList()));

        List<Integer> firstIndexes = sorted.stream()
                .map(Item::getFirstIndex)
                .collect(toList());
        println("first indexes: " + firstIndexes);

        List<Integer> secondIndexes = sorted.stream()
                .map(Item::getSecondIndex)
                .collect(toList());
        println("second indexes: " + secondIndexes);

        Map<Integer, List<Integer>> groupOfFirstIndexes = groupByValues(firstIndexes);
        println("Group of first indexes:" + groupOfFirstIndexes);

        FunctionAnalyzer analyzer = new FunctionAnalyzer();
        functionsByValues = new ArrayList<>();
        for (int v = 0; v < groupOfFirstIndexes.size(); v++) {
            List<Integer> data = groupOfFirstIndexes.get(v);
            if (data.size() < 18) {
                continue;
            }
            List<Function> functions = analyzer.analyze(data, 6);
            functionsByValues.add(functions);
            println("first index, value = " + v + ": " + functions);
        }

        int[][] A = new int[6][6];
        int[][] B = new int[6][6];
        int[][] C = new int[6][6];
        for (int c = 0; c < 2; c++) {
            for (int l = 0; l < 6; l++) {
                List<Function> functionsOfCoefficients = analyzer.analyze(getCoefficients(l, c), 6);
                if (c == 0) {
                    for (int v = 0; v < 6; v++) {
                        A[l][v] = functionsOfCoefficients.get(v).getCoefficient(0);
                        B[l][v] = functionsOfCoefficients.get(v).getCoefficient(1);
                    }
                } else {
                    for (int v = 0; v < 6; v++) {
                        C[l][v] = functionsOfCoefficients.get(v).getCoefficient(0);
                    }
                }
                println("coefficients c = " + c + ", l = " + l + ": " + functionsOfCoefficients);
            }
        }
        println("A(l, v) = " + Arrays.deepToString(A));
        println("B(l, v) = " + Arrays.deepToString(B));
        println("C(l, v) = " + Arrays.deepToString(C));
        println("position of value \"v\" with number \"l\" in first index = " +
                "A(l, v) " +
                "+ B(l, v) * |v / 6| " +
                "+ 36 * s(|v / 6|) " +
                "+ (C(l, v) + 36 * |v / 6|) * |l / 6| " +
                "+ 36 * s(|l / 6|)");

        List<Integer> counts = countValues(firstIndexes);
        println("count values of first indexes: " + counts);
        Map<Integer, List<Integer>> groupByValues = groupByValues(counts);
        println(groupByValues);
        for (int i = 0; i < 10; i++) {
            println(analyzer.analyze(groupByValues.get(i), 6));
        }
        println(firstIndexes);
        println(counts);
        println(countValues(counts));
        println(countValues(countValues(counts)));
        println(countValues(countValues(countValues(counts))));

//        println("Sorted 2^(i1+i2) - 2^i2 ~~~");
//        for (int i = 0; i < firstIndexes.size(); i++) {
//            int i1 = i(BigInteger.valueOf(firstIndexes.get(i)), );
//            int i2 = secondIndexes.get(i);
//        }
        List<BigInteger> diff = diff(sorted.stream().map(Item::getValue).collect(toList()));
        println(diff);

        System.out.println("B - C = " + Arrays.deepToString(apply(B, C, (b, c) -> b - c)));
        int[][] T = apply(B, A, (b, a) -> (b - 18) * (b - 18) - 72 * a);
        System.out.println("(B - 18) / 36 = " + Arrays.deepToString(applyDouble(B, b -> (b - 18) / 36.0)));
        System.out.println("T(l, v) = (B - 18)^2 - 72 * A = " + Arrays.deepToString(T));
        System.out.println("T / 4 = " + Arrays.deepToString(apply(T, x -> x / 4)));
        /*
        Next task:
            Implement algorithm that took function f(x) (x - integer, f(x) - double) and found first x that give
            integer f(x).
            Vary i in discriminant of v formula, check that works for l(i).
            Another variant, build table i/l and underline(or mark somehow) real function l(i), try to guess how it can be found,
            what's relation between them in scope of E(D(i)) (space of definition for i and l(i))
         */
        TetraFunction<Integer, Integer, Integer, Double> function_V = (i, l, vMod6) -> {
            int L = l / 6;
            int decremental = T[l % 6][vMod6] + 72 * (i + 2 * L);
            return (-(36 * L + B[l % 6][vMod6] - 18) + Math.sqrt(decremental)) / 36;
        };
        int maxOf_l = counts.stream().mapToInt(x -> x).max().getAsInt();
        System.out.println("max l = " + maxOf_l);
        {
            // check of function_V
            for (int i = 0; i < firstIndexes.size(); i++) {
                int v = firstIndexes.get(i);
                int l = counts.get(i);
                int V_to_check = (int) ((double) function_V.apply(i, l, v % 6));
                if ((v / 6) != V_to_check) {
                    System.out.print("test failed: expected = " + (v / 6) + ", actual = " + V_to_check);
                    break;
                }
            }
        }
        {
            List<Integer> indexesInSpaceOfDefinition = new ArrayList<>(firstIndexes.size());
            for (int i = 0; i < firstIndexes.size(); i++) {
                int ii = i;
                List<Integer> spaceOfDefinition = getSpaceOfDefinition(
                        l -> function_V.apply(ii, l, firstIndexes.get(ii) % 6),
                        maxOf_l);
                Integer l = counts.get(i);
//            System.out.println(spaceOfDefinition + " l(i) = " + l);
                indexesInSpaceOfDefinition.add(spaceOfDefinition.indexOf(l));
            }
            System.out.println(indexesInSpaceOfDefinition);
        }
        /*
            all values are zero therefore l in function v(l, i) it's a first value that fits space of definition of
            v(i,l) function e.g. E[v(i, l)]
        */
        {
            List<Integer> another_l = new ArrayList<>(firstIndexes.size());
            for (int i = 0; i < firstIndexes.size(); i++) {
                int _i = i;
                int min = Integer.MAX_VALUE;
                for (int vMod6 = 0; vMod6 < 6; vMod6++) {
                    int _vMod6 = vMod6;
                    List<Integer> spaceOfDefinition = getSpaceOfDefinition(
                            l -> function_V.apply(_i, l, _vMod6),
                            min);
                    if (!spaceOfDefinition.isEmpty()) {
                        Integer candidate = spaceOfDefinition.get(0);
                        if (candidate < min) {
                            min = candidate;
                        }
                    }
                }
                another_l.add(min);
            }
            System.out.println(counts);
            System.out.println(another_l);
        }
        /*
            values of counts - "l" are equal values of another_l that were build as min value of first value in space
            of definitions of six partial functions of different rest of "v". Therefore l in function v(l, i)
            it's a first value that fits space of definition of v(i,l) function e.g. E[v(i, l)]
        */

        List<Integer> vmod6 = firstIndexes.stream().map(x -> x % 6).collect(toList());
        System.out.println("v(i) % 6 = " + vmod6);
        System.out.println("A[l, v] % 6 = " + Arrays.deepToString(apply(A, a -> a % 6)));
        System.out.println("B[l, v] % 6 = " + Arrays.deepToString(apply(B, b -> b % 6)));
        System.out.println("C[l, v] % 6 = " + Arrays.deepToString(apply(C, c -> c % 6)));
        Map<Integer, List<Integer>> groupvmod6 = groupByValues(vmod6);
        System.out.println("group by values of v % 6 = " + groupvmod6);
//        System.out.println("analyze of 0-s values of v % 6 = " + analyzer.analyze(groupvmod6.get(0), ));

        int[][] O = new int[6][6];
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                O[i][j] = i * 6 + j;
            }
        }
        List<Integer> lmod6 = counts.stream().map(x -> x % 6).collect(toList());
        List<Integer> Olv = IntStream.range(0, firstIndexes.size())
                .mapToObj(i -> O[lmod6.get(i)][vmod6.get(i)])
                .collect(toList());
        System.out.println("O(l,v) = " + Olv);
    }

    private void testGetSpaceOfDefinition() {
        System.out.println(getSpaceOfDefinition(x -> (double) x, 100));
        System.out.println(getSpaceOfDefinition(x -> (double) 2 * x, 100));
        System.out.println(getSpaceOfDefinition(x -> (double) x / 2, 100));
        System.out.println(getSpaceOfDefinition(Math::sqrt, 100));

    }

    private List<Integer> getSpaceOfDefinition(IntFunction<Double> function, int lastValue) {
        List<Integer> result = new ArrayList<>();
        for (int x = 0; x <= lastValue; x++) {
            Double d;
            if (isInteger(function.apply(x))) {
                result.add(x);
            }
        }
        return result;
    }

    private boolean isInteger(double value) {
        return value % 1 == 0;
    }

    private int[][] apply(int[][] x, int[][] y, BinaryOperator<Integer> operator) {
        int[][] z = new int[x.length][x[0].length];
        for (int i = 0; i < x.length; i++) {
            for (int j = 0; j < x[0].length; j++) {
                z[i][j] = operator.apply(x[i][j], y[i][j]);
            }
        }
        return z;
    }

    private int[][] apply(int[][] x, IntFunction<Integer> operator) {
        int[][] z = new int[x.length][x[0].length];
        for (int i = 0; i < x.length; i++) {
            for (int j = 0; j < x[0].length; j++) {
                z[i][j] = operator.apply(x[i][j]);
            }
        }
        return z;
    }

    private double[][] applyDouble(int[][] x, IntFunction<Double> operator) {
        double[][] z = new double[x.length][x[0].length];
        for (int i = 0; i < x.length; i++) {
            for (int j = 0; j < x[0].length; j++) {
                z[i][j] = operator.apply(x[i][j]);
            }
        }
        return z;
    }

    private List<Integer> getCoefficients(int t, int c) {
        return functionsByValues.stream()
                .map(f -> f.get(t).getCoefficient(c))
                .collect(toList());
    }

    private List<Item<BigInteger>> calculateFirstLevel() {
        List<Item<BigInteger>> firstLevel = new ArrayList<>();
        int k = 0;
        Item<BigInteger> item = new Item<>(x(ZERO, 0), 0);
        while (item.getValue().compareTo(limit) < 0) {
            firstLevel.add(item);
            item = new Item<>(x(ZERO, ++k), k);
        }
        item = new Item<>(x(ZERO, ++k), k);
        if (item.getValue().compareTo(limit) < 0) {
            firstLevel.add(item);
        }
        return firstLevel;
    }

    private List<Item<BigInteger>> calculateSecondLevel(List<Item<BigInteger>> firstLevel) {
        List<Item<BigInteger>> secondLevel = new ArrayList<>();
        for (int i = 0; i < firstLevel.size(); i++) {
            Item<BigInteger> firstLevelItem = firstLevel.get(i);
            int k = 0;
            Item<BigInteger> item = new Item<>(
                    x(firstLevelItem.getValue(), 0), i, 0);
            List<Item<BigInteger>> branch = new ArrayList<>();
            while (item.getValue().compareTo(limit) < 0) {
                branch.add(item);
                item = new Item<>(x(firstLevelItem.getValue(), ++k), i, k);
            }
            secondLevel.addAll(branch);
        }
        return secondLevel;
    }

    private static Map<Integer, List<Integer>> groupByValues(List<Integer> data) {
        return IntStream.range(0, data.size())
                .mapToObj(i -> new Item<>(data.get(i), i))
                .collect(groupingBy(
                        Item::getValue,
                        mapping(Item::getFirstIndex, toList())
                ));
    }

    private void println(Object object) {
        System.out.println(object.toString());
    }

    @SuppressWarnings("SuspiciousNameCombination")
    private BigInteger x(BigInteger y, int k) {
        return reverse2(reverse1(
                forward1(forward2(y)).multiply(powOf2(i(y, k))).subtract(BigInteger.ONE).divide(THREE)
        ));
    }

    @SuppressWarnings("SuspiciousNameCombination")
    private int i(BigInteger y, int k) {
        return merge(k, m -> 6 * m + cycle(y, 2, 3, 4, 1, 2, 1),
                m -> 6 * m + cycle(y, 4, 5, 6, 3, 6, 5));
    }

    @SafeVarargs
    private final int merge(int x, IntFunction<Integer>... functions) {
        int size = functions.length;
        return functions[x % size].apply(x / size);
    }

    private List<Integer> countValues(List<Integer> data) {
        List<Integer> counts = new ArrayList<>(data.size());
        Map<Integer, Integer> valueToCount = new HashMap<>();
        for (Integer value : data) {
            Integer currentCount = valueToCount.getOrDefault(value, 0);
            counts.add(currentCount);
            valueToCount.put(value, currentCount + 1);
        }
        return counts;
    }
}
