package com.home.stepic.algorithm;

import java.util.Scanner;

class LongestIncreaseMultipleSubsequence{
    public static void main(String[] args){
        new LongestIncreaseMultipleSubsequence().run();
    }

    private void run() {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = scanner.nextInt();
        }
        scanner.close();

        int max = 1;
        int[] d = new int[n];
        for (int i = 0; i < n; i++) {
            d[i] = 1;
            for (int j = 0; j < i; j++) {
                if (a[i] % a[j] == 0 && d[j] + 1 > d[i]){
                    d[i] = d[j] + 1;
                }
            }
            if (d[i] > max) {
                max = d[i];
            }
        }

        System.out.println(max);
    }
}
