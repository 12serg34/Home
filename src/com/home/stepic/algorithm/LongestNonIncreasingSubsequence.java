package com.home.stepic.algorithm;

import java.util.LinkedList;
import java.util.Scanner;

public class LongestNonIncreasingSubsequence {
    public static void main(String[] args){
        new LongestNonIncreasingSubsequence().run();
    }

    private void run() {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = scanner.nextInt();
        }
        scanner.close();

        int iMax = 0;
        int[] d = new int[n];
        for (int i = 0; i < n; i++) {
            d[i] = 1;
            for (int j = 0; j < i; j++) {
                if (a[i] <= a[j] && d[j] + 1 > d[i]){
                    d[i] = d[j] + 1;
                }
            }
            if (d[i] > d[iMax]){
                iMax = i;
            }
        }
        int i = iMax;
        LinkedList<Integer> path = new LinkedList<>();
        path.add(iMax);
        while (d[i] > 1){
            for (int j = i - 1; j >= 0; j--) {
                if (d[j] + 1 == d[i]){
                    path.addFirst(i = j);
                }
            }
        }

        StringBuilder builder = new StringBuilder();
        builder.append(d[iMax]).append("\n");
        for(int x: path){
            builder.append(x + 1).append(" ");
        }
        System.out.println(builder);
    }
}
