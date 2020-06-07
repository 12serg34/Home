package com.home.hailstone;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Item {
    private List<Integer> indexes;
    private List<Integer> levelIndexes;
    private BigInteger value;

    public Item(BigInteger value, List<Integer> indexes, List<Integer> levelIndexes) {
        this.indexes = indexes;
        this.levelIndexes = levelIndexes;
        this.value = value;
    }

    public BigInteger getValue() {
        return value;
    }

    public Item getChild(BigInteger value, int index) {
        List<Integer> indexes = new ArrayList<>(this.indexes);
        indexes.add(index);
        return new Item(value, indexes, new ArrayList<>(levelIndexes));
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

    public List<Integer> getIndexes() {
        return indexes;
    }

    public int getLevelIndex() {
        return levelIndexes.get(levelIndexes.size() - 1);
    }

    public int getLevelIndex(int i) {
        return levelIndexes.get(i);
    }

    public void addLevelIndex(int index) {
        levelIndexes.add(index);
    }
}
