package com.home.stepic.algorithm.huffman;

import java.util.*;

public class HuffmanCoding {

    public static void main(String[] args) {
        new HuffmanCoding().run();
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
            queue.add(Symbol.createEmpty());
        }
        while (queue.size() > 1) {
            Symbol a = queue.remove();
            Symbol b = queue.remove();
            queue.add(new Symbol(a, b));
        }
        queue.remove().buildCode();

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
