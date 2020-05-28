package util;

import java.util.Arrays;
import java.util.Random;

/**
 * Class to return a random number in range from min and max (both inclusive) 
 *  or an int array of n elements in range.
 * 
 * @author joaquin
 */
public class RandomN
{
	static Random random = new Random();

	public static int getRandomInt(int min, int max) {
		return random.nextInt((max - min) + 1) + min;
	}

	public static int[] getRandomIntSequence(int n, int min, int max) {
		int[] arr = new int[n];
		for(int i = 0; i < arr.length; i++) {
			arr[i] = getRandomInt(min, max);
		}
		return arr;
	}

	public static double getRandomDouble() {
		return random.nextDouble(); // (int)(Math.random()*(max+1-min)+min)
	}
	
	public static int getRandomInt(int n) {
		return random.nextInt(n);
	}
	
    public static boolean bernoulli(double p) {
        if (!(p >= 0.0 && p <= 1.0))
            throw new IllegalArgumentException("probability p must be between 0.0 and 1.0: " + p);
        return getRandomDouble() < p;
    }

    public static boolean bernoulli() {
        return bernoulli(0.5);
    }

	// TESTS ========================================================
	public static void main(String[] args) {
		System.out.println("Randome number from 1 to 9: " + getRandomInt(1, 9));
		System.out.println("Random sequence of lenght 50 from 1 to 9: " + Arrays.toString(getRandomIntSequence(50, 1, 9)));
	}
}