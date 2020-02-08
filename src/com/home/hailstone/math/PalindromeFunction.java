package com.home.hailstone.math;

import java.util.List;

import static com.home.hailstone.math.Util.palindromeCoefficient;

public class PalindromeFunction implements Function {
    private final Function argument;
    private final List<? extends Function> coefficients;

    PalindromeFunction(Function argument, List<? extends Function> coefficients) {
        this.argument = argument;
        this.coefficients = coefficients;
    }

    public Function getArgument() {
        return argument;
    }

    public Function getCoefficient(int i) {
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
