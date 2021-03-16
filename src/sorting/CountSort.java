package sorting;

import java.util.Arrays;

/**
 * Count sort (Cyclic sort): T = O(N), S = O(1), could use more space if can't modify array.
 *
 * <p>NOTE: the range should be from 0 to N, where N is not that large. And all unique values,
 * otherwise duplicates will we be out of place.
 *
 * <p>Algorithm: try first num (i) get correct index place of num (j) if not in correct place, then
 * swap them, by putting (i) in the correct place (j), then we check new value of num (i) else
 * advance for next num (i)
 */
public class CountSort {

  public static void main(String[] args) {
    int[] nums = {1, 5, 0, 4, 2, 3, 0, 1};
    System.out.println("original = " + Arrays.toString(nums));
    countSort(nums);
    System.out.println("sorted   = " + Arrays.toString(nums));
  }

  public static void countSort(int[] nums) {
    int i = 0;
    while (i < nums.length) {
      int j = nums[i];
      if (nums[i] != nums[j]) swap(nums, i, j);
      else i++;
    }
  }

  private static void swap(int[] nums, int i, int j) {
    int tmp = nums[i];
    nums[i] = nums[j];
    nums[j] = tmp;
  }

  private static void shuffle(int[] nums) {
    for (int i = 0; i < nums.length; i++) {
      swap(nums, i, (int) (Math.random() * (i + 1 - 0) + 0));
    }
  }
}
