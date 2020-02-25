package graphs.graph;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.NoSuchElementException;
import java.util.Scanner;

import fundamentals.Stack;

/**
 * Undirected graph, implemented with adjacency-lists (vertex-index array) with vertices named 0 to V-1;
 * Parallel edges and self-loops allowed.
 * 
 * Initialization: O(V) where V is the number of vertices.
 * Operations:
 *     all methods: O(1) except when client iterates over vertices adjacent where is O(V).
 */
public class Graph 
{	
	private final int V;
	private int E;
	private Stack<Integer>[] adj;

	@SuppressWarnings("unchecked")
	public Graph(int V) {
		if (V < 0) throw new IllegalArgumentException("Number of vertices must be nonnegative.");
		this.V = V;
		this.E = 0;
		adj = (Stack<Integer>[]) new Stack[V];
		for(int v = 0; v < V; v++) {
			adj[v] = new Stack<Integer>();
		}
	}

	public Graph(Graph graph) {
		this(graph.V());
		this.E = graph.E();
		for (int v = 0; v < graph.V(); v++) {
			Stack<Integer> reverse = new Stack<Integer>();
			for (int w : graph.adj[v]) {
				reverse.push(w);
			}
			for (int w : reverse) {
				this.adj[v].push(w);
			}
		}
	}

	@SuppressWarnings("unchecked")
	public Graph(Scanner in) {
		try {
			this.V = in.nextInt();
			if (V < 0) throw new IllegalArgumentException("number of vertices in a Graph must be nonnegative");
			adj = (Stack<Integer>[]) new Stack[V];
			for (int v = 0; v < V; v++) {
				adj[v] = new Stack<Integer>();
			}
			int E = in.nextInt();
			if (E < 0) throw new IllegalArgumentException("number of edges in a Graph must be nonnegative");
			for (int i = 0; i < E; i++) {
				int v = in.nextInt();
				int w = in.nextInt();
				validateVertex(v);
				validateVertex(w);
				addEdge(v, w); 
			}
		}
		catch (NoSuchElementException e) {
			throw new IllegalArgumentException("invalid input format in Graph constructor", e);
		}
	}

	public int V() {
		return V;
	}

	public int E() {
		return E;
	}

	public void addEdge(int v, int w) {
		validateVertex(v);
		validateVertex(w);
		E++;
		adj[v].push(w);
		adj[w].push(v);
	}

	public Iterable<Integer> adjacents(int v) {
		validateVertex(v);
		return adj[v];
	}

	public int degree(int v) {
		validateVertex(v);
		return adj[v].size();
	}

	private void validateVertex(int v) {
		if (v < 0 || v >= V)
			throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V-1));
	}

	public String toString() {
		StringBuilder s = new StringBuilder();
		s.append(V + " vertices, " + E + " edges \n");
		for (int v = 0; v < V; v++) {
			s.append(v + ": ");
			for (int w : adj[v]) {
				s.append(w + " ");
			}
			s.append("\n");
		}
		return s.toString();
	}

	public static void main(String[] args) throws FileNotFoundException {
		Graph graph = new Graph(new Scanner(new FileReader("resources/graph/tinyG.txt")));
		System.out.println(graph);
	}
}