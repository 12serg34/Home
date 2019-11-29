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

    private Function analyze(List<Integer> data) {
        List<Integer> coefficients = new ArrayList<>();
        coefficients.add(data.get(0));
        int currentDepth = 0;
        while (currentDepth < depth && !checkForConstant(data)) {
            data = Util.diff(data);
            coefficients.add(data.get(0));
            currentDepth++;
        }
        return new Function(coefficients);
    }

    public List<Function> analyze(List<Integer> data, int period) {
        return Util.split(data, period).stream()
                .map(this::analyze)
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
