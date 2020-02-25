package strings;

/**
 * Implementation of Boyer-Moore Substring Search algorithm.
 * It precomputes the pattern in the constructor with the values that the text is going to be skiped.
 * 
 * Initialization of DFA pattern: O(R + M) time and space.
 * Operations:
 *     searchIndexOf: O(N*M) worst case. Mismatched heuristic takes about O(N/M)
 *     
 *   Where:
 *     N = length of text
 *     M = length of pattern
 *     R = length of alphabet
 * 
 * Skip table computation (right array):
 *            N  E  E  D  L  E
 *   R        0  1  2  3  4  5   right[c]
 *   -------------------------------------
 *   A  -1   -1 -1 -1 -1 -1 -1    -1
 *   B  -1   -1 -1 -1 -1 -1 -1    -1
 *   C  -1   -1 -1 -1 -1 -1 -1    -1
 *   D  -1   -1 -1 -1  3* 3  3     3*
 *   E  -1   -1  1* 2* 2  2  5*    5*
 *   .........................    -1
 *   L  -1   -1 -1 -1 -1  4* 4     4*
 *   M  -1   -1 -1 -1 -1 -1 -1    -1
 *   N  -1    0* 0  0  0  0  0     0*
 *   .........................    -1
 * 
 * OR
 * 
 * R = 256   0  1  2  3  4...11 12 13...
 *           ----------------------------
 * ASCII     A  B  C  D  E... L  M  N...
 *           ----------------------------
 * right[c]=                        0*  (empty spaces are -1, letters not in NEEDLE)
 *                       1*         0
 *                       2*         0
 *                    3* 2          0
 *                    3  2    4*    0
 *                    3  5*   4     0  
 */
public class BoyerMoore 
{
	private static final int R = 256; // ASCII extended
	private int[] skipPat;
	private String pat;
	private int m;

	public BoyerMoore(String pat) {
		this.pat = pat;
		m = pat.length();
		skipPat = new int[R];
		for(int c = 0; c < R; c++) 
			skipPat[c] = -1;
		for(int j = 0; j < m; j++) 
			skipPat[pat.charAt(j)] = j;
	}

	public int searchIndexOf(String txt) {
		int n = txt.length();
		int i = 0;
		while(i <= n - m) {
			int skip = 0;
			for(int j = m-1; j >= 0; j--) {
				if(txt.charAt(i+j) != pat.charAt(j)) {
					skip = Math.max(1, j - skipPat[txt.charAt(i+j)]);
					i = i + skip;
					break;
				}
			}
			if(skip == 0) return i;
		}
		return n;
	}

	public static void main(String[] args) {
		String txt = "abacadabrabracabracadabrabrabracad";
		String pat = "abracadabra<<";
		BoyerMoore bm = new BoyerMoore(pat);
		int offset = bm.searchIndexOf(txt);
		System.out.println("text:   " + txt);
		System.out.print(  "pattern:");
		for (int i = 0; i < offset; i++)
			System.out.print(" ");
		System.out.println(pat);
	}
}