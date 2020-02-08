package com.home.hailstone.math;

public interface Function {
    @SuppressWarnings("unchecked")
    default <T> T getAs() {
        return (T) this;
    }
}
