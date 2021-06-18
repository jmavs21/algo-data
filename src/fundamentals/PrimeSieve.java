package fundamentals;

import java.util.Arrays;

/**
 * Program to calculate all the primes through n by using Sieve of Eratoshenes algorithm.
 * 
 * Operations: where n is size of array.
 *     sieve: O(n*log(log(n)))
 */
public class PrimeSieve {
  public static void sieve(int n) {
    boolean[] isPrime = new boolean[n + 1];
    Arrays.fill(isPrime, true);
    for (int i = 2; i <= n / i; i++) {
      if (isPrime[i]) {
        for (int j = i; j <= n / i; j++) {
          isPrime[i * j] = false;
        }
      }
    }
    System.out.print("Primes of " + n + " = ");
    for (int i = 2; i <= n; i++) {
      if (isPrime[i]) System.out.print(i + " ");
    }
    System.out.println();
  }

  // TESTS ========================================================
  public static void main(String[] args) {
    sieve(10);
  }
}
