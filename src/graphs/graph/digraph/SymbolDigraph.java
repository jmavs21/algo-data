package graphs.graph.digraph;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

import searching.RedBlackBST;

/**
 * SymbolGraph respresents a directed graph, where the vertex names are arbitrary strings.
 * By providing mappings between string vertex names and integers between 0 and V-1.
 * 
 * Extra space: O(E + V).
 * 
 * Initialization: O(E + V)
 * Operations:
 *     nameOf: O(1)
 *     indexOf, contains: O(V)
 */
public class SymbolDigraph 
{
	private RedBlackBST<String, Integer> st; // string -> vertex
	private String[] keys;                   // vertex -> string
	private Digraph digraph;                 

	public SymbolDigraph(String filename, String delimiter) throws FileNotFoundException {
		st = new RedBlackBST<String, Integer>();
		Scanner in = new Scanner(new FileReader(filename));
		while (in.hasNextLine()) {
			String[] a = in.nextLine().split(delimiter);
			for (int i = 0; i < a.length; i++) {
				if (!st.contains(a[i]))
					st.put(a[i], st.size());
			}
		}
		keys = new String[st.size()];
		for (String name : st.keys()) {
			keys[st.get(name)] = name;
		}
		digraph = new Digraph(st.size());
		in = new Scanner(new FileReader(filename));
		while (in.hasNextLine()) {
			String[] a = in.nextLine().split(delimiter);
			int v = st.get(a[0]);
			for (int i = 1; i < a.length; i++) {
				int w = st.get(a[i]);
				digraph.addEdge(v, w);
			}
		}
		in.close();
	}

	public boolean contains(String s) {
		return st.contains(s);
	}

	public int indexOf(String s) {
		return st.get(s);
	}

	public String nameOf(int v) {
		validateVertex(v);
		return keys[v];
	}

	public Digraph digraph() {
		return digraph;
	}

	private void validateVertex(int v) {
		int V = digraph.V();
		if (v < 0 || v >= V)
			throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V-1));
	}

	public static void main(String[] args) throws FileNotFoundException {
		String filename  = "resources/graph/routes.txt";
		String delimiter = " ";
		SymbolDigraph sg = new SymbolDigraph(filename, delimiter);
		Digraph graph = sg.digraph();
		String[] source = { "JFK", "ATL", "LAX"};
		for(int i = 0; i < source.length; i++) {
			System.out.println(source[i]);
			if (sg.contains(source[i])) {
				int s = sg.indexOf(source[i]);
				for (int v : graph.adjacents(s)) {
					System.out.println("   " + sg.nameOf(v));
				}
			}
			else {
				System.out.println("input not contain '" + source + "'");
			}
		}
	}
}
