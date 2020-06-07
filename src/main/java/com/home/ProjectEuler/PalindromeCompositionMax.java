package com.home.ProjectEuler;

import java.util.Scanner;

public class PalindromeCompositionMax {

    public static void main(String[] args) {
        new PalindromeCompositionMax().run();
    }

    private void run() {
        Scanner scanner = new Scanner(System.in);
        int T = scanner.nextInt();
        int[] numbers = new int[T];
        for (int i = 0; i < T; i++) {
            numbers[i] = scanner.nextInt();
        }
        scanner.close();

        for (int i = 0; i < T; i++) {
            test(numbers[i]);
        }
    }

    private void test(int n) {
        int palindrome = getPalindrome(n - 1);
        while (!checkCompose(palindrome)) {
            palindrome = getPalindrome(palindrome - 1);
        }
        System.out.println(palindrome);
    }

    private int getPalindrome(int x) {
        StringBuilder number = new StringBuilder(Integer.toString(x));
        int n = number.length();
        boolean increased = false;
        for (int i = 0; i < n / 2; i++) {
            char left = number.charAt(i);
            char right = number.charAt(n - 1 - i);
            if (left != right) {
                increased = left > right;
                number.setCharAt(n - 1 - i, left);
            }
        }
        if (increased) {
            for (int i = 0; i < (n + 1) / 2; i++) {
                int indexLeft = (n + 1) / 2 - 1 - i;
                int indexRight = n / 2 + i;
                char left = number.charAt(indexLeft);
                if (left != '0') {
                    number.setCharAt(indexLeft, (char) (left - 1));
                    number.setCharAt(indexRight, (char) (left - 1));
                    break;
                } else {
                    number.setCharAt(indexLeft, '9');
                    number.setCharAt(indexRight, '9');
                }
            }
        }
        return Integer.parseInt(number.toString());
    }

    private boolean checkCompose(int x) {
        boolean compositely = false;
        int middle = (int) Math.sqrt(x);
        for (int number = middle; number > 99; number--) {
            if (x % number == 0) {
                int y = x / number;
                if (compositely = (y > 99 && y < 1000)){
                    break;
                }
            }
        }
        return compositely;
    }
}