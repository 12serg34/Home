package com.home.ProjectEuler;

import java.util.Scanner;

public class LeastMultiple {

    public static void main(String[] args) {
        new LeastMultiple().run();
    }

    private void run() {
        Scanner scanner = new Scanner(System.in);
        int T = scanner.nextInt();
        int[] numbers = new int[T];
        for (int i = 0; i < T; i++) {
            numbers[i] = scanner.nextInt();
        }
        scanner.close();

        for (int i = 0; i < T; i++) {
            test(numbers[i]);
        }
    }

    private void test(int n) {
        System.out.println(getLeastMultiple(n));
    }

    private int getLeastMultiple(int n) {
        int y = 1;
        for (int x = n; x > 1; x--){
            int lm = getLeastMultiple(x, y);
            y*= x / lm;
        }
        return y;
   }

    private int getLeastMultiple(int a, int b){
        if (a == 0){
            return b;
        }
        if (b == 0){
            return a;
        }
        return getLeastMultiple(a % b, b % a);
    }
}