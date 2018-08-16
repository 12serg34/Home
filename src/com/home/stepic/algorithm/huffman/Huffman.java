package com.home.stepic.algorithm.huffman;

import java.util.*;

public class Huffman {

    public static void main(String[] args) {
        new Huffman().run();
    }

    public void run() {
        Scanner inputConsole = new Scanner(System.in);
        String s = inputConsole.nextLine();
        Map<Character, Symbol> symbols = new HashMap<>();
        int length = s.length();
        for (int i = 0; i < length; i++) {
            char c = s.charAt(i);
            Symbol symbol = symbols.get(c);
            if (symbol != null) {
                symbol.incrementFrequency();
            } else {
                symbols.put(c, new Symbol(c, 1));
            }
        }

        Queue<Symbol> queue = new PriorityQueue<>(symbols.values());
        while (!queue.isEmpty()){
            Symbol a = queue.remove();
            Symbol b = queue.remove();
            
        }
    }
}
