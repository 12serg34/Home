package com.home.hailstone.math;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import static java.math.BigInteger.ONE;
import static java.math.BigInteger.ZERO;

public class BigIntegerUtil {
    public static final BigInteger TWO = BigInteger.valueOf(2);
    public static final BigInteger THREE = BigInteger.valueOf(3);

    public static int cycle(BigInteger x, int... cycle) {
        return cycle[x.mod(BigInteger.valueOf(cycle.length)).intValue()];
    }

    public static int cycle(int x, BigInteger y, int[][] cycle) {
        return cycle
                [x % (cycle.length)]
                [y.mod(BigInteger.valueOf(cycle[0].length)).intValue()];

    }

    private static BigInteger mod2(BigInteger x) {
        return x.mod(TWO);
    }

    private static BigInteger mod3(BigInteger x) {
        return x.mod(THREE);
    }

    public static BigInteger F1(BigInteger x) {
        return TWO.multiply(x).add(ONE);
    }

    public static BigInteger R1(BigInteger x) {
        assert mod2(x).equals(ONE);
        return x.subtract(ONE).divide(TWO);
    }

    public static BigInteger F2(BigInteger x) {
        return (x.multiply(THREE)
                .add(mod2(x))
        ).divide(TWO);
    }

    public static BigInteger R2(BigInteger x) {
        assert !mod3(x).equals(ONE);
        return x.multiply(BigInteger.valueOf(4))
                .subtract(mod3(x))
                .divide(BigInteger.valueOf(6));
    }

    public static BigInteger R(BigInteger x) {
        return R2(R1(x));
    }

    public static BigInteger powOf2(int x) {
        assert x > -1;
        return TWO.pow(x);
    }

    public static List<BigInteger> diff(List<BigInteger> list) {
        List<BigInteger> diff = new ArrayList<>(list.size() - 1);
        for (int i = 0; i < list.size() - 1; i++) {
            diff.add(list.get(i + 1).subtract(list.get(i)));
        }
        return diff;
    }
}
