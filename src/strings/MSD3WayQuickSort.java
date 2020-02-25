package strings;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;

import fundamentals.KnuthShuffle;

/**
 * 3-way MSD radix quicksort:
 *      Not Stable, O(w + log n) space, between O(n) and O(w * n log R)
 *      
 * Extra space: O(w + log n)
 * Operations: where n is the length of array, w length of string and R the radix.
 *     sort: O2(n log n) for random strings.
 *     
 * NOTE: usefull for general-purpose strings with long prefix matches.
 */
public class MSD3WayQuickSort
{
	private static final int CUTOFF =  15;

	public static void sort(String[] a) {
		KnuthShuffle.shuffle(a);
		sort(a, 0, a.length-1, 0);
	}

	/**
	 *    |----------|------------|----------|
	 *    | < privot |  = pivot   |  > pivot |
	 *    |----------|------------|----------|
	 *    lo         lt           gt i       hi
	 *    lt                                 gt
	 *      i          
	 *    privot
	 */
	private static void sort(String[] a, int lo, int hi, int d) { 
		if (lo + CUTOFF >= hi) {
			insertion(a, lo, hi, d);
			return;
		}
		int lt = lo;
		int gt = hi;
		int i = lo + 1;
		int pivot = charAt(a[lo], d);
		while (i <= gt) {
			int t = charAt(a[i], d);
			if      (t < pivot) swap(a, lt++, i++);
			else if (t > pivot) swap(a, gt--, i);
			else                i++;
		}
		sort(a, lo, lt-1, d);
		if (pivot >= 0) sort(a, lt, gt, d+1);
		sort(a, gt+1, hi, d);
	}

	private static int charAt(String s, int d) { 
		if (d == s.length()) 
			return -1;
		return s.charAt(d);
	}

	private static void insertion(String[] a, int lo, int hi, int d) {
		for (int i = lo; i <= hi; i++) {
			for (int j = i; j > lo && less(a[j], a[j-1], d); j--) {
				swap(a, j, j-1);
			}
		}
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
