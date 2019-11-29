package com.home.hailstone.expirement;

import com.home.hailstone.Item;
import com.home.hailstone.math.Function;
import com.home.hailstone.math.FunctionAnalyzer;

import java.math.BigInteger;
import java.util.*;
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

        List<Item> sorted = secondLevel.stream()
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

    private int[] toArray(List<Integer> list) {
        return list.stream()
                .mapToInt(x -> x)
                .toArray();
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
}
