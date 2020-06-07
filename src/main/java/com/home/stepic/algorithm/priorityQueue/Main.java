package com.home.stepic.algorithm.priorityQueue;

import java.util.Scanner;

public class Main {
    public static void main(String[] args){
        String insertCommand = "Insert";
        String extractCommand = "ExtractMax";

        PriorityQueue queue = new PriorityQueue();
        StringBuilder output = new StringBuilder();
        Scanner inputConsole = new Scanner(System.in);
        int amountOfCommands = inputConsole.nextInt();
        for (int i = 0; i < amountOfCommands; i++) {
            String command = inputConsole.next();
            if (command.equals(insertCommand)){
                queue.insert(inputConsole.nextInt());
            }
            if (command.equals(extractCommand)){
                output = output.append(queue.extractMax()).append("\n");
            }
        }
        inputConsole.close();
        System.out.print(output);
    }
}
