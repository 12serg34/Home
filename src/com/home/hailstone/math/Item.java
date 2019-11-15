package com.home.hailstone.math;

import java.util.List;

public class Item {
    private int index;
    private int value;

    Item(int index, int value) {
        this.index = index;
        this.value = value;
    }

    int getValue() {
        return value;
    }

    private int getIndex() {
        return index;
    }

    @Override
    public String toString() {
        return String.valueOf(index);
    }

    static int[] extractIndexes(List<Item> items) {
        return items.stream()
                .mapToInt(Item::getIndex)
                .toArray();
    }

    static int[] extractValues(List<Item> items) {
        return items.stream()
                .mapToInt(Item::getValue)
                .toArray();
    }
}
