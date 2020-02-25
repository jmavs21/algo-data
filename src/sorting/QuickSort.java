package sorting;

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.Comparator;

import fundamentals.KnuthShuffle;
import util.RandomN;
import util.Student;

/**
 * 3-way Quicksort sort:
 * 	   Not Stable, Adaptive for duplicated keys, O(log n) extra space, Θ(n log n).
 *  
 * Operations: where n is number of elements.
 *     sort: O(n2) time, but typically O(n log n) time. 3-way quicksort could sometimes be O(n) when lots of duplicated keys.
 *     
 * NOTE: general purpose when space is tight and large numbers of equal keys.
 */
public class QuickSort
{
	private QuickSort() {}

	private static final int CUTOFF = 8;

	/**
	 *    |----------|------------|----------|
	 *    | < privot |  = pivot   |  > pivot |
	 *    |----------|------------|----------|
	 *    lo         lt           gt i       hi
	 *    lt                                 gt
	 *      i          
	 *    privot
	 */
	public static void sort(int[] a) {
		KnuthShuffle.shuffle(a);
		sort(a, 0, a.length - 1);
	}
	private static void sort(int[] a, int lo, int hi) {
		if(lo >= hi) {
			return;
		}
		int lt = lo;
		int gt = hi;
		int i = lo + 1;
		int pivot = a[lo];
		while(i <= gt) {
			if     (a[i] < pivot) swap(a, lt++, i++);
			else if(a[i] > pivot) swap(a, gt--, i);
			else                  i++;
		}
		sort(a, lo, lt - 1);
		sort(a, gt + 1, hi);
	}
	private static void swap(int[] a, int i, int j) {
		int tmp = a[i];
		a[i] = a[j];
		a[j] = tmp;
	}

	// generic 3 way quicksort
	public static <T> void sort3Way(T[] a, Comparator<T> comparator) {
		KnuthShuffle.shuffle(a);
		sort3Way(a, comparator, 0, a.length - 1);
	}
	private static <T> void sort3Way(T[] a, Comparator<T> comparator, int lo, int hi) {
		if(lo + CUTOFF - 1 >= hi) {
			InsertionSort.sort(a, comparator, lo, hi);
			return;
		}
		int lt = lo;
		int gt = hi;
		T pivot = a[lo];
		int i = lo + 1;
		while (i <= gt)
		{
			if(comparator.compare(a[i], pivot) < 0) {
				swap(a, lt++, i++);
			} else if(comparator.compare(pivot, a[i]) < 0) {
				swap(a, i, gt--);
			} else {
				i++;
			}
		}
		sort3Way(a, comparator, lo, lt - 1);
		sort3Way(a, comparator, gt + 1, hi);
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
		Integer[] test = new Integer[size];
		System.out.println(Arrays.toString(test));
		for(int i = 0; i < test.length; i++) {
			test[i] = RandomN.getRandomInt(0, size);
		}
		Instant start = Instant.now();
		sort3Way(test, Integer::compareTo);
		System.out.println(Arrays.toString(test));
		Instant end = Instant.now();
		System.out.println("Duration = " + Duration.between(start, end).getSeconds());

		System.out.println();

		Integer[] test2 = new Integer[] { 4, 8, 1, 0, 3, 4, 9, 2  };
		System.out.println(Arrays.toString(test2));
		sort3Way(test2, Integer::compareTo);
		System.out.println(Arrays.toString(test2));

		System.out.println();

		Student[] students = new Student[] {new Student(4, "dac", 1), new Student(3, "CAA", 3), new Student(3, "CCC", 3), new Student(2, "bac", 4),new Student(1, "acb", 5) };
		System.out.println("input order=          " + Arrays.toString(students));
		sort3Way(students, Student::compareTo);
		System.out.println("natural (id) order=   " + Arrays.toString(students));
		sort3Way(students, Student.BY_NAME);
		System.out.println("name order=           " + Arrays.toString(students));
		sort3Way(students, Student.BY_SECTION);
		System.out.println("section order=        " + Arrays.toString(students));
	}
}
