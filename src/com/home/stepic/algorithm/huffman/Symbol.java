package com.home.stepic.algorithm.huffman;

public class Symbol implements Comparable<Symbol>{
    public static final char LEFT_CHAR = '0';
    public static final char RIGHT_CHAR = '1';

    private char value;
    private int frequency;
    private Symbol left, right, parent;
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

    public Symbol(Symbol parent) {
        this.parent = parent;
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

    public void AddSymbol(char value, String code){
        if (!code.isEmpty()){
            char c = code.charAt(0);
            switch (c){
                case LEFT_CHAR:
                    if (left == null){
                        left = new Symbol(this);
                    }
                    else {
                        left.AddSymbol(value, code.substring(1, code.length() - 1));
                    }
                    break;
                case RIGHT_CHAR:
                    if (right == null){
                        right = new Symbol(this);
                    }
                    else {
                        right.AddSymbol(value, code.substring(1, code.length() - 1));
                    }
                    break;
            }
        }
        else {
            this.value = value;
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
