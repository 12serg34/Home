package com.home.stepic.algorithm;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by Sergey on 13.02.2017.
 */
public class PointsOver {

    public static void main(String[] args) {
        new PointsOver().run();
    }

    int n;
    ArrayList<Integer[]> segments;
    ArrayList<Integer> points;

    private void run() {
        try (Scanner consoleInput = new Scanner(System.in)) {
            n = consoleInput.nextInt();
            segments = new ArrayList<Integer[]>();
            for (int i = 0; i < n; i++) {
                segments.add(new Integer[]{consoleInput.nextInt(), consoleInput.nextInt()});
            }
        }
        segmentsOver();
        System.out.println(points.size());
        for (int i = 0; i < points.size(); i++) {
            System.out.println(points.get(i));
        }
    }

    private void segmentsOver() {
        points = new ArrayList<>();
        while (segments.size() > 0) {

            int min = segments.get(0)[1];
            for (int j = 1; j < segments.size(); j++) {
                if (segments.get(j)[1] < min) {
                    min = segments.get(j)[1];
                }
            }

            int point = min;
            for (int j = 0; j < segments.size(); j++) {
                Integer[] seg = segments.get(j);
                int a = seg[0];
                int b = seg[1];
                if (point >= a && point <= b) {
                    segments.remove(j);
                    j--;
                }
            }
            points.add(point);
        }
    }
}
