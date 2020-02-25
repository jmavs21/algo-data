package strings;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Least-Significant-Digit first string sort: sort an array of w-character strings.
 * 	   Stable, O(n) space, O(w * n)
 * 
 * Extra space: O(n + R)
 * Operations: where n is the length of array, w length of string and R the radix.
 *     sort: O(w * n)
 *     
 * NOTE: usefull for short fixed-length strings.
 */
public class LSD 
{
	public static void sort(String[] arr, int w) {
		int n = arr.length;
		int R = 256; // radix R, extended ASCII alphabet size
		String[] aux = new String[n];
		for(int d = w-1; d >= 0; d--) {
			int[] count = new int[R+1];
			for(int i = 0; i < n; i++)
				count[arr[i].charAt(d) + 1]++;
			for(int i = 0; i < R; i++) 
				count[i + 1] += count[i];
			for(int i = 0; i < n; i++) 
				aux[count[arr[i].charAt(d)]++] = arr[i];
			for(int i = 0; i < n; i++) 
				arr[i] = aux[i];
		}
	}

	public static void main(String[] args) throws FileNotFoundException {
		Scanner sc = new Scanner(new FileReader("resources/strings/words3.txt"));
		ArrayList<String> arr = new ArrayList<String>();
		while(sc.hasNext()) {
			arr.add(sc.next());
		}
		sc.close();
		String[] a = arr.toArray(new String[arr.size()]);
		int n = arr.size();
		int w = arr.get(0).length();
		sort(a, w);
		for (int i = 0; i < n; i++)
			System.out.println(a[i]);
	}
}