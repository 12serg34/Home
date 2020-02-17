package com.home.hailstone.math;

import java.util.ArrayList;
import java.util.List;

public class CycleFunction implements Function {
    private final List<Function> cycle;

    public CycleFunction() {
        this.cycle = new ArrayList<>();
    }

    public void extend(Function function) {
        cycle.add(function);
    }

    public int getSize() {
        return cycle.size();
    }

    public Function getElement(int i) {
        return cycle.get(i);
    }

    @Override
    public String toString() {
        return cycle.toString();
    }
}
