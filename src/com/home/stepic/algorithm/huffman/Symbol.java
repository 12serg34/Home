package com.home.stepic.algorithm.huffman;

public class Symbol implements Comparable<Symbol>{
    public static final String LEFT_CHAR = "0";
    public static final String RIGHT_CHAR = "1";

    private char value;
    private int frequency;
    private Symbol left, right;
    private String code;

    public Symbol(char value, int frequency) {
        this.value = value;
        this.frequency = frequency;
    }

    public Symbol(Symbol left, Symbol right) {
        this.left = left;
        this.right = right;
        this.frequency = left.frequency + right.frequency;
        this.code = "";
    }

    public void incrementFrequency(){
        frequency++;
    }

    public void buildCode(){
        if (left != null){
            left.code = code + LEFT_CHAR;
            left.buildCode();
        }
        if (right != null){
            right.code = code + RIGHT_CHAR;
            right.buildCode();
        }
    }

    public char getValue() {
        return value;
    }

    public String getCode() {
        return code;
    }

    @Override
    public int compareTo(Symbol o) {
        return Integer.compare(this.frequency, o.frequency);
    }
}
