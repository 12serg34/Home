package com.home.hailstone.math;

public class DivideFunction implements Function {
    private final Function argument;
    private final int denominator;

    public DivideFunction(Function argument, int denominator) {
        this.argument = argument;
        this.denominator = denominator;
    }

    @Override
    public String toString() {
        return argument +
                "/" +
                denominator;
    }
}
