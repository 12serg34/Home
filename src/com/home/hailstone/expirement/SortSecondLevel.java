package com.home.hailstone.expirement;

import com.home.hailstone.Item;
import com.home.hailstone.math.FunctionAnalyzer;
import com.home.hailstone.math.PalindromeFunction;
import com.home.hailstone.math.Util;
import com.home.hailstone.math.Value;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.IntUnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.home.hailstone.math.BigIntegerUtil.*;
import static com.home.hailstone.math.Util.getSpaceOfDefinition;
import static com.home.hailstone.math.Util.merge;
import static java.math.BigInteger.ONE;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

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
        /*
            l - level index. It's an index of value where it has place in some level.
         */
        /*
                We got the limit formula L1 took from one piece of space of definition.
             */
        /*
//                нужно исправить чтобы было не три индекса, а глобальный индекс от мержа второго уровня и третий индекс
//                    третий индекс остается тритм инексом и его повдение все же непредсказуемо. заметен медленный рост, но ничего точного сказать не получается
//                 */
        /*
            оказалось l в группировке первого индекса это по сути второй индекс. Код переписан с учетом этого, идет
             переосмысление происходящего, ведь это несколько неожиданно и все упрощает.
             Третий уровень показаал возрастание порядка. То есть. например, для него даже если и сложно получить
             обратные через ОДЗ, то для 5 и больших уровней это попросту нереально. Нужно добить мерж второго уровня,
             получить обратные для обоих индексов в простом виде и надеятся что на третьем уровне их простота сильна
             не изменится.
             Ну и конечно, третий уровень дал больший период в три раза как и предпологалось.

         */

public class SortSecondLevel {

    private static int[][] I = new int[][]{
            {2, 3, 4, 1, 2, 1},
            {1, 2, 3, 0, 3, 2}
    };

    private List<List<PalindromeFunction>> lByj1Functions;

    public static void main(String[] args) {
        SortSecondLevel sortSecondLevel = new SortSecondLevel();
        switch (args[0]) {
            case "2":
                sortSecondLevel.sortSecondLevel();
                break;
            case "3":
                sortSecondLevel.sortThirdLevel();
                break;
        }
    }

    private void sortSecondLevel() {
        println("It's a second level");
        int limitExponent = 36;
        println("Limit: " + limitExponent);
        BigInteger limit = BigInteger.valueOf(10).pow(limitExponent);

        List<Item> secondLevel = calculateLevel(2, limit);

        int levelSize = secondLevel.size();
        List<Integer> j0List = toIndexList(secondLevel, 0);
        List<Integer> j1List = toIndexList(secondLevel, 1);

        println("l: " + secondLevel.stream().map(Item::getLevelIndex).collect(toList()));
        println("j0: " + j0List);
        println("j1: " + j1List);
        println("second level: " + secondLevel.stream().map(Item::getValue).limit(100).collect(toList()));

        Map<List<Integer>, Item> jToItem = new HashMap<>();
        secondLevel.forEach(item -> jToItem.put(item.getIndexes(), item));

        FunctionAnalyzer analyzer = new FunctionAnalyzer();
        @SuppressWarnings("OptionalGetWithoutIsPresent") int j1Max = j1List.stream().max(Integer::compareTo).get();
        lByj1Functions = new ArrayList<>();
        for (int j0 = 0; j0 < 6 * 4; j0++) {
            List<Integer> lByj1 = new ArrayList<>();
            for (int j1 = 0; j1 <= j1Max; j1++) {
                Item item = jToItem.get(asList(j0, j1));
                if (item == null) {
                    break;
                }
                lByj1.add(item.getLevelIndex());
            }
            List<PalindromeFunction> palindromeFunctions = analyzer.analyze("j1", lByj1, 6);
            lByj1Functions.add(palindromeFunctions);
            System.out.printf("j0 = %1s, l(j1) = %2s\n", j0, merge(palindromeFunctions));
        }

        int[][] A = new int[6][6];
        int[][] B = new int[6][6];
        int[][] C = new int[6][6];
        for (int coefficientIndex = 0; coefficientIndex < 2; coefficientIndex++) {
            for (int j1Mod6 = 0; j1Mod6 < 6; j1Mod6++) {
                List<PalindromeFunction> functionsOfCoefficients =
                        analyzer.analyze(
                                "j0",
                                getCoefficients(j1Mod6, coefficientIndex), 6);
                if (coefficientIndex == 0) {
                    for (int j0Mod6 = 0; j0Mod6 < 6; j0Mod6++) {
                        A[j1Mod6][j0Mod6] = ((Value) functionsOfCoefficients.get(j0Mod6).getCoefficient(0).getAs()).getValue();
                        B[j1Mod6][j0Mod6] = ((Value) functionsOfCoefficients.get(j0Mod6).getCoefficient(1).getAs()).getValue();
                    }
                } else {
                    for (int j0Mod6 = 0; j0Mod6 < 6; j0Mod6++) {
                        C[j1Mod6][j0Mod6] = ((Value) functionsOfCoefficients.get(j0Mod6).getCoefficient(0).getAs()).getValue();
                    }
                }
                println("coefficients coefficientIndex = " + coefficientIndex
                        + ", j1 % 6 = " + j1Mod6 + ": " + merge(functionsOfCoefficients));
            }
        }
        println("A(j1, j0) = " + Arrays.deepToString(A));
        println("B(j1, j0) = " + Arrays.deepToString(B));
        println("C(j1, j0) = " + Arrays.deepToString(C));
        println("l(j0, j1) = " +
                "A(j1, j0) " +
                "+ B(j1, j0) * |j0 / 6| " +
                "+ 36 * S2(|j0 / 6|)\n" +
                "+ (C(j1, j0) + 36 * |j0 / 6|) * |j1 / 6|\n" +
                "+ 36 * S2(|j1 / 6|)");

        System.out.println("B - C = " + Arrays.deepToString(apply(B, C, (b, c) -> b - c)));
        int[][] T = apply(B, A, (b, a) -> (b - 18) * (b - 18) - 72 * a);
        int[][] BMinus18 = apply(B, b -> b - 18);
        System.out.println("B - 18 = " + Arrays.deepToString(BMinus18));
        System.out.println("has all values twice ((B - 18) / 2) % 18 = "
                + Arrays.deepToString(apply(BMinus18, x -> (x / 2) % 18)));
        System.out.println("C - 18 = " + Arrays.deepToString(apply(C, c -> c - 18)));
        System.out.println("T(j1, j0) = (B - 18)^2 - 72 * A = " + Arrays.deepToString(T));
        System.out.println("T / 4 = " + Arrays.deepToString(apply(T, x -> x / 4)));
        System.out.println("(T / 4) mod 3 = " + Arrays.deepToString(apply(T, x -> (x / 4) % 3)));
        System.out.println("(T / 4) mod 6 = " + Arrays.deepToString(apply(T, x -> (x / 4) % 6)));

        TetraFunction<Integer, Integer, Integer, Double> function_J0 = (l, j1, j0Mod6) -> {
            int J1 = j1 / 6;
            int decremental = T[j1 % 6][j0Mod6] + 72 * (l + 2 * J1);
            return (-(36 * J1 + B[j1 % 6][j0Mod6] - 18) + Math.sqrt(decremental)) / 36;
        };
        System.out.println("max j1 = " + j1Max);
        {
            // check of function_J0
            for (int l = 0; l < levelSize; l++) {
                int j0 = j0List.get(l);
                int j1 = j1List.get(l);
                int J0_to_check = (int) ((double) function_J0.apply(l, j1, j0 % 6));
                if ((j0 / 6) != J0_to_check) {
                    System.out.print("test failed: expected = " + (j0 / 6) + ", actual = " + J0_to_check);
                    break;
                }
            }
        }
        {
            List<Integer> indexesInSpaceOfDefinition = new ArrayList<>(j0List.size());
            for (int l = 0; l < levelSize; l++) {
                int final_l = l;
                List<Integer> spaceOfDefinition = getSpaceOfDefinition(
                        j1 -> function_J0.apply(final_l, j1, j0List.get(final_l) % 6),
                        j1Max);
                Integer j1 = j1List.get(l);
                indexesInSpaceOfDefinition.add(spaceOfDefinition.indexOf(j1));
            }
            System.out.println(indexesInSpaceOfDefinition);
        }
        {
            List<Integer> another_j1 = new ArrayList<>(j0List.size());
            for (int l = 0; l < levelSize; l++) {
                int final_l = l;
                int min = j1Max;
                for (int j0Mod6 = 0; j0Mod6 < 6; j0Mod6++) {
                    int _j0Mod6 = j0Mod6;
                    List<Integer> spaceOfDefinition = getSpaceOfDefinition(
                            j1 -> function_J0.apply(final_l, j1, _j0Mod6),
                            min);
                    if (!spaceOfDefinition.isEmpty()) {
                        Integer candidate = spaceOfDefinition.get(0);
                        if (candidate < min) {
                            min = candidate;
                        }
                    }
                }
                another_j1.add(min);
            }
            System.out.println(j1List);
            System.out.println(another_j1);
        }
        {
            int l = 100; // j0(l=100) % 6 = 1
            int wrong_j0mod6 = 5;
            List<Integer> spaceOfDefinition = getSpaceOfDefinition(
                    j1 -> function_J0.apply(l, j1, wrong_j0mod6),
                    1000);
            System.out.println(spaceOfDefinition);
            spaceOfDefinition.forEach(j1 -> System.out.println(6 * function_J0.apply(l, j1, wrong_j0mod6) + wrong_j0mod6));
        }
        {
            List<Integer> another_j1 = new ArrayList<>(j0List.size());
            for (int l = 0; l < levelSize; l++) {
                int final_l = l;
                int right_j1 = -1;
                for (int j0Mod6 = 0; j0Mod6 < 6; j0Mod6++) {
                    int final_j0Mod6 = j0Mod6;
                    List<Integer> spaceOfDefinition = getSpaceOfDefinition(
                            j1 -> function_J0.apply(final_l, j1, final_j0Mod6),
                            1000);
                    if (!spaceOfDefinition.isEmpty()) {
                        for (Integer j1 : spaceOfDefinition) {
                            double v = 6 * function_J0.apply(final_l, j1, final_j0Mod6).intValue() + final_j0Mod6;
                            if (v >= 0) {
                                if (right_j1 == -1) {
                                    right_j1 = j1;
                                } else {
                                    System.out.println("it's not work");
                                }
                            }
                        }
                    }
                }
                another_j1.add(right_j1);
            }
            System.out.println(j1List);
            System.out.println(another_j1);
        }
        {
            List<Integer> lSubstituteA = IntStream.range(0, levelSize)
                    .map(l -> l - A[j1List.get(l) % 6][j0List.get(l) % 6])
                    .boxed()
                    .collect(toList());
            System.out.println("l - A(j1(l), j0(l)) = " + lSubstituteA); // all values more or equal zero
        }
        {
            List<Integer> restByl = IntStream.range(0, levelSize)
                    .map(l -> 18 * l + T[j1List.get(l) % 6][j0List.get(l) % 6] / 4)
                    .distinct()
                    .boxed()
                    .collect(toList());
            System.out.println("distinct of 18*l + T(j1(l), j0(l)) / 4 = " + restByl);

            for (int i = 0; i < 20; i++) {
                int finalI = i;
                List<Integer> spaceOfDefinition = Util.getSpaceOfDefinition(
                        y -> Math.sqrt(36 * y + restByl.get(finalI)),
                        10000);
                List<PalindromeFunction> analyze = analyzer.analyze("e", spaceOfDefinition, 2);
                System.out.printf("y(e) = %1s\n", analyze);
            }
        }
        {
            List<Integer> data = IntStream.range(0, levelSize)
                    .map(l -> {
                        int j1Mod6 = j1List.get(l) % 6;
                        int j0Mod6 = j0List.get(l) % 6;
                        return (18 * l + T[j1Mod6][j0Mod6] / 4) % 36;
//                        int b = (int) Math.sqrt();
//                        return b - BMinus18[j1Mod6][j0Mod6] / 2;
                    })
                    .distinct()
                    .sorted()
                    .boxed()
                    .collect(toList());
            System.out.println("b - m01 / 2 = " + data);
        }
        {
            // coefficients of p (index of y in fact of j1) from Y and B
            int[][] k00 = {
                    {0, 0, 0, 0, 0, 0, 0, 0, 0},
                    {1, 8, 7, 2, 5, 4, 3, 2, 1}};
            int[][] k01 = {{0, 0, 0, 0, 0, 0, 0, 0, 0},
                    {3, 8, 7, 3, 5, 4, 3, 2, 1}};
//            int[][] k10 = {
//                    {4, 10, 11, 6, 13, 14, 8, 16, 17},
//                    {8, 26, 25, 10, 23, 22, 12, 20, 19}};
            int[][] k11 = {
                    {6, 9, 9, 6, 9, 9, 6, 9, 9},
                    {6, 9, 9, 6, 9, 9, 6, 9, 9}};
            int[][] k20 = {
                    {8, 18, 18, 8, 18, 18, 8, 18, 18},
                    {8, 18, 18, 8, 18, 18, 8, 18, 18}};

            IntegerSquares.Calculator calculator = new IntegerSquares.Calculator();

            IntUnaryOperator J1Function = l -> {
                int j1Mod6 = j1List.get(l) % 6; // 7
                int j0Mod6 = j0List.get(l) % 6; // 32
                int k0 = 18 * l + T[j1Mod6][j0Mod6] / 4;
                int k10 = (BMinus18[j1Mod6][j0Mod6] / 2 + 18) % 18;
//                int pBy0 = k0 % 3 == 0 ? cycle(valueOf(k10 / 6), 0, 2, 1) : (k10 == 0 ? 0 : 1);
//                if (k0 % 3 != 0) {
////                    if ((9 * pBy0 + cycle(valueOf(b), 0, 7, 5, 0, 1, -1, 0, -5, -7) * (pBy0 % 2) + t0 + 18) % 18 != 0) {
////                        System.out.println("alarm " + l);
////                    }
//                }
//                int P = pBy0 / 2;
//                int B2 = k0 / 9;
//                int pMod2 = pBy0 % 2;
//                int bMod9 = k0 % 9;
//                int J1 = k00[pMod2][bMod9] + k01[pMod2][bMod9] * B2
//                        + k10[pMod2][bMod9] * P + k11[pMod2][bMod9] * B2 * P
//                        + k20[pMod2][bMod9] * ((P - 1) * P / 2);
//                        return 2 * k11[pMod2][bMod9] * P + 9 * B2 + 2 * k01[pMod2][bMod9] + bMod9;
                int k1 = calculator.k1(k0, k10, 0);
                int j1 = (k1 * k1 - k0) / 36;
                return j1;
            };
            IntUnaryOperator J0Function = l -> {
                int j1Mod6 = j1List.get(l) % 6; // 7
                int j0Mod6 = j0List.get(l) % 6; // 32
                int k0 = 18 * l + T[j1Mod6][j0Mod6] / 4;
                int k10 = (BMinus18[j1Mod6][j0Mod6] / 2 + 18) % 18;
//                int pBy0 = k0 % 3 == 0 ? cycle(valueOf(k10 / 6), 0, 2, 1) : (k10 == 0 ? 0 : 1);
//                if (k0 % 3 != 0) {
////                    if ((9 * pBy0 + cycle(valueOf(b), 0, 7, 5, 0, 1, -1, 0, -5, -7) * (pBy0 % 2) + t0 + 18) % 18 != 0) {
////                        System.out.println("alarm " + l);
////                    }
//                }
//                int P = pBy0 / 2;
//                int B2 = k0 / 9;
//                int pMod2 = pBy0 % 2;
//                int bMod9 = k0 % 9;
//                int J1 = k00[pMod2][bMod9] + k01[pMod2][bMod9] * B2
//                        + k10[pMod2][bMod9] * P + k11[pMod2][bMod9] * B2 * P
//                        + k20[pMod2][bMod9] * ((P - 1) * P / 2);
//                        return 2 * k11[pMod2][bMod9] * P + 9 * B2 + 2 * k01[pMod2][bMod9] + bMod9;
                int k1 = calculator.k1(k0, k10, 0);
                int J1 = (k1 * k1 - k0) / 36;
                int J0 = (k1 - (BMinus18[j1Mod6][j0Mod6] / 2) - 18 * J1) / 18;
                return J0;
            };
            System.out.println("J1(823) = " + J1Function.applyAsInt(823));
            System.out.println("real J1(823) = " + (j1List.get(823) / 6));
            List<Integer> data = IntStream.range(0, levelSize)
                    .map(J1Function)
                    .boxed()
                    .collect(toList());
            System.out.println("another J1 = " + data);
            System.out.println("real J1 =    " + j1List.stream()
                    .map(j1 -> j1 / 6)
                    .collect(toList()));
            List<Integer> J0 = IntStream.range(0, levelSize)
                    .map(J0Function)
                    .boxed()
                    .collect(toList());
            System.out.println("another J0 = " + J0);
            System.out.println("real J0 =    " + j0List.stream()
                    .map(j0 -> j0 / 6)
                    .collect(toList()));
        }
        {
            List<Double> J1_1_List = IntStream.range(0, levelSize)
                    .mapToDouble(l -> {
                        int j0 = j0List.get(l) % 6;
                        int j1 = j1List.get(l) % 6;
                        int d = B[j1][j0] - 18;
                        int a = A[j1][j0];
                        return ((-(d - 2) + Math.sqrt((d - 2) * (d - 2) + 2 * 36 * (l - a))) / 36);
                    })
                    .boxed()
                    .collect(toList());
            List<Integer> J1List = IntStream.range(0, levelSize)
                    .map(l -> j1List.get(l) / 6)
                    .boxed()
                    .collect(toList());
            IntStream.range(0, levelSize)
                    .forEach(l -> {
                        Integer J1 = J1List.get(l);
                        Double J1_1 = J1_1_List.get(l);
                        if (J1 > J1_1) {
                            System.out.println("limit doesn't work: l = " + l + ", J1 = " + J1 + ", J1_1 = " + J1_1);
                        }
                    });
        }
        {
            int[][] D = apply(B, b -> b - 18);
            int[][] G = apply(D, d -> d % 36);
            System.out.println("(G ^ 2 - T) / 72 = " + Arrays.deepToString(apply(G, T, (g, t) -> ((g * g - t) / 72))));
        }
    }

    public void sortThirdLevel() {
        println("It's a third level");
        int limitExponent = 100;
        println("Limit: " + limitExponent);
        BigInteger limit = BigInteger.valueOf(10).pow(limitExponent);

        List<Item> thirdLevel = calculateLevel(3, limit);
        println("thirdLevel", thirdLevel.stream().map(Item::getValue).collect(toList()));

        List<Integer> firstIndexes = toIndexList(thirdLevel, 0);
        println("first indexes", firstIndexes);

        List<Integer> secondIndexes = toIndexList(thirdLevel, 1);
        println("second indexes", secondIndexes);

        List<Integer> secondLevelIndexes = thirdLevel.stream()
                .map(it -> it.getLevelIndex(0))
                .collect(toList());
        println("second level indexes", secondLevelIndexes);

        List<Integer> j2List = thirdLevel.stream()
                .map(it -> it.getIndex(2))
                .collect(toList());
        println("third indexes", j2List);

        @SuppressWarnings("OptionalGetWithoutIsPresent") int j2Max = j2List.stream().max(Integer::compareTo).get();
        FunctionAnalyzer analyzer = new FunctionAnalyzer();

        {
            List<Item> grouped = thirdLevel.stream()
                    .sorted((it1, it2) -> {
                        int compareFirstIndex = Integer.compare(it1.getFirstIndex(), it2.getFirstIndex());
                        if (compareFirstIndex == 0) {
                            int compareSecondIndex = Integer.compare(it1.getSecondIndex(), it2.getSecondIndex());
                            if (compareSecondIndex == 0) {
                                return Integer.compare(it1.getIndex(2), it2.getIndex(2));
                            }
                            return compareSecondIndex;
                        }
                        return compareFirstIndex;
                    })
                    .collect(toList());
            println(grouped.stream().map(Item::getFirstIndex).collect(toList()));
            println(grouped.stream().map(Item::getSecondIndex).collect(toList()));
            println(grouped.stream().map(it -> it.getIndex(2)).collect(toList()));
            println(grouped.stream().map(it -> it.getLevelIndex(1)).collect(toList()));
        }
        {
            Map<List<Integer>, Item> jToItem = thirdLevel.stream().collect(toMap(Item::getIndexes, Function.identity()));
            List<Integer> lByj2 = new ArrayList<>();
            for (int j2 = 0; j2 <= j2Max; j2++) {
                Item item = jToItem.get(asList(0, 0, j2));
                if (item == null) {
                    break;
                }
                lByj2.add(item.getLevelIndex());
            }
            List<PalindromeFunction> palindromeFunctions = analyzer.analyze("j2", lByj2, 18);
            System.out.println(palindromeFunctions);
        }
        {
            Map<List<Integer>, Item> l2Andj2ToItem = thirdLevel.stream().collect(toMap(
                    item -> asList(item.getLevelIndex(0), item.getIndex(2)),
                    Function.identity()));

            List<Integer> lByj2 = new ArrayList<>();
            for (int j2 = 0; j2 <= j2Max; j2++) {
                Item item = l2Andj2ToItem.get(asList(4, j2));
                if (item == null) {
                    break;
                }
                lByj2.add(item.getLevelIndex());
            }
            println("l(l2, j2) = ", lByj2);
            List<PalindromeFunction> j2 = analyzer.analyze("j2", lByj2, 18);
            println("l(l2, j2) = " + j2);
            {
                PalindromeFunction function = j2.get(0);
                int k0 = getCoefficient(function, 0);
                int k1 = getCoefficient(function, 1);
                int k2 = getCoefficient(function, 2);
                int k3 = getCoefficient(function, 3);

                int c0 = k0;
                int c1 = (2 * k3 - 3 * k2 + 6 * k1) / 6;
                int c2 = -(k3 - k2) / 2;
                int c3 = k3 / 6;
                println("c = " + asList(c0, c1, c2, c3));

            }
        }
    }

    private int getCoefficient(PalindromeFunction palindrome, int i) {
        return ((Value) palindrome.getCoefficient(i).getAs()).getValue();
    }

    private List<Integer> toIndexList(List<Item> data, int index) {
        return data.stream()
                .map(item -> item.getIndex(index))
                .collect(toList());
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

    private List<Integer> getCoefficients(int j1Mod6, int coefficientIndex) {
        return lByj1Functions.stream()
                .map(lByj1 -> {
                    Value value = lByj1.get(j1Mod6).getCoefficient(coefficientIndex).getAs();
                    return value.getValue();
                }).collect(toList());
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
        Item one = new Item(ONE, emptyList(), new ArrayList<>());
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
        childLevel.sort(Comparator.comparing(Item::getValue));
        for (int i = 0; i < childLevel.size(); i++) {
            childLevel.get(i).addLevelIndex(i);
        }
        return childLevel;
    }

    private void println(Object object) {
        System.out.println(object.toString());
    }

    private void println(String prefix, Collection<?> collection) {
        System.out.println(prefix + ": " + collection.stream().limit(100).collect(Collectors.toList()));
    }

    private BigInteger C(BigInteger P, int j) {
        return P.multiply(powOf2(i(P, j))).subtract(ONE)
                .divide(THREE);
    }

    private int i(BigInteger P, int j) {
        return 3 * j + cycle(j, R(P), I);
    }
}
