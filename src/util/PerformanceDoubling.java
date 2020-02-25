package util;

import java.time.Duration;
import java.time.Instant;

import fundamentals.KnuthShuffle;

/**
 * Tests class for durations.
 * 
 * Test for O(n^3):
 *     Aprox Duration: n^3 * 1e-9 * 2
 *        example for n = 1500;
 *                        1500^3 * 1e-9 * 2 = 6.75 seconds
 *             or
 *                n^3 * 2e-9              
 *             
 * @author joaquin
 */
public class PerformanceDoubling 
{
	private static final String ALPHABET = "abcdefghijklmnopqrstuvwxyz" ;// abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ

	public static void doublingMethodAnalysis(int n) {
		double x0 = 0.0;
		double y0 = 0.0;
		long t0 = 0;
		System.out.printf("%-12s   %-12s   %-12s    %-12s   %-12s   %-12s   %-20s%n", "N", "T", "T1/T0", "lgN", "lgT", "slope", "a");
		while(n < Integer.MAX_VALUE) { // 1_024_001
			Instant start = Instant.now();

			KnuthShuffle.shuffle(generateRandomStrings(n, 10));

			Instant end = Instant.now();
			long t1 = Duration.between(start, end).getSeconds();
			double ratio = 0.0;
			if(t0 > 0 * 1e-10) {
				ratio = (double)t1/t0;
			}

			double x1 = Math.log(n)   /Math.log(2);
			double y1 = Math.log(t1)/Math.log(2);
			double slope = (y1 - y0)/(x1 - x0);
			double a = t1/Math.pow(n, slope);
			System.out.printf("%-12d   %-12d   %-12.5f    %-12.2f   %-12.2f   %-12.2f   %-20.15f%n", n, t1, ratio, x1, y1, slope, a);
			x0 = x1;
			y0 = y1;
			t0 = t1;
			n *= 2;
		}
	}

	public static long threeSum(int[] a) {
		int n = a.length;
		long count = 0;
		for(int i = 1; i < n; i++) {
			for (int j = i + 1; j < n; j++) {
				for (int k = j + 1; k < n; k++) {
					if(a[i] + a[j] + a[k] == 0) {						
						count++;
					}
				}
			}
		}
		return count;
	}
	
//	private static int[] generateRandomInts(int numbers) {
//		int[] a = new int[numbers];
//		for(int i = 0; i < numbers; i++) {
//			a[i] = RandomN.getRandomInt(-100, 100);
//		}
//		return a;
//	}
	
	private static String[] generateRandomStrings(int numbers, int k) {
		String[] a = new String[numbers];
		for(int i = 0; i < numbers; i++) {
			char[] cArr = new char[k];
			for(int j = 0; j < k; j++) {
				cArr[j] = ALPHABET.charAt(RandomN.getRandomInt(0, ALPHABET.length() - 1));
			}
			a[i] = new String(cArr);
		}
		return a;
	}

	// TESTS ========================================================
	public static void main(String[] args) {
		doublingMethodAnalysis(1);
	}
}
