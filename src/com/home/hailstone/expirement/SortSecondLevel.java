package com.home.hailstone.expirement;

import com.home.hailstone.Item;
import com.home.hailstone.math.Function;
import com.home.hailstone.math.FunctionAnalyzer;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BinaryOperator;
import java.util.function.IntFunction;
import java.util.stream.IntStream;

import static com.home.hailstone.math.BigIntegerUtil.*;
import static java.math.BigInteger.ONE;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
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
/*
        Next task:
            Implement algorithm that took function f(x) (x - integer, f(x) - double) and found first x that give
            integer f(x).
            Vary i in discriminant of v formula, check that works for l(i).
            Another variant, build table i/l and underline(or mark somehow) real function l(i), try to guess how it can be found,
            what's relation between them in scope of E(D(i)) (space of definition for i and l(i))
         */
/*
            all values are zero therefore l in function v(l, i) it's a first value that fits space of definition of
            v(i,l) function e.g. E[v(i, l)]
        */
/*
            May be there is a more strong statement that for given i exists only one rest of v (mod 6) that fits for
                integer and positive value
            Update: it seems that right l is one point when v not only integer but simply positive. Other v with for
            wrong rest of v fall with growing of l
            So, we must find out the space of definition analytically
         */
/*
            values of counts - "l" are equal values of another_l that were build as min value of first value in space
            of definitions of six partial functions of different rest of "v". Therefore l in function v(l, i)
            it's a first value that fits space of definition of v(i,l) function e.g. E[v(i, l)]
        */
        /*
            "it's not work" didn't appear. So, it looks like previous assumptions is correct and l has only one
            appropriate value in terms of space of definition. It takes hope that v function could be found analytically
            in more or less simple form.
         */

public class SortSecondLevel {

    private static int[][] I = new int[][]{
            {2, 3, 4, 1, 2, 1},
            {1, 2, 3, 0, 3, 2}
    };

    private List<List<Function>> levelIndexesFromj1;

    public static void main(String[] args) {
        new SortSecondLevel().run();
    }

    private void run() {
        println("It's a second level");
        int limitExponent = 36;
        println("Limit: " + limitExponent);
        BigInteger limit = BigInteger.valueOf(10).pow(limitExponent);

        List<Item> secondLevel = calculateLevel(2, limit);
        secondLevel.sort(Comparator.comparing(Item::getValue));
        IntStream.range(0, secondLevel.size())
                .forEach(levelIndex -> secondLevel.get(levelIndex)
                        .setLevelIndex(levelIndex));

        List<Integer> j0List = toIndexList(secondLevel, 0);
        println("j0: " + j0List);
        List<Integer> j1List = toIndexList(secondLevel, 1);
        println("j1: " + j1List);
        println("second level: " + secondLevel.stream().map(Item::getValue).limit(100).collect(toList()));

        Map<List<Integer>, Item> jToLevelIndex = new HashMap<>();
        secondLevel.forEach(item -> jToLevelIndex.put(item.getIndexes(), item));
        FunctionAnalyzer analyzer = new FunctionAnalyzer();
        @SuppressWarnings("OptionalGetWithoutIsPresent") int j0Max = j0List.stream().max(Integer::compareTo).get();
        @SuppressWarnings("OptionalGetWithoutIsPresent") int j1Max = j1List.stream().max(Integer::compareTo).get();
        levelIndexesFromj1 = new ArrayList<>();
        int period = 6;
        for (int j0 = 0; j0 < period * 4; j0++) {
            List<Integer> data = new ArrayList<>();
            for (int j1 = 0; j1 <= j1Max; j1++) {
                Item item = jToLevelIndex.get(asList(j0, j1));
                if (item == null) {
                    break;
                }
                data.add(item.getLevelIndex());
            }
            List<Function> functions = analyzer.analyze(data, period);
            levelIndexesFromj1.add(functions);
            System.out.printf("level index from j1 at j0 = %1s is: %2s\n", j0, functions);
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

        System.out.println("B - C = " + Arrays.deepToString(apply(B, C, (b, c) -> b - c)));
        int[][] T = apply(B, A, (b, a) -> (b - 18) * (b - 18) - 72 * a);
        System.out.println("B - 18 = " + Arrays.deepToString(apply(B, b -> b - 18)));
        System.out.println("T(l, v) = (B - 18)^2 - 72 * A = " + Arrays.deepToString(T));
        System.out.println("T / 4 = " + Arrays.deepToString(apply(T, x -> x / 4)));
        System.out.println("(T / 4) mod 3 = " + Arrays.deepToString(apply(T, x -> (x / 4) % 3)));
        System.out.println("(T / 4) mod 6 = " + Arrays.deepToString(apply(T, x -> (x / 4) % 6)));

        TetraFunction<Integer, Integer, Integer, Double> function_V = (i, l, vMod6) -> {
            int L = l / 6;
            int decremental = T[l % 6][vMod6] + 72 * (i + 2 * L);
            return (-(36 * L + B[l % 6][vMod6] - 18) + Math.sqrt(decremental)) / 36;
        };
        System.out.println("max l = " + (int) j1Max);
        {
            // check of function_V
            for (int i = 0; i < j0List.size(); i++) {
                int v = j0List.get(i);
                int l = j1List.get(i);
                int V_to_check = (int) ((double) function_V.apply(i, l, v % 6));
                if ((v / 6) != V_to_check) {
                    System.out.print("test failed: expected = " + (v / 6) + ", actual = " + V_to_check);
                    break;
                }
            }
        }
        {
            List<Integer> indexesInSpaceOfDefinition = new ArrayList<>(j0List.size());
            for (int i = 0; i < j0List.size(); i++) {
                int ii = i;
                List<Integer> spaceOfDefinition = getSpaceOfDefinition(
                        l -> function_V.apply(ii, l, j0List.get(ii) % 6),
                        j1Max);
                Integer l = j1List.get(i);
//            System.out.println(spaceOfDefinition + " l(i) = " + l);
                indexesInSpaceOfDefinition.add(spaceOfDefinition.indexOf(l));
            }
            System.out.println(indexesInSpaceOfDefinition);
        }

        {
            List<Integer> another_l = new ArrayList<>(j0List.size());
            for (int i = 0; i < j0List.size(); i++) {
                int _i = i;
                int min = j1Max;
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
            System.out.println(j1List);
            System.out.println(another_l);
        }

        {
            int i = 100; // vmod6(i=100) = 1
            int wrong_vmod6 = 5;
            List<Integer> spaceOfDefinition = getSpaceOfDefinition(
                    l -> function_V.apply(i, l, wrong_vmod6),
                    1000);
            System.out.println(spaceOfDefinition);
            spaceOfDefinition.forEach(l -> System.out.println(6 * function_V.apply(i, l, wrong_vmod6) + wrong_vmod6));
        }

        {
            List<Integer> another_l = new ArrayList<>(j0List.size());
            for (int i = 0; i < j0List.size(); i++) {
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
            System.out.println(j1List);
            System.out.println(another_l);
        }


        List<Integer> vmod6 = j0List.stream().map(x -> x % 6).collect(toList());
        System.out.println("v(i) % 6 = " + vmod6);
        System.out.println("A[l, v] % 6 = " + Arrays.deepToString(apply(A, a -> a % 6)));
        System.out.println("B[l, v] % 6 = " + Arrays.deepToString(apply(B, b -> b % 6)));
        System.out.println("C[l, v] % 6 = " + Arrays.deepToString(apply(C, c -> c % 6)));

        int[][] O = new int[6][6];
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                O[i][j] = i * 6 + j;
            }
        }
        List<Integer> lmod6 = j1List.stream().map(x -> x % 6).collect(toList());
        List<Integer> Olv = IntStream.range(0, j0List.size())
                .mapToObj(i -> O[lmod6.get(i)][vmod6.get(i)])
                .collect(toList());
        System.out.println("O(l,v) = " + Olv);

        {
            List<Integer> iSubstituteA = IntStream.range(0, j0List.size())
                    .map(i -> i - A[j1List.get(i) % 6][j0List.get(i) % 6])
                    .boxed()
                    .collect(toList());
            System.out.println("i - A(l(i), v(i)) = " + iSubstituteA); // all values more or equal zero
        }

        {
            System.out.println("(x * x) % 18 = " + IntStream.range(0, 18)
                    .map(x -> (x * x) % 18)
                    .boxed()
                    .collect(toList()));
        }

        {
            List<Double> L1 = IntStream.range(0, j0List.size())
                    .mapToDouble(i -> {
                        int l = j1List.get(i) % 6;
                        int v = j0List.get(i) % 6;
                        int d = B[l][v] - 18;
                        int a = A[l][v];
                        return ((-(d - 2) + Math.sqrt((d - 2) * (d - 2) + 2 * 36 * (i - a))) / 36);
                    })
                    .boxed()
                    .collect(toList());
            List<Integer> L = IntStream.range(0, j0List.size())
                    .map(i -> j1List.get(i) / 6)
                    .boxed()
                    .collect(toList());
            IntStream.range(0, j0List.size())
                    .forEach(i -> {
                        Integer l = L.get(i);
                        Double l1 = L1.get(i);
                        if (l > l1) {
                            System.out.println("i = " + i + ", L = " + l + ", L1 = " + l1);
                        }
                    });
            /*
                We got the limit formula L1 took from one piece of space of definition.
             */
        }
        {
            int[][] D = apply(B, b -> b - 18);
            int[][] G = apply(D, d -> d % 36);
            System.out.println("(G ^ 2 - T) / 72 = " + Arrays.deepToString(apply(G, T, (g, t) -> ((g * g - t) / 72))));
        }
        {
//            List<Item> thirdLevel = calculateLevel(3, limit);
//            println("thirdLevel", thirdLevel.stream().map(Item::getValue).collect(toList()));
//
//            List<Integer> firstIndexes = toIndexList(thirdLevel);
//            println("first indexes" + firstIndexes);
//
//            List<Integer> secondIndexes = thirdLevel.stream()
//                    .map(Item::getSecondIndex)
//                    .collect(toList());
//            println("second indexes" + secondIndexes);
//
//            List<Integer> secondSortedIndexes = thirdLevel.stream()
//                    .map(it -> it.getSortedIndex(0))
//                    .collect(toList());
//            println("sorted indexes from second level" + secondSortedIndexes);
//
//            List<Integer> thirdIndexes = thirdLevel.stream()
//                    .map(it -> it.getIndex(2))
//                    .collect(toList());
//            println("third indexes" + thirdIndexes);
//
//            Map<Integer, List<Integer>> groupOfSecondSortedIndexes = groupByValues(secondSortedIndexes);
//            println("Group of secondSortedIndexes:" + groupOfSecondSortedIndexes);
//            FunctionAnalyzer analyzer = new FunctionAnalyzer();
//
//            for (int v = 0; v < 10; v++) {
//                println("T = 18, v = " + v + ", " + analyzer.analyze(groupOfSecondSortedIndexes.get(v), 18));
//            }
//
//            Map<Integer, List<Integer>> groupOfThirdIndexes = groupByValues(thirdIndexes);
//            println(analyzer.analyze(groupOfThirdIndexes.get(0), 18)); // doesn't work :(
//
//            {
//                List<Item> grouped = thirdLevel.stream()
//                        .sorted((it1, it2) -> {
//                            int compareFirstIndex = Integer.compare(it1.getFirstIndex(), it2.getFirstIndex());
//                            if (compareFirstIndex == 0) {
//                                int compareSecondIndex = Integer.compare(it1.getSecondIndex(), it2.getSecondIndex());
//                                if (compareSecondIndex == 0) {
//                                    return Integer.compare(it1.getIndex(2), it2.getIndex(2));
//                                }
//                                return compareSecondIndex;
//                            }
//                            return compareFirstIndex;
//                        })
//                        .collect(toList());
//                println(grouped.stream().map(Item::getFirstIndex).collect(toList()));
//                println(grouped.stream().map(Item::getSecondIndex).collect(toList()));
//                println(grouped.stream().map(it -> it.getIndex(2)).collect(toList()));
//                println(grouped.stream().map(it -> it.getSortedIndex(1)).collect(toList()));
//            }

//            {
//                List<Integer> data = groupOfFirstIndexes.get(0);
//                for (int T = 1000; T <= 1128; T++) {
//                    List<Function> functions = analyzer.analyze(data, T);
//                    println("first index, value = " + T + ": " + functions);
//                }
//                /*
//                нужно исправить чтобы было не три индекса, а глобальный индекс от мержа второго уровня и третий индекс
//                    третий индекс остается тритм инексом и его повдение все же непредсказуемо. заметен медленный рост, но ничего точного сказать не получается
//                 */
//            }

//            functionsByValues = new ArrayList<>();
//            for (int v = 0; v < groupOfFirstIndexes.size(); v++) {
//                List<Integer> data = groupOfFirstIndexes.get(v);
//                if (data.size() < 18) {
//                    continue;
//                }
//                List<Function> functions = analyzer.analyze(data, 18);
//                functionsByValues.add(functions);
//                println("first index, value = " + v + ": " + functions);
//            }
        }
    }

    private List<Integer> toIndexList(List<Item> data, int index) {
        return data.stream()
                .map(item -> item.getIndex(index))
                .collect(toList());
    }

    private List<Item> sortByValues(List<Item> level) {
        return level.stream()
                .sorted(Comparator.comparing(Item::getValue))
                .collect(toList());
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
        return levelIndexesFromj1.stream()
                .map(f -> f.get(t).getCoefficient(c))
                .collect(toList());
    }

    private List<Item> branch(Item parent, BigInteger limit) {
        List<Item> branch = new ArrayList<>();
        for (int j = 0; ; j++) {
            Item child = calculateChild(parent, j);
            if (child.getValue().compareTo(limit) <= 0) {
                branch.add(child);
            } else break;
        }
        return branch;
    }

    private Item calculateChild(Item parent, int j) {
        BigInteger childValue = C(parent.getValue(), j);
        return parent.getChild(childValue, j);
    }

    private List<Item> calculateLevel(int level, BigInteger limit) {
        if (level == 1) {
            return calculateFirstLevel(limit);
        }
        BigInteger parentLimit = getParentUpLimit(limit);
        List<Item> parentLevel = calculateLevel(level - 1, parentLimit);
        return branch(parentLevel, limit);
    }

    private List<Item> calculateFirstLevel(BigInteger limit) {
        Item one = new Item(ONE, emptyList());
        return branch(one, limit);
    }

    private BigInteger getParentUpLimit(BigInteger child) {
        return THREE.multiply(child).add(ONE)
                .divide(TWO);
    }

    private List<Item> branch(List<Item> parentLevel, BigInteger limit) {
        List<Item> childLevel = new ArrayList<>();
        for (Item parent : parentLevel) {
            childLevel.addAll(branch(parent, limit));
        }
        return childLevel;
    }

    private void println(Object object) {
        System.out.println(object.toString());
    }

    private void println(String prefix, List<?> list) {
        StringBuilder text = new StringBuilder(prefix + ": ");
        if (list.size() < 100) {
            text = text.append(list);
        } else {
            text = text.append("[");
            for (int i = 0; i < 100; i++) {
                text = text.append(list.get(i) + ", ");
            }
        }
        System.out.println(text);
    }


    private BigInteger C(BigInteger P, int j) {
        return P.multiply(powOf2(i(P, j))).subtract(ONE)
                .divide(THREE);
    }

    private int i(BigInteger P, int j) {
        return 3 * j + cycle(j, R(P), I);
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
