package com.home.hailstone.math;

public class Value implements Function {
    private final int value;

    public Value(Integer value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
