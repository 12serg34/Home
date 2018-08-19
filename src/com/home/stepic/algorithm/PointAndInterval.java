package com.home.stepic.algorithm;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Scanner;

class PointAndInterval {

    int n, m;
    int[] points;
    int[] references;

    public static void main(String[] args) {
        new PointAndInterval().run();
    }

    private void run() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        Scanner scanner = new Scanner(reader);
        n = scanner.nextInt();
        m = scanner.nextInt();
        Interval[] intervals = new Interval[n];
        for (int i = 0; i < n; i++) {
            intervals[i] = new Interval(scanner.nextInt(), scanner.nextInt());
        }
        points = new int[m];
        references = new int[m];
        for (int i = 0; i < m; i++) {
            points[i] = scanner.nextInt();
            references[i] = i;
        }
        scanner.close();

        quickSortPoints(0, m - 1);
        int[] countsOfOvers = new int[m];
        for (int i = 0; i < n; i++) {
            Interval interval = intervals[i];
            int left = getLeftOverPoint(interval);
            int right = getRightOverPoint(interval);
            for (int j = left; j <= right; j++) {
                countsOfOvers[references[j]]++;
            }
        }

        for (int i = 0; i < m; i++) {
            System.out.print(countsOfOvers[i] + " ");
        }
    }

    private int getLeftOverPoint(Interval interval) {
        int l = -1;
        int r = m;
        while (r - l > 1) {
            int m = (l + r) >> 1;
            if (points[references[m]] >= interval.left) {
                r = m;
            } else {
                l = m;
            }
        }
        return r;
    }

    private int getRightOverPoint(Interval interval) {
        int l = -1;
        int r = m;
        while (r - l > 1) {
            int m = (l + r) >> 1;
            if (points[references[m]] <= interval.right) {
                l = m;
            } else {
                r = m;
            }
        }
        return l;
    }

    private void quickSortPoints(int l, int r) {
        if (l <= r) {
            int m = partition(l, r);
            quickSortPoints(l, m - 1);
            quickSortPoints(m + 1, r);
        }
    }

    private int partition(int l, int r) {
        int m = points[references[l]];
        int j = l;
        for (int i = l + 1; i <= r; i++) {
            if (points[references[i]] <= m) {
                swap(i, ++j);
            }
        }
        swap(l, j);
        return j;
    }

    private void swap(int i, int j) {
        int buffer = references[i];
        references[i] = references[j];
        references[j] = buffer;
    }
}

class Interval {
    int left, right;

    public Interval(int left, int right) {
        this.left = left;
        this.right = right;
    }
}
