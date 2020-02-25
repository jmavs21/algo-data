package graphs.graph.weighted.digraph;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

import fundamentals.Queue;
import fundamentals.Stack;
import util.RandomN;

/**
 * Weighted Digraph, implemented with adjacency-lists (vertex-index array) with vertices named 0 to V-1;
 * Parallel edges and self-loops allowed.
 * 
 * Initialization: O(V) where V is the number of vertices.
 * Operations:
 *     all methods: O(1) except when client iterates over vertices adjacent where is O(V).
 */
public class WeightedDigraph 
{
	private final int V;
	private int E;
	private Stack<EdgeDirect>[] adj;
	private int[] indegree;

	@SuppressWarnings("unchecked")
	public WeightedDigraph(int V) {
		if (V < 0) throw new IllegalArgumentException("Number of vertices in a Digraph must be nonnegative");
		this.V = V;
		E = 0;
		indegree = new int[V];
		adj = (Stack<EdgeDirect>[] ) new Stack[V];
		for(int i = 0; i < V; i++) {
			adj[i] = new Stack<EdgeDirect>();
		}
	}

	public WeightedDigraph(int V, int E) {
		this(V);
		if (E < 0) throw new IllegalArgumentException("Number of edges in a Digraph must be nonnegative");
		for (int i = 0; i < E; i++) {
			int v = RandomN.getRandomInt(V);
			int w = RandomN.getRandomInt(V);
			double weight = 0.01 * RandomN.getRandomInt(100);
			EdgeDirect e = new EdgeDirect(v, w, weight);
			addEdge(e);
		}
	}

	public WeightedDigraph(WeightedDigraph wDigraph) {
		this(wDigraph.V());
		this.E = wDigraph.E();
		for (int v = 0; v < wDigraph.V(); v++)
			this.indegree[v] = wDigraph.indegree(v);
		for (int v = 0; v < wDigraph.V(); v++) {
			Stack<EdgeDirect> reverse = new Stack<EdgeDirect>();
			for (EdgeDirect e : wDigraph.adj[v]) {
				reverse.push(e);
			}
			for (EdgeDirect e : reverse) {
				adj[v].push(e);
			}
		}
	}

	public WeightedDigraph(Scanner in) {
		this(in.nextInt());
		int E = in.nextInt();
		if (E < 0) throw new IllegalArgumentException("Number of edges must be nonnegative");
		for (int i = 0; i < E; i++) {
			int v = in.nextInt();
			int w = in.nextInt();
			validateVertex(v);
			validateVertex(w);
			double weight = in.nextDouble();
			addEdge(new EdgeDirect(v, w, weight));
		}
	}

	public int V() {
		return V;
	}

	public int E() {
		return E;
	}

	public void addEdge(EdgeDirect e) {
		int v = e.from();
		int w = e.to();
		validateVertex(v);
		validateVertex(w);
		adj[v].push(e);
		indegree[w]++;
		E++;
	}

	public Iterable<EdgeDirect> adjacents(int v) {
		validateVertex(v);
		return adj[v];
	}

	public int outdegree(int v) {
		validateVertex(v);
		return adj[v].size();
	}

	public int indegree(int v) {
		validateVertex(v);
		return indegree[v];
	}

	public Iterable<EdgeDirect> edges(){
		Queue<EdgeDirect> queue = new Queue<EdgeDirect>();
		for(int v = 0; v < V; v++) {
			for(EdgeDirect e : adj[v]) {
				queue.enqueue(e);
			}
		}
		return queue;
	}

	public WeightedDigraph reverse() {
		WeightedDigraph reverse = new WeightedDigraph(V);
		for(int v = 0; v < V; v++) {
			for(EdgeDirect e : adjacents(v)) {
				reverse.addEdge(new EdgeDirect(e.to(), e.from(), e.weight()));
			}
		}
		return reverse;
	}

	public String toString() {
		StringBuilder s = new StringBuilder();
		s.append(V + " " + E + "\n");
		for (int v = 0; v < V; v++) {
			s.append(v + ": ");
			for (EdgeDirect e : adj[v]) {
				s.append(e + "  ");
			}
			s.append("\n");
		}
		return s.toString();
	}

	private void validateVertex(int v) {
		if (v < 0 || v >= V) throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V-1));
	}

	public static void main(String[] args) throws FileNotFoundException {
		WeightedDigraph wDigraph = new WeightedDigraph(new Scanner(new FileReader("resources/graph/tinyEWD.txt")));
		System.out.println(wDigraph);

		System.out.println("reverse:");
		wDigraph = wDigraph.reverse();
		System.out.println(wDigraph);
	}
}