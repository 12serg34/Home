package com.home.hailstone;

import java.math.BigInteger;

public class Grid {

    private static final char SQUARE = '□';

    public static void main(String[] args) {
        System.out.println("Hello, there is a grid of floor(2^n / 3^k)");

        BigInteger two = BigInteger.valueOf(2);
        BigInteger three = BigInteger.valueOf(3);

        int k = 5;
        int n = 55;
        BigInteger[][] grid = new BigInteger[k][n];
        grid[0][0] = BigInteger.ONE;
        for (int j = 1; j < n; j++) {
            grid[0][j] = grid[0][j - 1].multiply(two);
        }
        for (int i = 1; i < k; i++) {
            for (int j = 0; j < n; j++) {
                grid[i][j] = grid[i - 1][j].divide(three);
            }
        }

        for (int i = 0; i < k; i++) {
            for (int j = 0; j < n; j++) {
                System.out.print(grid[i][j].mod(three));
            }
            System.out.println();
        }
    }
}

