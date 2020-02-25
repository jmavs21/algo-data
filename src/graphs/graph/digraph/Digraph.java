package graphs.graph.digraph;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.NoSuchElementException;
import java.util.Scanner;

import fundamentals.Stack;

/**
 * Directed graph, implemented with adjacency-lists (vertex-index array) with vertices named 0 to V-1;
 * Parallel edges and self-loops allowed.
 * 
 * Initialization: O(V) where V is the number of vertices.
 * Operations:
 *     all methods: O(1) except when client iterates over vertices adjacent where is O(V).
 */
public class Digraph 
{
	private final int V;
	private int E = 0;
	private Stack<Integer>[] adj;
	private int[] indegree;

	@SuppressWarnings("unchecked")
	public Digraph(int V) {
		if (V < 0) throw new IllegalArgumentException("Number of vertices in a Digraph must be nonnegative");
		this.V = V;
		this.E = 0;
		indegree = new int[V];
		adj = (Stack<Integer>[]) new Stack[V];
		for(int i = 0; i < V; i++) {
			adj[i] = new Stack<Integer>();
		}
	}

	public Digraph(Digraph digraph) {
		this(digraph.V());
		this.E = digraph.E();
		for (int v = 0; v < V; v++)
			this.indegree[v] = digraph.indegree(v);
		for (int v = 0; v < digraph.V(); v++) {
			Stack<Integer> reverse = new Stack<Integer>();
			for (int w : digraph.adj[v]) {
				reverse.push(w);
			}
			for (int w : reverse) {
				adj[v].push(w);
			}
		}
	}

	@SuppressWarnings("unchecked")
	public Digraph(Scanner in) {
		try {
			V = in.nextInt();
			if (V < 0) throw new IllegalArgumentException("number of vertices in a Digraph must be nonnegative");
			indegree = new int[V];
			adj = (Stack<Integer>[]) new Stack[V];
			for (int v = 0; v < V; v++) {
				adj[v] = new Stack<Integer>();
			}
			int E = in.nextInt();
			if (E < 0) throw new IllegalArgumentException("number of edges in a Digraph must be nonnegative");
			for (int i = 0; i < E; i++) {
				int v = in.nextInt();
				int w = in.nextInt();
				addEdge(v, w);
			}
		}
		catch (NoSuchElementException e) {
			throw new IllegalArgumentException("invalid input format in Digraph constructor", e);
		}
	}

	public int V() {
		return V;
	}

	public int E() {
		return E;
	}

	public int outdegree(int v) {
		validateVertex(v);
		return adj[v].size();
	}

	public int indegree(int v) {
		validateVertex(v);
		return indegree[v];
	}

	public void addEdge(int v, int w) {
		validateVertex(v);
		validateVertex(w);
		adj[v].push(w);
		indegree[w]++;
		E++;
	}

	public Iterable<Integer> adjacents(int v) {
		validateVertex(v);
		return adj[v];
	}

	public Digraph reverse() {
		Digraph reverse = new Digraph(V);
		for(int v = 0; v < V; v++) {
			for(int w : adjacents(v)) {
				reverse.addEdge(w, v);
			}
		}
		return reverse;
	}

	private void validateVertex(int v) {
		if (v < 0 || v >= V)
			throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V-1));
	}

	public String toString() {
		StringBuilder s = new StringBuilder();
		s.append(V + " vertices, " + E + " edges \n");
		for (int v = 0; v < V; v++) {
			s.append(String.format("%d: ", v));
			for (int w : adj[v]) {
				s.append(String.format("%d ", w));
			}
			s.append("\n");
		}
		return s.toString();
	}

	public static void main(String[] args) throws FileNotFoundException {
		Digraph digraph = new Digraph(new Scanner(new FileReader("resources/graph/tinyDG.txt")));
		System.out.println(digraph);
	}
}