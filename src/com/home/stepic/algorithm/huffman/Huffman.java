package com.home.stepic.algorithm.huffman;

import java.util.*;

public class Huffman {

    public static void main(String[] args) {
        new Huffman().run();
    }

    public void run() {
        Scanner inputConsole = new Scanner(System.in);
        String s = inputConsole.nextLine();

        char[] chars = s.toCharArray();
        Map<Character, Symbol> symbolsMap = new HashMap<>();
        for (char c : chars) {
            Symbol symbol = symbolsMap.get(c);
            if (symbol != null) {
                symbol.incrementFrequency();
            } else {
                symbolsMap.put(c, new Symbol(c, 1));
            }
        }

        Collection<Symbol> symbols = symbolsMap.values();
        Queue<Symbol> queue = new PriorityQueue<>(symbols);
        if (symbols.size() == 1){
            queue.add(new Symbol(Character.MAX_VALUE, Integer.MAX_VALUE));
        }
        while (queue.size() > 1) {
            Symbol a = queue.remove();
            Symbol b = queue.remove();
            queue.add(new Symbol(a, b));
        }

        Symbol root = queue.remove();
        root.buildCode();

        Map<Character, String> codes = new HashMap<>(symbols.size());
        for (Symbol symbol : symbols) {
            codes.put(symbol.getValue(), symbol.getCode());
        }

        StringBuilder codedString = new StringBuilder();
        for (char c : chars) {
            codedString = codedString.append(codes.get(c));
        }

        System.out.printf("%1s %2s\n", symbols.size(), codedString.length());
        for (Symbol symbol : symbols) {
            System.out.printf("%1s: %2s\n", symbol.getValue(), symbol.getCode());
        }
        System.out.println(codedString);
    }
}
