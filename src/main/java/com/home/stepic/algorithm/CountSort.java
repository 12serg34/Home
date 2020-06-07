package com.home.stepic.algorithm;

import java.util.Scanner;

class CountSort {

    private static final int M = 10;

    public static void main(String[] args){
        new CountSort().run();
    }

    private void run() {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = scanner.nextInt() - 1;
        }
        scanner.close();

        a = countSort(a);
        for(int e: a){
            System.out.print((e + 1) + " ");
        }
    }

    private int[] countSort(int[] a){
        int[] b = new int[M];
        for (int i = 0; i < a.length; i++) {
            b[a[i]]++;
        }
        for (int j = 1; j < b.length; j++) {
            b[j] += b[j - 1];
        }
        int[] c = new int[a.length];
        for (int i = a.length - 1; i >= 0; i--) {
            int e = a[i];
            c[b[e] - 1] = e;
            b[e]--;
        }
        return c;
    }
}
