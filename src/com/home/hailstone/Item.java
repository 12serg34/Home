package com.home.hailstone;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Item<T> {
    private List<Integer> indexes;
    private List<Integer> sortedIndexes;
    private T value;

    private Item(T value, List<Integer> indexes, List<Integer> sortedIndexes) {
        this.indexes = indexes;
        this.sortedIndexes = sortedIndexes;
        this.value = value;
    }

    public Item(T value, int... indexes) {
        this.indexes = Arrays.stream(indexes)
                .boxed()
                .collect(Collectors.toList());
        this.sortedIndexes = new ArrayList<>();
        this.value = value;
    }

    public T getValue() {
        return value;
    }

    public int getFirstIndex() {
        return indexes.get(0);
    }

    public int getSecondIndex() {
        return indexes.get(1);
    }

    public int getIndex(int i) {
        return indexes.get(i);
    }

    public int getSortedIndex(int i) {
        return sortedIndexes.get(i);
    }

    public Item<T> getChild(T value, int index) {
        ArrayList<Integer> indexes = new ArrayList<>(this.indexes);
        indexes.add(index);
        return new Item<T>(value, indexes, new ArrayList<>(sortedIndexes));
    }

    public void addSortedIndex(int index) {
        sortedIndexes.add(index);
    }
}
