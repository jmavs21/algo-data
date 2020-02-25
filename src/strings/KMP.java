package strings;

/**
 * Implementation of Knuth Morris Pratt (KMP) altorithm for Substring Search.
 * It precomputes a Deterministic Finite state Automaton (DFA) of the pattern to search
 * on a continum input stream (so no need of going back in text string).
 * 
 * Initialization of DFA pattern: O(R*M) time and space.
 * Operations:
 *     searchIndexOf: O(N)
 *     
 *     Where:
 *     N = length of text
 *     M = length of pattern
 *     R = length of alphabet
 * 	
 * Matrix of pattern DFA internal representation (with alphabet of only ABC):
 *     0  1  2  3  4  5 --> m
 *     A  B  A  B  A  C --> pat.chartAt(j) to find row
 *     ----------------
 *  A  1' 1  3' 1  5' 1    
 *  B  0  2' 0  4' 0  4
 *  C  0  0  0  0  0  6'
 *     ----------------
 *  r  x  j
 *  R             
 * Graphical representation of pattern (only match transitions):
 *     (0)-A-->(1)-B-->(2)-A-->(3)-B-->(4)-A-->(5)-C-->(6)
 *     
 * Note: this algorithm also usefull to find a substring cyclic rotation on a text.
 */
public class KMP 
{
	private int[][] dfa;
	private final int R = 256; // ASCII extended
	private int m;

	public KMP(String pat) {
		m = pat.length();
		dfa = new int[R][m];
		dfa[pat.charAt(0)][0] = 1;
		for(int x = 0, j = 1; j < m; j++) {
			for(int r = 0; r < R; r++) 
				dfa[r][j] = dfa[r][x];
			dfa[pat.charAt(j)][j] = j+1;
			x = dfa[pat.charAt(j)][x];
		}
	}

	public int searchIndexOf(String txt) {
		int n = txt.length();
		int i, j;
		for(i = 0, j = 0; i < n && j < m; i++) 
			j = dfa[txt.charAt(i)][j];
		if(j == m) 
			return i - m;
		return n;
	}

	public static void main(String[] args) {
		String txt = "abacadabrabracabracadabrabrabracad";
		String pat = "abracadabra";
        KMP kmp = new KMP(pat);
        int offset = kmp.searchIndexOf(txt);
        System.out.println("text:   " + txt);
        System.out.print(  "pattern:");
        for (int i = 0; i < offset; i++)
            System.out.print(" ");
        System.out.println(pat);
	}
}