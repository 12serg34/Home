package com.home.stepic.algorithm.huffman;

public class Symbol {
    private char value;
    private int frequency;
    private Symbol left, right, parent;

    public Symbol(char value, int frequency) {
        this.value = value;
        this.frequency = frequency;
    }

    public Symbol(Symbol left, Symbol right, Symbol parent) {
        this.left = left;
        this.right = right;
        left.parent = right.parent = this;
    }

    public Symbol(Symbol left, Symbol right){

    }

    public void setValue(char value) {
        this.value = value;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public void incrementFrequency(){
        frequency++;
    }

    public char getValue() {

        return value;
    }


}
