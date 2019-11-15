package com.home.hailstone.math;

public class Util {

    public static int gamma(int x) {
        return x % 3 == 2 ? 1 : 0;
    }

    public static int cycle(int x, int... cycle) {
        return cycle[x % cycle.length];
    }

    public static int mod2(int x) {
        return x % 2;
    }

    public static int invertMod2(int x) {
        return 1 - mod2(x);
    }

    public static int mod3(int x) {
        return x % 3;
    }

    public static int forward1(int x) {
        return 2 * x + 1;
    }

    public static int reverse1(int x) {
        assert mod2(x) != 0;
        return (x - 1) / 2;
    }

    public static int forward2(int x) {
        return (3 * x + x % 2) / 2;
    }

    public static int reverse2(int x) {
        assert x > -1;
        assert mod3(x) != 1;
        return (4 * x - mod3(x)) / 6;
    }

    public static int powOf2(int x) {
        assert x > -1;
        return 1 << x;
    }
}
