package fundamentals;

import java.util.Arrays;

/**
 * Binary Search algorithm, with iterative or recursive implementation.
 * 
 * Example:
 *   find k = 4:
 *     indices:   0     1     2     3     4     5     6     7     8     
 *             |-----------------------------------------------------|
 *       array:|  1     2     3     4*    5     6     7     8     9  |
 *             |-----------------------------------------------------|
 *    mid = 4     l                       m                       h
 *    mid = 1     l     m           h
 *    mid = 2                l m    h
 *    mid = 3                     l m h
 *                                a[3]==4
 *  Operations: where n is the number of elements in array.
 *      binarySearch, binarySearchRecur: O(log n) in the worst case.
 */
public class BinarySearch {
  private BinarySearch() {}

  public static int binarySearch(int[] a, int k) {
    int lo = 0;
    int hi = a.length - 1;
    while (lo <= hi) {
      int mid = lo + (hi - lo) / 2;
      if (a[mid] < k) lo = mid + 1;
      else if (a[mid] > k) hi = mid - 1;
      else return mid;
    }
    return -(lo + 1);
  }

  public static int binarySearchRecur(int[] a, int k) {
    return binarySearchRecur(a, k, 0, a.length - 1);
  }

  private static int binarySearchRecur(int[] a, int k, int lo, int hi) {
    if (lo <= hi) {
      int mid = lo + (hi - lo) / 2;
      if (a[mid] < k) return binarySearchRecur(a, k, mid + 1, hi);
      else if (a[mid] > k) return binarySearchRecur(a, k, lo, mid - 1);
      else return mid;
    }
    return -(lo + 1);
  }

  // TESTS ========================================================
  public static void main(String[] args) {
    int[] a = {0, 2, 5, 7, 9, 10, 15};
    Arrays.sort(a);
    System.out.println(binarySearch(a, -1));
    System.out.println(Arrays.binarySearch(a, -1));
    System.out.println(binarySearch(a, 16));
    System.out.println(Arrays.binarySearch(a, 16));
    System.out.println("Search on array from index 0 to length + 1: " + Arrays.toString(a));

    System.out.println("---------- Iterative verison ----------");
    for (int i = 0; i <= a.length + 1; i++) {
      System.out.printf("Index %d search: %d%n", i, binarySearch(a, i));
    }

    System.out.println("---------- Recursive verison ----------");
    for (int i = 0; i <= a.length + 1; i++) {
      System.out.printf("Index %d search: %d%n", i, binarySearchRecur(a, i));
    }
  }
}
