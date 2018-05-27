package com.home.ProjectEuler;

import java.util.Scanner;

public class SpeciallyPifagorTriple {

    public static void main(String[] args) {
        new SpeciallyPifagorTriple().run();
    }

    private void run() {
        Scanner scanner = new Scanner(System.in);
        int T = scanner.nextInt();
        for (int i = 0; i < T; i++) {
            int n = scanner.nextInt();
            test(n);
        }
        scanner.close();
    }

    private void test(int n){
        int maxProduct = -1;
        int aLimit = n / 3 + 1;
        for (int a = 1; a < aLimit; a++){
            int bLimit = (n - a) / 2 + 1;
            for (int b = a + 1; b < bLimit; b++){
                int c = n - a - b;
                if (isPifagorTriple(a, b, c)){
                    int product = a * b * c;
                    if (product > maxProduct){
                        maxProduct = product;
                    }
                }
            }
        }
        System.out.println(maxProduct);
    }

    private boolean isPifagorTriple(int a, int b, int c){
        return (a * a + b*b - c*c) == 0;
    }
}