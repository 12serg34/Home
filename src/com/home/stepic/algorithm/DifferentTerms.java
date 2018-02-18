package com.home.stepic.algorithm;

import java.util.Scanner;

public class DifferentTerms {

    public static void main(String[] args) {
        new DifferentTerms().run();
    }

    public void run(){
        Scanner inputConsole = new Scanner(System.in);
        int n = inputConsole.nextInt();

        int s = n;
        int k = 1;
        while (s > 2 * k){
            s-= k++;
        }

        System.out.println(k);
        for (int i = 1; i < k; i++) {
            System.out.print(i + " ");
        }
        System.out.println(s);
    }
}


