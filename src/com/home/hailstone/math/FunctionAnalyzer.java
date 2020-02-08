package com.home.hailstone.math;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class FunctionAnalyzer {
    private int depth;

    public FunctionAnalyzer() {
        this(10);
    }

    private FunctionAnalyzer(int depth) {
        this.depth = depth;
    }

    private PalindromeFunction analyze(String variable, List<Integer> data) {
        List<Value> coefficients = new ArrayList<>();
        coefficients.add(new Value(data.get(0)));
        int currentDepth = 0;
        while (currentDepth < depth && !checkForConstant(data)) {
            data = Util.diff(data);
            coefficients.add(new Value(data.get(0)));
            currentDepth++;
        }
        return new PalindromeFunction(new EqualFunction(variable), coefficients);
    }

    public List<PalindromeFunction> analyze(String variable, List<Integer> data, int period) {
        return Util.split(data, period).stream()
                .map(list -> analyze(variable, list))
                .collect(toList());
    }

    private static boolean checkForConstant(List<Integer> data) {
        int firstValue = data.get(0);
        for (int i = 1; i < data.size(); i++) {
            if (data.get(i) != firstValue) {
                return false;
            }
        }
        return true;
    }
}
