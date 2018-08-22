package com.home.stepic.algorithm.pointsAndIntervals;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;

class PointAndInterval {

    int n, m;
    int[] points;
    int[][] intervals;
    private Random random;

    {
        random = new Random();
    }

    public static void main(String[] args) throws IOException {
        PointAndInterval program = new PointAndInterval();
        program.readInput();
        program.run();
    }

    private void readInput() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String[] s = reader.readLine().split(" ");
        n = Integer.parseInt(s[0]);
        m = Integer.parseInt(s[1]);

        intervals = new int[2][n];
        intervals[0] = new int[n];
        intervals[1] = new int[n];
        for (int i = 0; i < n; i++) {
            s = reader.readLine().split(" ");
            intervals[0][i] = Integer.parseInt(s[0]);
            intervals[1][i] = Integer.parseInt(s[1]);
        }

        points = new int[m];
        s = reader.readLine().split(" ");
        for (int i = 0; i < m; i++) {
            points[i] = Integer.parseInt(s[i]);
        }
        reader.close();
    }

    public void run() {
        sort();
        count();
    }

    private void sort(){
        quickSort(intervals[0], 0, n - 1);
        quickSort(intervals[1], 0, n - 1);
    }

    private void count(){
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < m; i++) {
            int p = points[i];
            builder.append(findLeft(p) - findRight(p)).append(" ");
        }
        System.out.println(builder);
    }

    private int findLeft(int x) {
        int l = -1;
        int r = n;
        while (r - l > 1) {
            int m = (l + r) >> 1;
            if (x >= intervals[0][m]) {
                l = m;
            } else {
                r = m;
            }
        }
        return l;
    }

    private int findRight(int x) {
        int l = -1;
        int r = n;
        while (r - l > 1) {
            int m = (l + r) >> 1;
            if (x <= intervals[1][m]) {
                r = m;
            } else {
                l = m;
            }
        }
        return l;
    }

    private void quickSort(int[] a, int l, int r) {
        if (l < r) {
            int[] x = partition(a, l, r);
            quickSort(a, l, x[0] - 1);
            quickSort(a, x[1] + 1, r);
        }
    }

    private int[] partition(int[] a, int l, int r) {
        swap(a, l, l + random.nextInt(r - l));
        int x = a[l];
        int el = l, er = l;
        for (int i = l + 1; i <= r; i++) {
            if (a[i] == x) {
                swap(a, i, ++er);
            }
            if (a[i] < x) {
                swap(a, i, ++er);
                swap(a, er, el++);
            }
        }
        return new int[]{el, er};
    }

    private void swap(int[] a, int i, int j) {
        int buffer = a[i];
        a[i] = a[j];
        a[j] = buffer;
    }
}
