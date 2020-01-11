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

    update 05.01.2020
        Сейчас мы научились формально описывать как можно найти значение первого индекса в зависимости от глобального
        индекса сортировки. Второй индекс еще не описан, но кажется там примерно такая же зависимость, будет все очень
        похоже.
        Вспоминая всю цепочку доказательства, я заметил что нигде не записал её последнюю версию.
        Примерные воспоминания такие:
            необходимо построить формализованный алгоритм, такой что:
            1. с ним возможно будет найти значения индексов для каждого уровня
            2. он будет работать для любого натурального числа
            3. он будет основан на обратном переходе с началом в 1 (или 0 в другой
            нотации)
        Этого (по идее/как я понимаю) достаточно для доказательства гипотезы
        План к действию такой:
            1. описать merge/слияние первого и второго уровня, после полученного и третьего и т.д.
            2. для каждого получить обратные формулы/алгоритм расчета индексов в зависимости от значения самого числа
                этого можно достичь через нахождение глобального индекса как промежуточного звена на каждом этапе
                слияния
            3. Нужно перед описанием второго индекса понять как будут использоваться эти выражения и это даст понимание
            что от них требуется, в какой форме. Например, я могу ввести понятие функции дополняющей до полного
            ближайшего корня, но непонятно какие её свойства необходимо исследовать для доказательства.
            Итак, нужно разгадать тайну обратной функции от значения к клобальному индексу. Попытаться привести
            уравнение второго порядка к квадратному уравнению, посмотреть разницу индексов, возмонжо там будет вариация
            только остатков. Перед такими действиями необходимо привести в порядок текущие знания и подходы, которые
            у нас есть. Без этого невозможно дальше двигаться быстро. Думать больше на абстрактном уровне, не
            бояться вложенностей. Больше вводить обознаяений и просчитывать их на компьюторе. На больших уровнях пойдут
            куда более длинные периоды. Глазами не будет возможности обозреть что тут происходит. Необходимо
            использовать алгоритмы.
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
                int min = maxOf_l;
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
        {
            int i = 100; // vmod6(i=100) = 1
            int wrong_vmod6 = 5;
            List<Integer> spaceOfDefinition = getSpaceOfDefinition(
                    l -> function_V.apply(i, l, wrong_vmod6),
                    1000);
            System.out.println(spaceOfDefinition);
            spaceOfDefinition.forEach(l -> System.out.println(6 * function_V.apply(i, l, wrong_vmod6) + wrong_vmod6));
        }
        /*
            May be there is a more strong statement that for given i exists only one rest of v (mod 6) that fits for
                integer and positive value
            Update: it seems that right l is one point when v not only integer but simply positive. Other v with for
            wrong rest of v fall with growing of l
            So, we must find out the space of definition analytically
         */
        {
            List<Integer> another_l = new ArrayList<>(firstIndexes.size());
            for (int i = 0; i < firstIndexes.size(); i++) {
                int _i = i;
                int right_l = -1;
                for (int vMod6 = 0; vMod6 < 6; vMod6++) {
                    int _vMod6 = vMod6;
                    List<Integer> spaceOfDefinition = getSpaceOfDefinition(
                            l -> function_V.apply(_i, l, _vMod6),
                            1000);
                    if (!spaceOfDefinition.isEmpty()) {
                        for (Integer l : spaceOfDefinition) {
                            double v = 6 * function_V.apply(_i, l, _vMod6).intValue() + _vMod6;
                            if (v >= 0) {
                                if (right_l == -1) {
                                    right_l = l;
                                } else {
                                    System.out.println("it's not work");
                                }
                            }
                        }
                    }
                }
                another_l.add(right_l);
            }
            System.out.println(counts);
            System.out.println(another_l);
        }
        /*
            "it's not work" didn't appear. So, it looks like previous assumptions is correct and l has only one
            appropriate value in terms of space of definition. It takes hope that v function could be found analytically
            in more or less simple form.
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
