package com.home.ProjectEuler;

import java.util.Scanner;

public class HighlyDivisibleTriangularNumber {

    public static void main(String[] args) {
        new HighlyDivisibleTriangularNumber().run();
    }

    private void run() {
        Scanner scanner = new Scanner(System.in);
        int T = scanner.nextInt();
        for (int t = 0; t < T; t++) {
                test(scanner.nextInt());
        }
        scanner.close();
    }

    private void test(int n){
        int i = 1;
        int number = 1;
        while (getAmountOf(number) + 1 <= n){
            number+= ++i;
        }
        System.out.println(number);
    }

    private int getAmountOf(int n){
        if (n == 1){
            return 1;
        }
        int factor = n;
        int limit = (int)(Math.sqrt(n)) + 1;
        for (int i = 2; i < limit; i++){
            if (n % i == 0){
                factor = i;
                break;
            }
        }
        factor = n / factor;
        return getAmountOf(factor) + 1;
    }
}