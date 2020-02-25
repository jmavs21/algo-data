package sorting;

import java.util.Arrays;
import java.util.Comparator;

import util.Student;

/**
 * Shell sort:
 * 	   Not Stable, Adaptive (n log3 n), O(1) space, O(n^3/2) compares.
 *  
 * Operations: where n is number of elements.
 *     sort: O(n^3/2) using Knuth's increment sequence.
 *     
 * NOTE: subquadratic, tight code.
 */
public class ShellSort 
{
	private ShellSort() {}

	public static void sort(int[] a) {
		int n = a.length;
		int h = 1;
		while(h < n/3) {
			h = 3*h + 1;
		}
		while(h >= 1) {
			for(int i = h; i < n; i++) {
				for(int j = i; j >= h && a[j] < a[j-h]; j -= h) {
					swap(a, j, j-h);
				}
			}
			h = h/3;
		}
	}
	private static void swap(int[] a, int i, int j) {
		int tmp = a[i];
		a[i] = a[j];
		a[j] = tmp;
	}

	// generic
	public static <T> void sort(T[] a, Comparator<T> comparator) {
		final int n = a.length;
		int h = 1;
		while (h < n/3) {
			h = 3*h + 1; // 1, 4, 13, 40, 121, 364, ...	
		}
		while (h >= 1) {
			for (int i = h; i < n; i++) {
				for (int j = i; j >= h && comparator.compare(a[j], a[j-h]) < 0; j -= h) {
					swap(a, j, j-h);
				}
			}
			h = h/3;
		}
	}
	private static <T> void swap(T[] a, int i, int j) {
		T temp = a[i];
		a[i] = a[j];
		a[j] = temp;
	}

	// TESTS ========================================================
	public static void main(String[] args) {
		int[] arr = { 4, 8, 1, 0, 3, 4, 9, 2 };
		sort(arr);
		System.out.println(Arrays.toString(arr));

		Integer[] test = new Integer[] { 4, 8, 1, 0, 3, 4, 9, 2 };
		System.out.println(Arrays.toString(test));
		sort(test, Integer::compareTo);
		System.out.println(Arrays.toString(test));

		Student[] students = new Student[] {new Student(4, "dac", 1), new Student(3, "CAA", 3), new Student(3, "CCC", 3), new Student(2, "bac", 4),new Student(1, "acb", 5) };
		System.out.println("input order=          " + Arrays.toString(students));
		sort(students, Student::compareTo);
		System.out.println("natural (id) order=   " + Arrays.toString(students));
		sort(students, Student.BY_NAME);
		System.out.println("name order=           " + Arrays.toString(students));
		sort(students, Student.BY_SECTION);
		System.out.println("section order=        " + Arrays.toString(students));
	}
}
