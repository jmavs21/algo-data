package sorting;

import java.util.Arrays;
import java.util.Comparator;

import util.Student;

/**
 * Insertion sort:
 * 	   Stable, Adaptive O(n), O(1) space, O(n^2) compares worst case.
 *  
 * Operations: where n is number of elements.
 *     sort: O(n^2) comparisons and swaps. (worse case can do thousand in instant, and million in 2.8 hours and billion in 317 years)
 *           O(n) when array in order.
 *
 * NOTE: usefull for small arrays or partially-sorted arrays.
 */
public class InsertionSort 
{
	private InsertionSort() {}

	public static void sort(int[] a) {
		for(int i = 0; i < a.length; i++) {
			for(int j = i; j > 0 && a[j] < a[j-1]; j--) {
				swap(a, j, j-1);
			}
		}
	}
	private static void swap(int[] a, int i, int j) {
		int tmp = a[i];
		a[i] = a[j];
		a[j] = tmp;
	}

	// generic version
	public static <T> void sort(T[] a, Comparator<T> comparator) {
		for(int i = 1; i < a.length; i++) {
			for(int j = i; j > 0 && comparator.compare(a[j], a[j-1]) < 0; j--) {
				swap(a, j, j-1);
			}
		}
	}

	// use for mergeSort when 7 elements
	public static <T >void sort(T[] a, Comparator<T> comparator, int lo, int hi) {
		for(int i = lo; i <= hi; i++) {
			for(int j = i; j > lo && comparator.compare(a[j], a[j-1]) < 0; j--) {
				swap(a, j, j-1);
			}
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

		Integer[] test = new Integer[] { 4, 8, 1, 0, 3, 4, 9, 2  };
		System.out.println(Arrays.toString(test));
		sort(test, Integer::compareTo);
		System.out.println(Arrays.toString(test));

		Student[] students = new Student[] {new Student(4, "dac", 1), new Student(3, "CAA", 3), new Student(3, "CCC", 3), new Student(2, "bac", 4),new Student(1, "acb", 5) };
		System.out.println("input order=          " + Arrays.toString(students));
		sort(students, Student::compareTo);
		System.out.println("natural (id) order=   " + Arrays.toString(students));
		sort(students, Student.BY_NAME); // (o1, o2) -> o1.name.compareTo(o2.name)
		System.out.println("name order=           " + Arrays.toString(students));
		sort(students, Student.BY_SECTION);
		System.out.println("section order=        " + Arrays.toString(students));
	}
}
