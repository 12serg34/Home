package com.home.ProjectEuler;

import java.util.HashMap;
import java.util.Scanner;

public class SumOfPrimeNumbers {

    private int maxPrime;
    private HashMap<Integer, Long> primes;
    private HashMap<Long, Long> cache;

    public static void main(String[] args) {
        new SumOfPrimeNumbers().run();
    }

    private void run() {
        Scanner scanner = new Scanner(System.in);
        int T = scanner.nextInt();
        int[] numbers = new int[T];
        for (int i = 0; i < T; i++) {
            numbers[i] = scanner.nextInt();
            maxPrime = numbers[i] > maxPrime ? numbers[i] : maxPrime;
        }
        scanner.close();

        initCache();
        for (int i = 0; i < T; i++) {
            test(numbers[i]);
        }
    }

    private void initCache() {
        long prime = 2L;
        primes = new HashMap<>();
        primes.put(1, prime);
        long sum = prime;
        cache = new HashMap<>();
        cache.put(prime, sum);
        for (int n = 2; prime < maxPrime; n++) {
            prime = getNextPrime(prime);
            sum += prime;
            primes.put(n, prime);
            cache.put(prime, sum);
        }
    }

    private void test(int n) {
        System.out.println(cache.get(getLastPrime(n)));
    }

    private long getNextPrime(long lastPrime) {
        long nextPrime = lastPrime + 1;
        while (!isPrime(nextPrime)) {
            nextPrime++;
        }
        return nextPrime;
    }

    private long getLastPrime(long value) {
        int left = 1;
        int right = primes.size();
        return getLastPrime(value, left, right);
    }

    private long getLastPrime(long value, int left, int right) {
        if (right - left < 4) {
            while (primes.get(right) > value) {
                right--;
            }
            return primes.get(right);
        }

        int middle = (right + left) / 2;
        long middlePrime = primes.get(middle);
        if (value == middlePrime) {
            return value;
        }
        if (value > middlePrime) {
            return getLastPrime(value, middle, right);
        } else {
            return getLastPrime(value, left, middle + 1);
        }
    }

    private boolean isPrime(long x) {
        long prime;
        long limit = (long) (Math.sqrt(x));
        for (int i = 1; (prime = primes.get(i)) <= limit; i++) {
            if (x % prime == 0) {
                return false;
            }
        }
        return true;
    }
}