package com.home.stepic.algorithm;

import java.util.ArrayList;
import java.util.Scanner;

public class Huffman {

    public static void main(String[] args) {
        new Huffman().run();
    }

    public void run() {
        Scanner inputConsole = new Scanner(System.in);
        String inputString = inputConsole.nextLine();

        int lengthString = inputString.length();
        ArrayList<Integer> frequencies = new ArrayList<>();
        ArrayList<Character> symbols = new ArrayList<>();
        for (int i = 0; i < lengthString; i++) {
            char c = inputString.charAt(i);
            int index = symbols.indexOf(c);
            if (index != -1) {
                frequencies.set(index, frequencies.get(index) + 1);
            } else {
                symbols.add(c);
                frequencies.add(1);
            }
        }

        ArrayList<Integer> frequenciesForDelete = new ArrayList<>(frequencies);
        int countSymbol = symbols.size();
        int[] treeHuffman = new int[2 * countSymbol + 1];

        int indexMin1 = getMin(frequenciesForDelete);
        int min1 = frequenciesForDelete.remove(indexMin1);
        int indexMin2 = getMin(frequenciesForDelete);
        int min2 = frequenciesForDelete.remove(indexMin2);

        int k = 2 * countSymbol;
        treeHuffman[k] = indexMin1;
        treeHuffman[k-1] = indexMin2;
        frequenciesForDelete.add(min1 + min2);

//        ArrayList<String> codeSymbols = new ArrayList<>();
    }

    private char getCharFromIndex(int i){
        return (char)(i + 97);
    }

    private int getIndexFromChar(char c){
        return c - 97;
    }

    private int getMin(ArrayList<Integer> arrayList){
        int result = -1;
        if (arrayList!= null && arrayList.size() > 0){
            result = 0;
            for (int i = 1; i < arrayList.size(); i++){
                if (arrayList.get(i) < arrayList.get(result)) {
                    result = i;
                }
            }
        }
        return result;
    }

    class SymbolFrequency{
        private char symbol;
        private int frequency;

        public char getSymbol() {
            return symbol;
        }
        public void setSymbol(char symbol) {
            this.symbol = symbol;
        }

        public int getFrequency() {
            return frequency;
        }
        public void setFrequency(int frequency) {
            this.frequency = frequency;
        }
    }
}
