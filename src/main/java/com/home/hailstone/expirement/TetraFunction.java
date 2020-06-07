package com.home.hailstone.expirement;

public interface TetraFunction<A, B, C, R> {
    R apply(A a, B b, C c);
}
