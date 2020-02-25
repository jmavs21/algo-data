package fundamentals;

import java.util.Arrays;

import util.RandomN;

/**
 * The Knuth shuffle algoritm (also known Fisher-Yates). 
 *     Implemens a rearrangement of elements in uniformly random order.
 * 
 * Operations: where n is the number of elements in array.
 *      knuthShuffle: O(n)
 */
public class KnuthShuffle {

	public static <T> void shuffle(T[] array) {
		for(int i = 0; i < array.length; i++) {
			swap(array, i, RandomN.getRandomInt(0, i));
		}
	}
	
	public static void shuffle(int[] arr) {
		for(int i = 0; i < arr.length; i++) {
			swap(arr, i, RandomN.getRandomInt(0, i));
		}
	}
	
	private static void swap(int[] a, int i, int j) {
		int tmp = a[i];
		a[i] = a[j];
		a[j] = tmp;
	}
	
	private static <T> void swap(T[] array, int i, int j) {
		T temp = array[i];
		array[i] = array[j];
		array[j] = temp;
	}
	
	// TESTS ========================================================
	public static void main(String[] args) {
		Integer[] arr = new Integer[] { 0, 1, 2, 3, 4, 4, 8, 9 };
		System.out.println(Arrays.toString(arr));
		shuffle(arr);
		System.out.println(Arrays.toString(arr));
		
		int[] test = { 0, 1, 2, 3, 4, 4, 8, 9 };
		System.out.println(Arrays.toString(test));
		shuffle(test);
		System.out.println(Arrays.toString(test));
	}
}