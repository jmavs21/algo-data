package strings;

/**
 * This client computes the Longest Repeated Substring of a string that appears at least twice.
 * 
 * Initialization of NFA regex: O(N) time and space, where N is length of text.
 * Operations:
 *     length, index: O(1)
 *     lcp: O(length of longest prefix)
 *     select: O(length of suffix)
 */
public class LongestRepeatedSubstring 
{
	private LongestRepeatedSubstring() { }

	public static String lrs(String text) {
		int n = text.length();
		SuffixArray suffixArray = new SuffixArray(text);
		String lrs = "";
		for(int i = 1; i < n; i++) {
			int length = suffixArray.lcp(i);
			if(length > lrs.length()) {
				lrs = text.substring(suffixArray.index(i), suffixArray.index(i) + length);
			}
		}
		return lrs;
	}

	public static void main(String[] args) {
		String text = "aaaabbbbb";
		System.out.println(text);
		System.out.println("'" + lrs(text) + "'");
	}
}