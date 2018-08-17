package com.home.stepic.algorithm.huffman;

import java.util.LinkedList;

public class Symbol implements Comparable<Symbol> {
    public static final char LEFT_CHAR = '0';
    public static final char RIGHT_CHAR = '1';

    private char value;
    private int frequency;
    private String code;
    private Symbol left, right;

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

    public static Symbol createEmpty() {
        return new Symbol(Character.MAX_VALUE, Integer.MAX_VALUE);
    }

    public void incrementFrequency() {
        frequency++;
    }

    public void buildCode() {
        if (left != null) {
            left.code = code + LEFT_CHAR;
            left.buildCode();
        }
        if (right != null) {
            right.code = code + RIGHT_CHAR;
            right.buildCode();
        }
    }

    public void addSymbol(char value, LinkedList<Character> code) {
        if (code.isEmpty()) {
            this.value = value;
        } else {
            Symbol next = getNext(code.removeFirst());
            if (next != null){
                next.addSymbol(value, code);
            }
        }
    }

    public Character decode(LinkedList<Character> code){
        if (isList()){
            return value;
        }else {
            Symbol next = getNext(code.removeFirst());
            if (next != null){
                return next.decode(code);
            }
        }
        return Character.MAX_VALUE;
    }

    public boolean isList(){
        return left == null && right == null;
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

    private Symbol getNext(char c){
        switch (c) {
            case LEFT_CHAR:
                if (left == null) {
                    left = Symbol.createEmpty();
                }
                return left;
            case RIGHT_CHAR:
                if (right == null) {
                    right = Symbol.createEmpty();
                }
                return right;
        }
        return null;
    }
}
