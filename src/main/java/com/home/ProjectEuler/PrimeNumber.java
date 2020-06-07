package com.home.ProjectEuler;

import java.util.HashMap;
import java.util.Scanner;

public class PrimeNumber {

    private int maxNumber;
    private HashMap<Integer, Long> cache;

    public static void main(String[] args) {
        new PrimeNumber().run();
    }

    private void run() {
        Scanner scanner = new Scanner(System.in);
        int T = scanner.nextInt();
        int[] numbers = new int[T];
        for (int i = 0; i < T; i++) {
            numbers[i] = scanner.nextInt();
            maxNumber = numbers[i] > maxNumber? numbers[i] : maxNumber;
        }
        scanner.close();

        initCache();
        for (int i = 0; i < T; i++) {
            test(numbers[i]);
        }
    }

    private void initCache(){
        cache = new HashMap<>();
        long prime = 2L;
        cache.put(1, prime);
        for (int n = 2; n <= maxNumber; n++){
            prime = getNextPrime(prime);
            cache.put(n, prime);
        }
    }

    private void test(int n) {
        System.out.println(cache.get(n));
    }

    private long getNextPrime(long lastPrime) {
        long nextPrime = lastPrime + 1;
        while (!isPrime(nextPrime)){
            nextPrime++;
        }
        return nextPrime;
   }

   private boolean isPrime(long x){
       long prime;
       long limit = (long)(Math.sqrt(x));
       for (int i = 1; (prime = cache.get(i)) <= limit; i++) {
           if (x % prime == 0){
               return false;
           }
       }
       return true;
   }
}