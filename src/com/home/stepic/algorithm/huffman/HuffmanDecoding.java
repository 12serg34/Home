package com.home.stepic.algorithm.huffman;

import java.util.Scanner;

public class HuffmanDecoding {

    public static void main(String[] args) {
        new HuffmanDecoding().run();
    }

    public void run() {
        Scanner inputConsole = new Scanner(System.in);
        int amountSymbols = inputConsole.nextInt();
        int length = inputConsole.nextInt();
        Symbol root = new Symbol(Character.MAX_VALUE, Integer.MAX_VALUE);
        for (int i = 0; i < amountSymbols; i++) {
            String[] parts = inputConsole.nextLine().split(":");
            root.AddSymbol(parts[0].charAt(0), parts[1].trim());
        }
        String code = inputConsole.nextLine();
        System.out.println(code);
    }
}
