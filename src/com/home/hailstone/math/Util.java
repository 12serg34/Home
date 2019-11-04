package com.home.hailstone.math;

public class Util {
    public static int betta(int x) {
        return x % 3 == 1 ? 1 : 0;
    }

    public static int gamma(int x) {
        return x % 3 == 2 ? 1 : 0;
    }

    public static int cycle(int x, int... cycle) {
        return cycle[x % cycle.length];
    }

    public static int invertMod2(int x) {
        return 1 - x;
    }

    public static int invertMod3(int x) {
        assert x > -1;
        assert x < 3;
        return 2 - x;
    }

    public static int forward1(int x) {
        return 2 * x + 1;
    }

    public static int reverse1(int x) {
        assert x % 2 != 0;
        return (x - 1) / 2;
    }

    public static int forward2(int x) {
        return (3 * x + x % 2) / 2;
    }

    public static int reverse2(int x) {
        assert x > -1;
        assert x % 3 != 1;
        return (4 * x - x % 3) / 6;
    }

    public static int powOf2(int i) {
        assert i > -1;
        return 1 << i;
    }
}
