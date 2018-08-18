package com.home.stepic.algorithm.priorityQueue;

import java.util.Arrays;

public class PriorityQueue {
    public static final int FIRST_LIMIT = 16;

    private long[] values;
    private int size, limitSize;

    public PriorityQueue(){
        limitSize = FIRST_LIMIT;
        values = new long[limitSize];
    }

    public void insert(long l){
        checkSize();
        values[size] = l;
        siftUp(size++);
    }

    public long extractMax(){
        long max = Long.MIN_VALUE;
        if (size > 0){
            max = values[0];
            values[0] = values[size - 1];
            size--;
            siftDown(0);
        }
        return max;
    }

    private void checkSize(){
        if (size == limitSize){
            limitSize <<= 1;
            values = Arrays.copyOf(values, limitSize);
        }
    }

    private void siftUp(int i){
        int parent = getParent(i);
        if (values[i] > values[parent]){
            swap(parent, i);
            siftUp(parent);
        }
    }

    private void siftDown(int i) {
        int left = getLeft(i);
        int right = getRight(i);
        if (left != -1 && values[i] < values[left]){
            if (right == -1 || values[left] > values[right]){
                swap(i, left);
                siftDown(left);
            }
            else {
                swap(i, right);
                siftDown(right);
            }
        }else {
            if (right != -1 && values[i] < values[right]){
                swap(i, right);
                siftDown(right);
            }
        }
    }

    private int getParent(int i){
        int parent = (i + 1) / 2 - 1;
        if (parent == -1){
            parent = 0;
        }
        return parent;
    }

    private int getLeft(int i){
        int left = ((i + 1) << 1) - 1;
        if (left >= size){
            left = -1;
        }
        return left;
    }

    private int getRight(int i){
        int right = (i + 1) << 1;
        if (right >= size){
            right = -1;
        }
        return right;
    }

    private void swap(int i, int j){
        long buffer = values[i];
        values[i] = values[j];
        values[j] = buffer;
    }
}
