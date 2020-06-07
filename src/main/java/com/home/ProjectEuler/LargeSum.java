package com.home.ProjectEuler;

import java.math.BigInteger;
import java.util.Scanner;

public class LargeSum {

    private BigInteger[] numbers;

    public static void main(String[] args) {
        new LargeSum().run();
    }

    private void run() {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        numbers = new BigInteger[n];
        for (int i = 0; i < n; i++) {
            numbers[i] = scanner.nextBigInteger();
        }
        sumUp();
    }

    private void sumUp() {
        BigInteger sum = BigInteger.ZERO;
        for (BigInteger number : numbers) {
            sum = sum.add(number);
        }
        System.out.println(sum.toString().substring(0, 10));
    }
}