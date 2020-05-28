package sorting;

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.Comparator;

import util.RandomN;
import util.Student;

/**
 * Merge sort:
 * 	   Stable, Not Adaptive*, O(n) space, Θ(n log n).
 *  
 * Operations: where n is number of elements.
 *     sort: Θ(n log n) time. (can do thousand in instant, and million in 1 second and billion in 18 minutes, could be improve with insertion sort for 8 elements sub-arrays.)
 *     
 * NOTE: general purpose stable sort, n log n guarantee.
 */
public class MergeSort 
{
	private MergeSort() {}

	private static final int CUTOFF = 8;

	/**
	 *     --------------------
	 *     |        |         |
	 *     --------------------
	 *     lo      mid       hi
	 *     i           j
	 *     k    from   lo to hi
	 *                 i > mid = j++
	 *                 j > hi =  i++
	 *                 j < i =   j++
	 *                           i++
	 */
	public static void sort(int[] a) {
		int[] tmp = new int[a.length];
		sort(a, tmp, 0, a.length-1);
	}
	private static void sort(int[] a, int[] tmp, int lo, int hi) {
		if(lo >= hi) return;
		int mid = (lo + hi) >>> 1;
		sort(a, tmp, lo, mid);
		sort(a, tmp, mid + 1, hi);
		merge(a, tmp, lo, mid, hi);
	}
	private static void merge(int[] a, int[] tmp, int lo, int mid, int hi) {
		for(int k = lo; k <= hi; k++) tmp[k] = a[k];
		int i = lo, j = mid+1;
		for(int k = lo; k <= hi; k++) {
			if(i > mid)              a[k] = tmp[j++];
			else if(j > hi)          a[k] = tmp[i++];
			else if(tmp[j] < tmp[i]) a[k] = tmp[j++];
			else                     a[k] = tmp[i++];
		}
	}

	// generics comparator
	public static <T> void sort(T[] a, T[] tmp, Comparator<T> comparator, int lo, int hi) { // top-down mergesort
		if(lo + CUTOFF - 1 >= hi) {
			InsertionSort.sort(a, comparator, lo, hi); // maybe 20% faster
			return;
		}
		int mid = (lo + hi) >>> 1;
		sort(a, tmp, comparator, lo, mid);
		sort(a, tmp, comparator, mid + 1, hi); // the two sorts are doing in total (2^n) recursive time. But n is been divided by 2^n which is the same as (n/2^n) which is (log n), which is the depth of the tree.
		if(comparator.compare(a[mid], a[mid+1]) < 0 || comparator.compare(a[mid], a[mid+1]) == 0) {
			return; // * don't merge if two halves already sorted (biggest-item in first half <= than smallest-item in second half), makes it kind of adaptive
		}
		merge(a, tmp, comparator, lo, mid, hi); // called (log n) times, but merge doing (n) time, so (n log n) is the final time.
	}
	private static <T> void merge(T[] a, T[] tmp, Comparator<T> comparator, int lo, int mid, int hi) {
		for(int k = lo; k <= hi; k++) { // this loop can be removed by using the 'switching aux for array', but makes it more complex to understand.
			tmp[k] = a[k];
		}
		int i = lo;
		int j = mid + 1;
		for(int k = lo; k <= hi; k++) {
			if(i > mid) a[k] = tmp[j++];
			else if(j > hi) a[k] = tmp[i++];
			else if(comparator.compare(tmp[j], tmp[i]) < 0)	a[k] = tmp[j++];
			else a[k] = tmp[i++]; // left lessOrEqual to right (A|A!A!B___A!A!C!E) to perserve order (stable)
		}
	}

	// merge sort iterative
	public static <T> void sortIterative(T[] a, T[] tmp, Comparator<T> comparator) { // bottom-up mergesort
		int n = a.length;
		for(int len = 1; len < n; len = len+len) {
			for(int lo = 0; lo < n-len; lo += len+len) { // since each time double, then you are doing (log n) time.
				int mid = lo+len-1;
				int hi = Math.min(lo+len+len-1, n-1);
				System.out.println("lo="+lo + ", mid="+mid + ", hi="+hi);
				merge(a, tmp, comparator, lo, mid, hi); // called (log n) times, but merge doing (n) time, so (n log n) is the final time.
			}
		}
	}
	
	// TESTS ========================================================
	public static void main(String[] args) {
		int[] arr = { 4, 8, 1, 0, 3, 4, 9, 2 };
		sort(arr);
		System.out.println(Arrays.toString(arr));

		int size = 10; // 10 million, 10_000_000
		Integer[] test = new Integer[size];
		System.out.println(Arrays.toString(test));
		for(int i = 0; i < test.length; i++) {
			test[i] = RandomN.getRandomInt(0, size);
		}
		Instant start = Instant.now();
		// sort(test, new Integer[test.length], Integer::compareTo, 0, test.length - 1);
		sortIterative(test, new Integer[test.length], Integer::compareTo);
		System.out.println(Arrays.toString(test));
		Instant end = Instant.now();
		System.out.println("Duration = " + Duration.between(start, end).getSeconds());

		System.out.println();

		//		Integer[] test2 = new Integer[] {4, 8, 1, 0, 3, 4, 9, 2 };
		int[] test2 = new int[] {4, 8, 1, 0, 3, 4, 9, 2 };
		System.out.println(Arrays.toString(test2));
		sort(test2);
		System.out.println(Arrays.toString(test2));

		System.out.println();

		Student[] students = new Student[] {new Student(4, "dac", 1), new Student(3, "CAA", 3), new Student(3, "CCC", 3), new Student(2, "bac", 4),new Student(1, "acb", 5) };
		System.out.println("input order=          " + Arrays.toString(students));
		sort(students, new Student[students.length], Student::compareTo, 0, students.length - 1);
		System.out.println("natural (id) order=   " + Arrays.toString(students));
		sort(students, new Student[students.length], Student.BY_NAME, 0, students.length - 1);
		System.out.println("name order=           " + Arrays.toString(students));
		sort(students, new Student[students.length], Student.BY_SECTION, 0, students.length - 1);
		System.out.println("section order=        " + Arrays.toString(students));
	}
}