package com.home.hailstone.math;

import java.util.List;

import static com.home.hailstone.math.Util.palindromeCoefficient;

public class PalindromeFunction2 {
    private final List<CycleFunction2> coefficients;

    PalindromeFunction2(List<CycleFunction2> coefficients) {
        this.coefficients = coefficients;
    }

    public CycleFunction2 getCoefficient(int i) {
        return coefficients.get(i);
    }

    public int getSize() {
        return coefficients.size();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < coefficients.size() - 1; i++) {
            builder.append(coefficients.get(i))
                    .append("*")
                    .append(palindromeCoefficient(argument.toString(), i))
                    .append(" + ");
        }
        builder.append(coefficients.get(coefficients.size() - 1))
                .append("*")
                .append(palindromeCoefficient(argument.toString(), coefficients.size() - 1));
        return builder.toString();
    }
}
