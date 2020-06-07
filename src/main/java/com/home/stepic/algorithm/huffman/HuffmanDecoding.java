package com.home.stepic.algorithm.huffman;

import java.util.LinkedList;
import java.util.Scanner;

public class HuffmanDecoding {

    public static void main(String[] args) {
        new HuffmanDecoding().run();
    }

    public void run() {
        Scanner inputConsole = new Scanner(System.in);
        int amountSymbols = inputConsole.nextInt();
        int length = inputConsole.nextInt();
        inputConsole.nextLine();

        Symbol root = Symbol.createEmpty();
        for (int i = 0; i < amountSymbols; i++) {
            String[] parts = inputConsole.nextLine().split(":");
            root.addSymbol(parts[0].charAt(0), stringToListCharacter(parts[1].trim()));
        }

        LinkedList<Character> code = stringToListCharacter(inputConsole.nextLine());
        StringBuilder decodedString = new StringBuilder();
        while (!code.isEmpty()){
            decodedString = decodedString.append(root.decode(code));
        }
        System.out.println(decodedString);
    }

    private LinkedList<Character> stringToListCharacter(String s){
        char[] chars = s.toCharArray();
        LinkedList<Character> list = new LinkedList<>();
        for(char c: chars){
            list.add(c);
        }
        return list;
    }
}
