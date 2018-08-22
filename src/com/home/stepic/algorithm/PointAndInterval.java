package com.home.stepic.algorithm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;

class PointAndInterval {

    Random random = new Random();
    int n, m;
    int[] points;
    Interval[] leftIntervals, rightIntervals;

    public static void main(String[] args) throws IOException {
        new PointAndInterval().run();
    }

    private void run() throws IOException {
        long t1 = System.nanoTime();
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String[] s = reader.readLine().split(" ");
        n = Integer.parseInt(s[0]);
        m = Integer.parseInt(s[1]);
        Interval[] intervals = new Interval[n];
        for (int i = 0; i < n; i++) {
            s = reader.readLine().split(" ");
            intervals[i] = new Interval(Integer.parseInt(s[0]), Integer.parseInt(s[1]));
        }
        points = new int[m];
        s = reader.readLine().split(" ");
        for (int i = 0; i < m; i++) {
            points[i] = Integer.parseInt(s[i]);
        }
        reader.close();

        leftIntervals = Arrays.copyOf(intervals, n);
        rightIntervals = Arrays.copyOf(intervals, n);
        quickSort(leftIntervals, intervals[0].new LeftComparator(), 0, n - 1);
        quickSort(rightIntervals, intervals[0].new RightComparator(), 0, n - 1);

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < m; i++) {
            int x = points[i];
            builder.append(getProbablyIntervals(x) - getImpossibleIntervals(x)).append(" ");
        }
        System.out.println(builder);
    }

    private int getProbablyIntervals(int x) {
        int l = -1;
        int r = n;
        while (r - l > 1) {
            int m = (l + r) >> 1;
            if (x >= leftIntervals[m].left) {
                l = m;
            } else {
                r = m;
            }
        }
        return l;
    }

    private int getImpossibleIntervals(int x) {
        int l = -1;
        int r = n;
        while (r - l > 1) {
            int m = (l + r) >> 1;
            if (x <= rightIntervals[m].right) {
                r = m;
            } else {
                l = m;
            }
        }
        return l;
    }

    private void quickSort(Interval[] a, Comparator<Interval> c, int l, int r) {
        if (l <= r) {
            Interval m = partition(a, c, l, r);
            quickSort(a, c, l, m.left - 1);
            quickSort(a, c, m.right + 1, r);
        }
    }

    private Interval partition(Interval[] a, Comparator<Interval> c, int l, int r) {
        swap(a, l, l + random.nextInt(r - l + 1));
        Interval e = new Interval(l, l);
        for (int i = l + 1; i <= r; i++) {
            int compare = c.compare(a[i], a[l]);
            if (compare == 0) {
                swap(a, i, ++e.right);
            }
            if (compare < 0) {
                swap(a, ++e.right, e.left);
                swap(a, ++e.left, i);
            }
        }
        return e;
    }

    private void swap(Interval[] a, int i, int j) {
        Interval buffer = a[i];
        a[i] = a[j];
        a[j] = buffer;
    }
}

class Interval {
    int left, right;

    public Interval(int left, int right) {
        this.left = left;
        this.right = right;
    }

    public class LeftComparator implements Comparator<Interval> {
        @Override
        public int compare(Interval o1, Interval o2) {
            return Integer.compare(o1.left, o2.left);
        }
    }

    public class RightComparator implements Comparator<Interval> {
        @Override
        public int compare(Interval o1, Interval o2) {
            return Integer.compare(o1.right, o2.right);
        }
    }

    @Override
    public String toString() {
        return left + " " + right;
    }
}
