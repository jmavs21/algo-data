package sorting;

import java.util.Arrays;
import java.util.Comparator;

import util.Student;

/**
 * Selection sort:
 * 	   Not Stable, Not Adaptive, O(1) space, Θ(n^2) compares but O(n) swaps.
 *  
 * Operations: where n is number of elements.
 *     sort: Θ(n^2) comparisons. (can do thousand in instant, and million in 2.8 hours and billion in 317 years)
 * 
 * NOTE: n swaps.
 */
public class SelectionSort 
{
	private SelectionSort() {}

	public static void sort(int[] a) {
		for(int i = 0; i < a.length; i++) {
			int min = i;
			for(int j = i + 1; j < a.length; j++) {
				if(a[j] < a[min]) 
					min = j;
			}
			swap(a, i, min);
		}
	}

	public static void swap(int[] a, int i, int j) {
		int tmp = a[i];
		a[i] = a[j];
		a[j] = tmp;
	}

	// generic
	public static <T> void sort(T[] a, Comparator<T> comparator) {
		final int n = a.length;
		for(int i = 0; i < n; i++) {
			int min = i;
			for(int j = i + 1; j < n; j++) {
				if(comparator.compare(a[j], a[min]) < 0) {
					min = j;
				}
			}
			swap(a, i, min);
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
		sort(test, Integer::compareTo); // using method reference
		System.out.println(Arrays.toString(test));

		Student[] students = new Student[] {new Student(4, "dac", 1), new Student(3, "CAA", 3), new Student(3, "CCC", 3), new Student(2, "bac", 4),new Student(1, "acb", 5) };
		System.out.println("input order=          " + Arrays.toString(students));
		sort(students, Student::compareTo);
		System.out.println("natural (id) order=   " + Arrays.toString(students));
		sort(students, Student.BY_NAME); // sort(students, (o1, o2) -> o1.name.compareTo(o2.name));
		System.out.println("name order=           " + Arrays.toString(students));
		sort(students, Student.BY_SECTION);
		System.out.println("section order=        " + Arrays.toString(students));
	}
}
