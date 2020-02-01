package com.home.hailstone;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class Item {
    private List<Integer> indexes;
    private BigInteger value;
    private int levelIndex;

    public Item(BigInteger value, List<Integer> indexes) {
        this.indexes = indexes;
        this.value = value;
    }

    public Item getChild(BigInteger value, int index) {
        List<Integer> indexes = new ArrayList<>(this.indexes);
        indexes.add(index);
        return new Item(value, indexes);
    }

    public List<Integer> getIndexes() {
        return indexes;
    }

    public int getIndex(int i) {
        return indexes.get(i);
    }

    public BigInteger getValue() {
        return value;
    }

    public int getLevelIndex() {
        return levelIndex;
    }

    public void setLevelIndex(int levelIndex) {
        this.levelIndex = levelIndex;
    }
}
