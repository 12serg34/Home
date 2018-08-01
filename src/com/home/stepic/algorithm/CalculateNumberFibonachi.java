package com.home.stepic.algorithm;

import java.util.Scanner;

/**
 * Created by Sergey on 10.02.2017.
 */
public class CalculateNumberFibonachi {
    public static void main(String[] args){
        Scanner inputConsole = new Scanner(System.in);
        int n = inputConsole.nextInt();
        System.out.println(getNumberFibonaci(n));
    }

    static int getNumberFibonaci(int n) {
        if (n <= 1) {
            return n;
        } else {
            int last = 0;
            int now = 1;
            int bufer = 0;
            for (int i = 1; i < n; i++) {
                bufer = now;
                now += last;
                last = bufer;
            }
            return now;
        }
    }
}
