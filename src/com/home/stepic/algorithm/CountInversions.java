package com.home.stepic.algorithm;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.Scanner;

class CountInversions {

    private long inversions;

    public static void main(String[] args) {
        new CountInversions().run();
    }

    private void run() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        Scanner input = new Scanner(reader);
        int n = input.nextInt();
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = input.nextInt();
        }
        input.close();

        countInversions(a);
        System.out.println(inversions);
    }

    private void countInversions(int[] a) {
        int size = a.length;
        int fullSize = getFullPowerTwo(size);
        LinkedList<int[]> queue = new LinkedList<>();
        for (int x : a) {
            queue.addLast(new int[]{x});
        }
        for (int i = size; i < fullSize; i++) {
            queue.addLast(new int[]{Integer.MAX_VALUE});
        }
        while (queue.size() > 1){
            queue.addLast(merge(queue.removeFirst(), queue.removeFirst()));
        }
    }

    private int[] merge(int[] a, int[] b) {
        int[] m = new int[a.length + b.length];
        int i = 0, j = 0;
        for (int k = 0; k < m.length; k++) {
            if (i >= a.length){
                for (; j < b.length; k++, j++) {
                    m[k] = b[j];
                }
                break;
            }
            if (j >= b.length){
                for (; i < a.length; k++, i++) {
                    m[k] = a[i];
                }
                break;
            }
            if (a[i] <= b[j]){
                m[k] = a[i++];
            }else {
                m[k] = b[j++];
                inversions+= a.length - i;
            }
        }
        return m;
    }

    private int getFullPowerTwo(int i){
        int j = 2;
        while (i > 1){
            i >>= 1;
            j <<= 1;
        }
        return j;
    }
}
