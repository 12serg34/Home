package com.home;

import java.util.HashMap;

public class ChessPattern {

    static int solve(String[] input) {
        int n = input.length;
        HashMap<Character, Integer> countSameWhites = new HashMap<>();
        HashMap<Character, Integer> countSameBlacks = new HashMap<>();
        int maxCountWhite = 0;
        int maxCountBlack = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (isWhite(i, j)){
                    char white = input[i].charAt(j);
                    int count = countSameWhites.getOrDefault(white, 0) + 1;
                    countSameWhites.put(white, count);
                    if (count > maxCountWhite){
                        maxCountWhite = count;
                    }
                }
                else {
                    char black = input[i].charAt(j);
                    int count = countSameBlacks.getOrDefault(black, 0) + 1;
                    countSameBlacks.put(black, count);
                    if (count > maxCountBlack){
                        maxCountBlack = count;
                    }
                }
            }
        }
        return n * n - maxCountWhite - maxCountBlack;
    }

    static boolean isWhite(int i, int j){
        return (i + j) % 2 == 0;
    }

    public static void main(String[] args){
        String[] input = new String[] { "abc", "bcd", "abc" };
//        String[] input = new String[] { "aba", "bab", "aba" };
        System.out.println(solve(input));
    }
}