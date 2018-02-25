package com.home;

import java.util.List;

import static java.util.Arrays.asList;

public class Main {

    public static void main(String[] args) {
        List<Integer> a = asList(1, 2, 3);
        a.set(0, 1);
    }
}