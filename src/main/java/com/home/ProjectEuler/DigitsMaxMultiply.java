package com.home.ProjectEuler;

import java.util.Scanner;

public class DigitsMaxMultiply {

    public static void main(String[] args) {
        new DigitsMaxMultiply().run();
    }

    private void run() {
        Scanner scanner = new Scanner(System.in);
        int T = scanner.nextInt();
        for (int i = 0; i < T; i++) {
            int n = scanner.nextInt();
            int k = scanner.nextInt();
            scanner.nextLine();
            String number = scanner.nextLine();
            test(n, k, number);
        }
        scanner.close();
    }

    private void test(int n, int k, String number) {
        int multiple = 0;
        int maxMultiple = 0;
        int[] digits = readDigits(number);
        for (int i = 0; i <= n - k; i++) {
            if (multiple == 0) {
                multiple = 1;
                for (int j = 0; j < k; j++) {
                    multiple *= digits[i + j];
                    if (multiple == 0) {
                        i += j;
                        break;
                    }
                }
            } else {
                multiple = multiple / digits[i - 1] * digits[i + k - 1];
                if (multiple == 0) {
                    i += k - 1;
                }
            }

            if (multiple != 0 && multiple > maxMultiple) {
                maxMultiple = multiple;
            }
        }
        System.out.println(maxMultiple);
    }

    private int[] readDigits(String number) {
        int codeOfZero = 48;
        char[] symbols = number.toCharArray();
        int[] digits = new int[symbols.length];
        for (int i = 0; i < symbols.length; i++) {
            digits[i] = symbols[i] - codeOfZero;
        }
        return digits;
    }
}