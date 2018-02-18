package com.home.ProjectEuler;

import java.util.Scanner;

public class SumOfThreesAndFives {

    public static void main(String[] args) {
        SumOfThreesAndFives solution = new SumOfThreesAndFives();
        solution.run();
    }

    private void run() {
        Scanner scanner = new Scanner(System.in);
        int T = scanner.nextInt();
        for (int test = 0; test < T; test++) {
            int n = scanner.nextInt();
            System.out.println(getTotalSum(n));
            test(n);
        }
        scanner.close();
    }

    private long getTotalSum(int n) {
        return getSum(3, n) - getSum(15, n) + getSum(5, n);
    }

    private long getSum(int value, int n) {
        int count = (n - 1) / value;
        return value * (1L + count) * count / 2;
    }

    private void test(int n){
        long sum = 0;
        for (int i = 0; i < n; i++) {
            if (i % 3 == 0 || i % 5 == 0){
                sum+= i;
            }
        }
        System.out.println("test = " + sum);
    }
}