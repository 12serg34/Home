package com.home.hailstone.math;

import java.util.List;

public class Function {
    private List<Integer> coefficients;

    Function(List<Integer> coefficients) {
        this.coefficients = coefficients;
    }

    public int getCoefficient(int i) {
        return coefficients.get(i);
    }

    public int getSize() {
        return coefficients.size();
    }

    @Override
    public String toString() {
        return coefficients.toString();
    }
}
