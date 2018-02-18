package com.home.stepic.algorithm;

import java.util.Scanner;

/**
 * Created by Sergey on 11.02.2017.
 */
public class _4_EuclidGCD {
    public static void main(String[] args) {
        Scanner consoleInput = new Scanner(System.in);
        int a = consoleInput.nextInt();
        int b = consoleInput.nextInt();
        System.out.println(getGCDEuclide(a, b));
    }

    static int getGCDEuclide(int a, int b) {
        if (a == 0)
            return b;
        if (b == 0)
            return a;

        if (a > b)
            a %= b;
        else
            b %= a;
        return getGCDEuclide(a, b);
    }
}
