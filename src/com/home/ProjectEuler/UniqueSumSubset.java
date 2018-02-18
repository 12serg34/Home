package com.home.ProjectEuler;

import java.util.Arrays;
import java.util.Scanner;

public class UniqueSumSubset {

    private int n;
    private int m;
    private int[] s;
    private int counter;

    public static void main(String[] args) {
        UniqueSumSubset solution = new UniqueSumSubset();
        solution.init();
        solution.run();
    }

    private void init() {
        Scanner scanner = new Scanner(System.in);
        n = scanner.nextInt();
        m = scanner.nextInt();
        s = new int[n];
        for (int i = 0; i < n; i++) {
            s[i] = scanner.nextInt();
        }
        scanner.close();
        Arrays.sort(s);
    }

    private void run() {
        int minSum = 0;
        int maxSum = 0;
        for (int i = 0; i < m; i++) {
            minSum += s[i];
            maxSum += s[n - 1 - i];
        }

        int totalSum = 0;
        for (int sum = minSum; sum <= maxSum; sum++) {
            counter = 0;
            if (searchSum(0, n - 1, sum, m) && counter == 1) {
                totalSum += sum;
            }
        }
        System.out.println(totalSum);
    }

    private boolean searchSum(int l, int r, int sum, int m) {
        boolean search = false;
        if (m == 1) {
            int index = searchValue(l, r, sum);
            if (index != -1) {
                search = true;
                counter++;
                if (checkEqualsNear(l, r, index)){
                    counter++;
                }
            }
        } else {
            int minValue = sum;
            for (int i = r - m + 2; i <= r; i++) {
                minValue -= s[i];
            }
            if (minValue > s[r - m + 1]) {
                return false;
            }
            if (minValue > s[l]) {
                l = searchMoreOrEqual(l + 1, r, minValue);
                if (m > r - l + 1) {
                    return false;
                }
            }

            int maxValue = sum;
            for (int i = l; i <= l + m - 2; i++) {
                maxValue -= s[i];
            }
            if (maxValue < s[l + m - 1]) {
                return false;
            }
            if (maxValue < s[r]) {
                r = searchLessOrEqual(l, r - 1, maxValue);
            }

            for (int i = l; i <= r - m + 1; i++) {
                if (searchSum(i + 1, r, sum - s[i], m - 1)) {
                    search = true;
                    if (counter > 1) {
                        break;
                    }
                }
            }
        }
        return search;
    }

    private int searchValue(int l, int r, int value) {
        if (l > r) {
            return -1;
        }

        if (l == r) {
            return value == s[l]? l : -1;
        }

        int middle = (l + r) / 2;
        if (s[middle] == value) {
            return middle;
        }

        if (s[middle] > value) {
            return searchValue(l, middle - 1, value);
        } else {
            return searchValue(middle + 1, r, value);
        }
    }

    private int searchMoreOrEqual(int l, int r, int value) {
        if (l == r) {
            return l;
        }

        int middle = (l + r) / 2;
        if (s[middle] == value) {
            int index = middle;
            while (index > l && s[index - 1] == s[index]){
                index--;
            }
            return index;
        }

        if (value > s[middle]){
            return searchMoreOrEqual(middle + 1, r, value);
        }
        else {
            return searchMoreOrEqual(l, middle, value);
        }
    }

    private int searchLessOrEqual(int l, int r, int value) {
        if (l == r) {
            return l;
        }

        int middle = (l + r) / 2 + 1;
        if (s[middle] == value) {
            int index = middle;
            while (index < r && s[index + 1] == s[index]){
                index++;
            }
            return index;
        }

        if (value > s[middle]){
            return searchLessOrEqual(middle, r, value);
        }
        else {
            return searchLessOrEqual(l, middle - 1, value);
        }
    }

    private boolean checkEqualsNear(int l, int r, int i){
        return (i > l && s[i - 1] == s[i])
                || (i < r && s[i + 1] == s[i]);
    }
}
