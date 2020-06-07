package com.home.ProjectEuler;

import java.util.Collections;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Scanner;

public class MaxProductInCells {

    public static void main(String[] args) {
        new MaxProductInCells().run();
    }

    private void run() {
        Scanner scanner = new Scanner(System.in);
        int n = 20, m = 20, k = 4;
        int[][] cells = new int[n + k - 1][m + k - 1];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                cells[i][j] = scanner.nextInt();
            }
        }
        scanner.close();
        test(cells, n, m, k);
    }

    private void test(int[][] cells, int n, int m, int k) {
        int maxProduct = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                int horizontal = 1;
                int vertical = 1;
                int mainDiagonal = 1;
                int slaveDiagonal = 1;
                for (int l = 0; l < k; l++) {
                    horizontal *= cells[i][j + l];
                    vertical *= cells[i + l][j];
                    mainDiagonal *= cells[i + l][j + l];
                    slaveDiagonal *= cells[i + l][j + k - 1 - l];
                }
                Queue<Integer> queue = new PriorityQueue<>(Collections.reverseOrder());
                queue.add(maxProduct);
                queue.add(horizontal);
                queue.add(vertical);
                queue.add(mainDiagonal);
                queue.add(slaveDiagonal);
                maxProduct = queue.poll();
            }
        }
        System.out.println(maxProduct);
    }
}