package sorting;

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.Comparator;

import util.RandomN;
import util.Student;

/**
 * Heap sort:
 * 	   Not Stable, Not Adaptive, O(1) extra space, O(n log n) compares.
 *  
 * Operations: where n is number of elements.
 *     sort: O(n log n).
 *     
 * Notes: implementation uses same sink method as in MaxPQ, but 0-based indexing.
 *      First pass: construct binary heap, by calling sink method to all nodes starting with first parent.
 *      second pass: remove (not nulling out) max nodes.
 */
public class HeapSort 
{
	private HeapSort() {}

	public static void sort(int[] a) {
		int n = a.length - 1;
		for (int i = n/2; i >= 0; i--) {			
			sink(a, i, n);
		}
		while (n > 0) {
			swap(a, 0, n--);
			sink(a, 0, n);
		}
	}
	private static void sink(int[] a, int i, int n) {
		while (2*i <= n) {
			int j = 2*i;
			if (j < n && a[j] < a[j+1]) j++;
			if (a[i] >= a[j]) 
				break;
			swap(a, i, j);
			i = j;
		}
	}
	private static void swap(int[] a, int i, int j) {
		int tmp = a[i];
		a[i] = a[j];
		a[j] = tmp;
	}

	public static <T> void sort(T[] a, Comparator<T> comparator)  {
		int n = a.length - 1;
		for (int i = n/2; i >= 0; i--) { // heap constrution. Starts with the first parent, to sink rest of nodes till reaching the root.
			sink(comparator, a, i, n);
		}
		while (n > 0) { // sortdown (remove but no nulling out), similar to delMax method in MaxPQ
			swap(a, 0, n--);
			sink(comparator, a, 0, n);
		}
	}
	private static <T> void sink(Comparator<T> comparator, T[] a, int i, int n) {
		while(2 * i <= n) {
			int j = 2 * i;
			if(j < n && comparator.compare(a[j], a[j + 1]) < 0){ // select biggest child
				j++;
			}
			if(!(comparator.compare(a[i], a[j]) < 0)) { // k is not less than the biggest child, stop
				break;
			}
			swap(a, i, j);
			i = j;
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

		int size = 100; // 10 million, 10_000_000
		Integer[] test0 = new Integer[size];
		System.out.println(Arrays.toString(test0));
		for(int i = 0; i < test0.length; i++) {
			test0[i] = RandomN.getRandomInt(0, size);
		}
		Instant start = Instant.now();
		sort(test0, Integer::compareTo);
		System.out.println(Arrays.toString(test0));
		Instant end = Instant.now();
		System.out.println("Duration = " + Duration.between(start, end).getSeconds());
		System.out.println();

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