package com.home.hailstone;

import java.util.Random;

import static java.lang.Math.sqrt;

public class SolvePalindromeTest {
    public static void main(String[] args) {
        new SolvePalindromeTest().run();
    }

    private void run() {
        Random random = new Random();
//        int k0 = 1 + random.nextInt(1000);
//        int k1 = random.nextInt(1000);
//        int real_x = random.nextInt(1000);
        int k0 = 60;
        int k1 = 53;
        int real_x = 31;
        int k2 = 36 * real_x * real_x + k0 * real_x + k1;
        int k2_sub_k1 = k2 - k1;
        double real_k11 = sqrt(k2_sub_k1) / 6.0;
        double real_k6 = 1.0 / 6 * sqrt(k2_sub_k1 - k0 * real_k11);
        int k6 = (int) Math.ceil(real_k6);
        int k6_mod_k0 = k6 % k0;

        int k2_sub_k1_mod_k0 = k2_sub_k1 % k0;
        int k5 = k6_mod_k0;
        for (; k5 < k0; k5++) {
            if ((36 * k5 * k5) % k0 == k2_sub_k1_mod_k0
                    && k5 >= k6_mod_k0) {
                break;
            }
        }
        if (k5 == k0) {
            for (k5 = 0; k5 < k6_mod_k0; k5++) {
                if ((36 * k5 * k5) % k0 == k2_sub_k1_mod_k0) {
                    break;
                }
            }
            if (k5 == k6_mod_k0) {
                throw new RuntimeException("You are wrong!");
            }
        }

        int x = k6 + (k5 - k6_mod_k0 + k0) % k0;
        System.out.println("k0 = " + k0);
        System.out.println("k1 = " + k1);
        System.out.println("k2 = " + k2);
        System.out.println("k5 = " + k5);
        System.out.println("real_k6 = " + real_k6);
        System.out.println("k6 = " + k6);
        System.out.println("x = " + x);
        System.out.println("real x = " + real_x);
        System.out.println("real_k11 = " + real_k11);
    }
}
