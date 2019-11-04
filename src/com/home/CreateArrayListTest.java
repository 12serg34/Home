package com.home;

import java.util.ArrayList;
import java.util.Random;

public class CreateArrayListTest {
    public static void main(String[] args) {
        long initTime = System.nanoTime();
        Random random = new Random();
        int n = 80_000;
        System.out.println("Size of collections: " + n);    

        ArrayList<Integer> array = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            array.add(random.nextInt());
        }
        System.out.println("Initialize time: " + getDuration(initTime));

        long startTimeAdd = System.nanoTime();
        ArrayList<Integer> arrayListAdd = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            arrayListAdd.add(array.get(i));
        }
        System.out.println("Time of add: : " + getDuration(startTimeAdd));

        long startTimeAddWOGrow = System.nanoTime();
        ArrayList<Integer> arrayListAddWOGrow = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            arrayListAdd.add(array.get(i));
        }
        System.out.println("Time of add without grow: : " + getDuration(startTimeAddWOGrow));

        long startTimeCopy = System.nanoTime();
        ArrayList<Integer> arrayListCopy = new ArrayList<>(array);
        System.out.println("Time of copy: " + getDuration(startTimeCopy));
    }

    private static long getDuration(long startTime) {
        return (System.nanoTime() - startTime) / 1000;
    }
}
