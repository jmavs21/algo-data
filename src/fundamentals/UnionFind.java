package fundamentals;

import java.util.Arrays;

/**
 * Union-Find (Disjoint-sets) to model connectivity of n sites, implemented with:
 * 		Path Compression by halving (update children parents when using find method).
 * 		Weighted Quick Union by Rank (less depth)
 *  
 *  Initialization: O(n) where n is the number of sites in array.
 *  Operations: 
 *      union, find, connected: O(log n) in the worst case
 *      count: O(1)
 */
public class UnionFind 
{
	private int[] parent; // parent of index
	private byte[] rank; // never more than 31
	private int count; // number of components

	public UnionFind(int n) {
		if (n < 0) throw new IllegalArgumentException();
		count = n;
		parent = new int[n];
		rank = new byte[n]; // all zeros
		for (int i = 0; i < n; i++) {
			parent[i] = i;
		}
	}

	public int count() {
		return count;
	}

	public boolean connected(int p, int q) {
		return find(p) == find(q);
	}

	public int find(int p) {
		validate(p);
		while (p != parent[p]) {
			parent[p] = parent[parent[p]];
			p = parent[p];
		}
		return p;
	}

	public void union(int p, int q) {
		int pP = find(p);
		int qP = find(q);
		if (pP == qP) return;
		if      (rank[pP] < rank[qP]) parent[pP] = qP;
		else if (rank[qP] < rank[pP]) parent[qP] = pP;
		else {
			parent[qP] = pP;
			rank[pP]++;
		}
		count--;
	}

	private void validate(int p) {
		int n = parent.length;
		if (p < 0 || p >= n) {
			throw new IllegalArgumentException("Index " + p + " is not between 0 and " + (n - 1));  
		}
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("rank=  " + Arrays.toString(rank));
		sb.append("\nparent=" + Arrays.toString(parent));
		sb.append("\ncount= " + count());
		sb.append("\n");
		return sb.toString();
	}

	public static void main(String[] args) {
		int n = 10;
		int[] pArr = { 4, 3, 6, 9, 2, 8, 5, 7, 6, 1, 6 };
		int[] qArr = { 3, 8, 5, 4, 1, 9, 0, 2, 1, 0, 7 };
		UnionFind uf = new UnionFind(n);
		System.out.println(uf);
		for(int i = 0; i <= n; i++){
			int p = pArr[i];
			int q = qArr[i];
			if (uf.connected(p, q)) continue;
			uf.union(p, q);
			System.out.println("union=" + p + ", " + q);
			System.out.println(uf);
		}
		System.out.println(uf.count() + " components");
	}
}
