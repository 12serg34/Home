package com.home.ProjectEuler;

import java.util.Scanner;

public class DifferenceSquareOfSumAndSumOfSquare {

    public static void main(String[] args) {
        new DifferenceSquareOfSumAndSumOfSquare().run();
    }

    private void run() {
        Scanner in = new Scanner(System.in);
        int t = in.nextInt();
        for (int a0 = 0; a0 < t; a0++) {
            int n = in.nextInt();
            test(n);
        }
    }

    private void test(int n) {
        System.out.println(get(n));
    }

    private long get(int n) {
        return n * (n + 1L) * (3L * n * n - n - 2L) / 12L;
    }
}