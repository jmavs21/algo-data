package strings;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Most-Significant-Digit first string sort: sort a random array of w-character strings.
 *      Stable, O(w * R + n) space, between O(n) and O(w * n)
 *      
 * Extra space: O(w * R + n)
 * Operations: where n is the length of array, w length of string and R the radix.
 *     sort: O(n log n) for random strings.
 *     
 * NOTE: usefull for random strings (not a lot of equal strings).
 */
public class MSD 
{
	private static final int R = 256;
	private static final int CUTOFF =  15;

	private MSD() {}
	
	public static void sort(String[] arr) {
		String[] aux = new String[arr.length];
		sort(arr, aux, 0, arr.length - 1, 0);
	}

	private static void sort(String[] arr, String[] aux, int lo, int hi, int d) {
		if (hi <= lo + CUTOFF) {
			insertion(arr, lo, hi, d);
			return;
		}
		int[] count = new int[R + 2];
		for(int i = lo; i <= hi; i++) 
			count[charAt(arr[i], d) + 2]++;
		for(int i = 0; i < R + 1; i++) 
			count[i + 1] += count[i];
		for(int i = lo; i <= hi; i++) 
			aux[count[charAt(arr[i], d) + 1]++] = arr[i];
		for(int i = 0; i <= hi; i++) 
			arr[i] = aux[i - lo];
		for(int i = 0; i < R; i++) 
			sort(arr, aux, lo + count[i], lo + count[i + 1] - 1, d + 1);
	}

	private static void insertion(String[] a, int lo, int hi, int d) {
		for (int i = lo; i <= hi; i++)
			for (int j = i; j > lo && less(a[j], a[j-1], d); j--)
				swap(a, j, j-1);
	}

	private static void swap(String[] a, int i, int j) {
		String tmp = a[i];
		a[i] = a[j];
		a[j] = tmp;
	}

	private static boolean less(String v, String w, int d) {
		for (int i = d; i < Math.min(v.length(), w.length()); i++) {
			if (v.charAt(i) < w.charAt(i)) return true;
			if (v.charAt(i) > w.charAt(i)) return false;
		}
		return v.length() < w.length();
	}

	private static int charAt(String s, int d) {
		if(d == s.length()) 
			return -1;
		return s.charAt(d);
	}

	public static void main(String[] args) throws FileNotFoundException {
		Scanner sc = new Scanner(new FileReader("resources/strings/shells.txt"));
		ArrayList<String> arr = new ArrayList<String>();
		while(sc.hasNext()) {
			arr.add(sc.next());
		}
		sc.close();
		String[] a = arr.toArray(new String[arr.size()]);
		int n = a.length;
		sort(a);
		for (int i = 0; i < n; i++)
			System.out.println(a[i]);
	}
}