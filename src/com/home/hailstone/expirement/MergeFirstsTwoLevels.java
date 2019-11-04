package com.home.hailstone.expirement;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.home.hailstone.math.Util.*;

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
        System.out.println("Hello, it's merge of two firsts levels");
        int limit = 1000;
        List<Item> collect = IntStream.range(0, 4)
                .mapToObj(i1 -> new Item(i1, -1, x(1, i1)))
                .flatMap(l1 -> IntStream.range(0, 4)
                        .mapToObj(i2 -> new Item(l1.i1, i2, x(l1.value, i2))))
                .sorted(Comparator.comparingInt(it1 -> it1.value))
                .collect(Collectors.toList());
        System.out.println(collect);
        System.out.println(collect.stream()
                .map(it -> it.i1)
                .collect(Collectors.toList()));
        System.out.println(collect.stream()
                .map(it -> it.i2)
                .collect(Collectors.toList()));
        System.out.println(collect.stream()
                .map(it -> it.value)
                .collect(Collectors.toList()));
    }

//    private List<Integer> getFirstLevel(int limit

    private int x(int y, int k) {
        return reverse2(reverse1((forward1(forward2(y)) * powOf2(i(y, k)) - 1)
                / 3));
    }

    private int i(int y, int k) {
        return 2 * j(y, k) + invertMod2(y % 2) + 1;
    }

    private int j(int y, int k) {
        return 3 * (k / 2) + theta(e1(y) + 2, k);
    }

    private int e1(int y) {
        return reverse1(
                (forward1(forward2(y)) * powOf2(invertMod2(y % 2)) - 1)
                        / 3
        );
    }

    private int theta(int i, int j) {
        return cycle(j, cycle(i, 1, 0, 0), cycle(i, 2, 2, 1));
    }

    static class Item {
        int i1;
        int i2;
        int value;

        public Item(int i1, int i2, int value) {
            this.i1 = i1;
            this.i2 = i2;
            this.value = value;
        }

        @Override
        public String toString() {
            return String.format("i1 = %1d, i2 = %2d, v = %3d", i1, i2, value);
        }
    }
}
