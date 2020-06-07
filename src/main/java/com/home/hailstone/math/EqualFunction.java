package com.home.hailstone.math;

public class EqualFunction implements Function {
    private final String variable;

    public EqualFunction(String variable) {
        this.variable = variable;
    }

    @Override
    public String toString() {
        return variable;
    }
}
