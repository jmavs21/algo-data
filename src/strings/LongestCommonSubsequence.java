package strings;

/**
 * Longest common subsequence. Using Bottom-Up Dynamic Programming.
 *    O(M * N) space and time
 *       Where:
 *          M = length of first string.
 *          N = length of second string.
 * ex. 
 *     ggcaccacg (rows)
 *     acggcggatacg (cols)
 *     LCS = 7 (ggcaacg)  O(M + N) to trace back lcs. 
 *     
 *         0  1  2  3  4  5  6  7  8  9  10 11 12
 *         a  c  g  g  c  g  g  a  t  a  c  g  -
 *   0  g [7, 7, 7, 6, 6, 6, 5, 4, 3, 3, 2, 1, 0]
 *   1  g [6, 6, 6, 6, 5, 5, 5, 4, 3, 3, 2, 1, 0]
 *   2  c [6, 5, 5, 5, 5, 4, 4, 4, 3, 3, 2, 1, 0]
 *   3  a [6, 5, 4, 4, 4, 4, 4, 4, 3, 3, 2, 1, 0]
 *   4  c [5, 5, 4, 4, 4, 3, 3, 3, 3, 3, 2, 1, 0]
 *   5  c [4, 4, 4, 4, 4, 3, 3, 3, 3, 3, 2, 1, 0]
 *   6  a [3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 2, 1, 0]
 *   7  c [2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1, 0]
 *   8  g [1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0]
 *   9  - [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]
 */
public class LongestCommonSubsequence {

	public static void main(String[] args) {
		String s = "ggcaccacg";
		String t = "acggcggatacg";
		int m = s.length();
		int n = t.length();
		int[][] opt = new int[m + 1][n + 1];
		for(int i = m - 1; i >= 0; i--) {
			for(int j = n - 1; j >= 0; j--) {
				if(s.charAt(i) == t.charAt(j)) 
					opt[i][j] = opt[i + 1][j + 1] + 1;
				else 
					opt[i][j] = Math.max(opt[i + 1][j], opt[i][j + 1]);
			}
		}
		// reover lcs itself backward
		StringBuilder lcs = new StringBuilder();
		int i = 0, j = 0;
		while(i < m && j < n) {
			if(s.charAt(i) == t.charAt(j)) {
				lcs.append(s.charAt(i));
				i++;
				j++;
			} else if(opt[i + 1][j] >= opt[i][j + 1]) 
				i++;
			else 
				j++;
		}
		System.out.println(opt[0][0]);
		System.out.println(lcs.toString());
	}
}
