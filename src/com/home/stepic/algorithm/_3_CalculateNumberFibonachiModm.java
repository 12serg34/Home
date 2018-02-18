package com.home.stepic.algorithm;

import java.util.Scanner;

/**
 * Created by Sergey on 10.02.2017.
 */
public class _3_CalculateNumberFibonachiModm {
    public static void main(String[] args) {
        Scanner inputConsole = new Scanner(System.in);
        long n = inputConsole.nextLong();
        int m = inputConsole.nextInt();
        System.out.println(getLastNumberFibonaciModm(n, m));
    }

    static int getLastNumberFibonaciModm(long n, int m) {
        if (n <= 1) {
            return (int)n;
        } else {
            int last = 0;
            int now = 1;
            int buffer = 0;
            long period = 0;
            for (long i = 2; i <= n; i++) {
                buffer = now;
                now = (now + last) % m;
                last = buffer;
                if (last == 1 && now == 0) {
                    period = i;
                    now = getLastNumberFibonaciModm(n % period, m);
                    break;
                }
            }
            return now;
        }
    }
}
