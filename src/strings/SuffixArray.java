package strings;

import java.util.Arrays;

/**
 * Suffix Array of a string of length n.
 * 
 * Initialization of NFA regex: O(N) time and space, where N is length of text.
 * Operations:
 *     length, index: O(1)
 *     lcp: O(length of longest prefix)
 *     select: O(length of suffix)
 */
public class SuffixArray 
{
	private Suffix[] suffixes;

	public SuffixArray(String text) {
		int n = text.length();
		suffixes = new Suffix[n];
		for(int i = 0; i < n; i++) 
			suffixes[i] = new Suffix(text, i);
		Arrays.sort(suffixes);
	}

	private static class Suffix implements Comparable<Suffix> {
		private final String text;
		private final int index;
		private Suffix(String text, int index) {
			this.text = text;
			this.index = index;
		}
		private int length() {
			return text.length() - index;
		}
		private char charAt(int i) {
			return text.charAt(index + i);
		}
		public int compareTo(Suffix that) {
			if (this == that) return 0;
			int n = Math.min(this.length(), that.length());
			for (int i = 0; i < n; i++) {
				if (this.charAt(i) < that.charAt(i)) return -1;
				if (this.charAt(i) > that.charAt(i)) return +1;
			}
			return this.length() - that.length();
		}
		public String toString() {
			return text.substring(index);
		}
	}

	public int length() {
		return suffixes.length;
	}

	public int index(int i) {
		if(i < 0 || i >= suffixes.length) throw new IllegalArgumentException();
		return suffixes[i].index;
	}

	/**
	 * Returns the length of the longest common prefix of the ith smallest suffix and the i-1st smallest suffix.
	 */
	public int lcp(int i) {
		if(i < 1 || i >= suffixes.length) throw new IllegalArgumentException();
		return lcp(suffixes[i], suffixes[i - 1]);
	}
	public int lcp(Suffix a, Suffix b) {
		int n = Math.min(a.length(), b.length());
		for(int i = 0; i < n; i++) {
			if(a.charAt(i) != b.charAt(i))
				return i;
		}
		return n;
	}

	public String select(int i) {
		if (i < 0 || i >= suffixes.length) throw new IllegalArgumentException();
		return suffixes[i].toString();
	}

	public int rank(String query) {
		int lo = 0;
		int hi = suffixes.length - 1;
		while (lo <= hi) {
			int mid = lo + (hi - lo) / 2;
			int cmp = compare(query, suffixes[mid]);
			if      (cmp < 0) hi = mid - 1;
			else if (cmp > 0) lo = mid + 1;
			else              return mid;
		}
		return lo;
	}

	private static int compare(String query, Suffix suffix) {
		int n = Math.min(query.length(), suffix.length());
		for (int i = 0; i < n; i++) {
			if (query.charAt(i) < suffix.charAt(i)) return -1;
			if (query.charAt(i) > suffix.charAt(i)) return +1;
		}
		return query.length() - suffix.length();
	}

	public static void main(String[] args) {
		String s = "ABRACADABRA!";
		System.out.println(s);
		SuffixArray suffix = new SuffixArray(s);
		System.out.println("  i ind lcp rnk select");
		System.out.println("---------------------------");
		for (int i = 0; i < s.length(); i++) {
			int index = suffix.index(i);
			String ith = "\"" + s.substring(index, Math.min(index + 50, s.length())) + "\"";
			assert s.substring(index).equals(suffix.select(i));
			int rank = suffix.rank(s.substring(index));
			if (i == 0) {
				System.out.printf("%3d %3d %3s %3d %s\n", i, index, "-", rank, ith);
			}
			else {
				int lcp = suffix.lcp(i);
				System.out.printf("%3d %3d %3d %3d %s\n", i, index, lcp, rank, ith);
			}
		}
	}
}