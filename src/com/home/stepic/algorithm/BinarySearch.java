package com.home.stepic.algorithm;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

class BinarySearch {
    public static void main(String[] args) {
        new BinarySearch().run();
    }

    private void run() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        Scanner inputConsole = new Scanner(reader);
        int n = inputConsole.nextInt();
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = inputConsole.nextInt();
        }
        int k = inputConsole.nextInt();
        int[] b = new int[k];
        for (int j = 0; j < k; j++) {
            b[j] = inputConsole.nextInt();
        }
        inputConsole.close();

        StringBuilder output = new StringBuilder();
        for (int j = 0; j < k; j++) {
            int search = binarySearch(a, 0, n - 1, b[j]);
            if (search != -1) {
                search++;
            }
            output.append(search).append(" ");
        }
        System.out.println(output);
    }

    private int binarySearch(int[] a, int l, int r, int x) {
        if (l > r) {
            return -1;
        }
        int m = (l + r) / 2;
        if (x == a[m]) {
            return m;
        }
        if (x > a[m]) {
            return binarySearch(a, m + 1, r, x);
        } else {
            return binarySearch(a, l, m - 1, x);
        }
    }

    private void test() {
        int n = 100000;
        int[] a = new int[n];
        Random random = new Random();
        for (int i = 0; i < n; i++) {
            a[i] = random.nextInt();
        }
        Arrays.sort(a);

        int k = 100000;
        int[] b = new int[k];
        for (int j = 0; j < k; j++) {
            b[j] = a[random.nextInt(n)];
        }

        for (int j = 0; j < k; j++) {
            int search = binarySearch(a, 0, n - 1, b[j]);
            if (search != -1) {
                search++;
            }
            System.out.printf("%1d ", search);
        }

    }
}
