package com.home.hailstone.math;

import java.math.BigInteger;

import static java.math.BigInteger.ONE;
import static java.math.BigInteger.ZERO;

public class BigIntegerUtil {
    private static final BigInteger TWO = BigInteger.valueOf(2);
    public static final BigInteger THREE = BigInteger.valueOf(3);

    public static int cycle(BigInteger x, int... cycle) {
        return cycle[x.mod(BigInteger.valueOf(cycle.length)).intValue()];
    }

    private static BigInteger mod2(BigInteger x) {
        return x.mod(TWO);
    }

    private static BigInteger mod3(BigInteger x) {
        return x.mod(THREE);
    }

    public static BigInteger forward1(BigInteger x) {
        return TWO.multiply(x).add(ONE);
    }

    public static BigInteger reverse1(BigInteger x) {
        assert !mod2(x).equals(ZERO);
        return x.subtract(ONE).divide(TWO);
    }

    public static BigInteger forward2(BigInteger x) {
        return (x.multiply(THREE)
                .add(mod2(x))
        ).divide(TWO);
    }

    public static BigInteger reverse2(BigInteger x) {
//        assert x > -1;
//        assert mod3(x) != 1;
        return x.multiply(BigInteger.valueOf(4))
                .subtract(mod3(x))
                .divide(BigInteger.valueOf(6));
    }

    public static BigInteger powOf2(int x) {
        assert x > -1;
        return TWO.pow(x);
    }
}
