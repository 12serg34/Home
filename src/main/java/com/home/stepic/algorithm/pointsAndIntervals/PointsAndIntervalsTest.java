package com.home.stepic.algorithm.pointsAndIntervals;

import java.util.Random;

public class PointsAndIntervalsTest {
    public static void main(String[] args) {
        new PointsAndIntervalsTest().test();
    }

    private static PointAndInterval generateInput() {
        PointAndInterval program = new PointAndInterval();
        Random random = new Random();
        int n = 1 + random.nextInt(50000);
        System.out.println("n = " + n);
        int m = 1 + random.nextInt(50000);
        System.out.println("m = " + m);
        final int min = -100000000;
        final int max = 100000000;

        int[][] intervals = new int[2][n];
        intervals[0] = new int[n];
        intervals[1] = new int[n];
        for (int i = 0; i < n; i++) {
            intervals[0][i] = min + random.nextInt(max - min);
            intervals[1][i] = intervals[0][i] + random.nextInt(max - intervals[0][i]);
            System.out.printf("%1d %2d\n", intervals[0][i], intervals[1][i]);
        }

        int[] points = new int[m];
        for (int i = 0; i < m; i++) {
            points[i] = min + random.nextInt(max - min);
        }

        program.n = n;
        program.m = m;
        program.intervals = intervals;
        program.points = points;
        return program;
    }

    private void test() {
        PointAndInterval program = generateInput();
        program.run();
    }
}
