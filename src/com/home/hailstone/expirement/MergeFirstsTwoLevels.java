package com.home.hailstone.expirement;

import com.home.hailstone.math.Function;
import com.home.hailstone.math.FunctionAnalyzer;
import com.home.hailstone.math.Util;

import java.math.BigInteger;
import java.util.*;
import java.util.function.IntFunction;
import java.util.stream.IntStream;

import static com.home.hailstone.math.BigIntegerUtil.*;
import static com.home.hailstone.math.SplitHierarchy.splitBy;
import static java.math.BigInteger.ZERO;
import static java.util.stream.Collectors.toList;

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

public class MergeFirstsTwoLevels {
    public static void main(String[] args) {
        new MergeFirstsTwoLevels().run();
    }

    private void run() {
        println("Hello, it's merge of two firsts levels");
        BigInteger limit = BigInteger.valueOf(10).pow(30);
        println("Limit: " + limit);

        int k = 0;
        Item item = new Item(0, 0, x(ZERO, 0));
        List<Item> firstLevel = new ArrayList<>();
        while (item.value.compareTo(limit) < 0) {
            firstLevel.add(item);
            item = new Item(++k, 0, x(ZERO, k));
        }
        item = new Item(++k, 0, x(ZERO, k));
        if (item.value.compareTo(limit) < 0) {
            firstLevel.add(item);
        }
        println("first level: " + firstLevel);

        List<BigInteger> originalFirstLevel = firstLevel.stream()
                .map(it -> forward1(forward2(it.value)))
                .collect(toList());
        println("original first level: " + originalFirstLevel);

        List<BigInteger> basesFromFirstLevel = IntStream.range(0, 100)
                .mapToObj(i -> x(x(ZERO, i), 0))
                .collect(toList());
        println("bases from first level: " + basesFromFirstLevel);

        List<Item> secondLevel = new ArrayList<>();
        for (int i = 0; i < firstLevel.size(); i++) {
            Item itemFromLevel1 = firstLevel.get(i);
            k = 0;
            item = new Item(i, 0, x(itemFromLevel1.value, 0));
            List<Item> branch = new ArrayList<>();
            while (item.value.compareTo(limit) < 0) {
                branch.add(item);
                item = new Item(i, ++k, x(itemFromLevel1.value, k));
            }
            println("branch of second level number " + i + " from " + itemFromLevel1.value + ": "
                    + branch);
            secondLevel.addAll(branch);
        }
        List<Item> mergedLevels = secondLevel.stream()
                .sorted(Comparator.comparing(Item::getValue))
                .collect(toList());
        println("merged levels: " + mergedLevels);
        int[] indexesOfFirstLevel = mergedLevels.stream()
                .mapToInt(Item::getI1)
                .toArray();
        println("indexes of first level: " + Arrays.toString(indexesOfFirstLevel));
        int[] indexesOfSecondLevel = mergedLevels.stream()
                .mapToInt(Item::getI2)
                .toArray();
        println("indexes of second level: " + Arrays.toString(indexesOfSecondLevel));

        Map<Integer, List<Integer>> groupOfFirstIndexes = groupByValues(indexesOfFirstLevel);
        println("Grouping of first indexes:" + groupOfFirstIndexes);
        println("Grouping of second indexes:" + groupByValues(indexesOfSecondLevel));

        println("split, group, first index, zero: ");
        Util.splitAndPrint(toArray(groupOfFirstIndexes.get(0)), 2);
        println("split, group, first index, one: ");
        Util.splitAndPrint(toArray(groupOfFirstIndexes.get(1)), 2);

        println("\nAfter all it can look like this:");
        println("split, group, first index, zero: ");
        Util.split(groupOfFirstIndexes.get(0), splitBy(6, 2));
        println("split, group, first index, one: ");
        Util.split(groupOfFirstIndexes.get(1), splitBy(6, 2));
        println("split, group, first index, two: ");
        Util.split(groupOfFirstIndexes.get(2), splitBy(6, 2));
        println("split, group, first index, three: ");
        Util.split(groupOfFirstIndexes.get(3), splitBy(6, 2));
        println("split, group, first index, four: ");
        Util.split(groupOfFirstIndexes.get(4), splitBy(6, 2));
        println("split, group, first index, five: ");
        Util.split(groupOfFirstIndexes.get(5), splitBy(6, 2));
        println("split, group, first index, six: ");
        Util.split(groupOfFirstIndexes.get(6), splitBy(6, 2));

        println("\nAttempt to analyze function automatically");
        FunctionAnalyzer analyzer = new FunctionAnalyzer();
        List<Function> functions = new ArrayList<>();
        for (int v = 0; v < groupOfFirstIndexes.size(); v++) {
            List<List<Integer>> split = Util.split(groupOfFirstIndexes.get(v), 6);
            functions.add(analyzer.analyze(split.get(0)));
        }
        List<Integer> collect = functions.stream()
                .map(f -> f.getCoefficient(0))
                .collect(toList());
        println(collect);
        println(analyzer.analyze(collect, 6));
        List<Integer> collect2 = functions.stream()
                .filter(f -> f.getSize() > 1)
                .map(f -> f.getCoefficient(1))
                .collect(toList());
        println(collect2);
        println(analyzer.analyze(collect2, 6));
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

    static class Item {
        int i1;
        int i2;
        BigInteger value;

        Item(int i1, int i2, BigInteger value) {
            this.i1 = i1;
            this.i2 = i2;
            this.value = value;
        }

        public BigInteger getValue() {
            return value;
        }

        public int getI1() {
            return i1;
        }

        public int getI2() {
            return i2;
        }

        @Override
        public String toString() {
            return value.toString();
        }
    }
}
