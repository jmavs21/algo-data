package graphs.graph.weighted.graph;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

import fundamentals.Stack;
import util.RandomN;

/**
 * Weighted Undirected Graph, implemented with adjacency-lists (vertex-index array) with vertices named 0 to V-1;
 * Parallel edges and self-loops allowed.
 * 
 * Initialization: O(V) where V is the number of vertices.
 * Operations:
 *     all methods: O(1) except when client iterates over vertices adjacent where is O(V).
 */
public class WeightedGraph 
{
	private int V;
	private int E;
	private Stack<Edge>[] adj;

	@SuppressWarnings("unchecked")
	public WeightedGraph(int V) {
		if (V < 0) throw new IllegalArgumentException("Number of vertices must be nonnegative");
		this.V = V;
		this.E = 0;
		adj = (Stack<Edge>[]) new Stack[V];
		for(int i = 0; i < V; i++) {
			adj[i] = new Stack<Edge>();
		}
	}

	public WeightedGraph(WeightedGraph wGraph) {
		this(wGraph.V());
		this.E = wGraph.E();
		for (int v = 0; v < wGraph.V(); v++) {
			Stack<Edge> reverse = new Stack<Edge>();
			for (Edge e : wGraph.adj[v]) {
				reverse.push(e);
			}
			for (Edge e : reverse) {
				adj[v].push(e);
			}
		}
	}

	public WeightedGraph(int V, int E) {
		this(V);
		if (E < 0) throw new IllegalArgumentException("Number of edges must be nonnegative");
		for (int i = 0; i < E; i++) {
			int v = RandomN.getRandomInt(V);
			int w = RandomN.getRandomInt(V);
			double weight = Math.round(100 * RandomN.getRandomDouble()) / 100.0;
			Edge e = new Edge(v, w, weight);
			addEdge(e);
		}
	}

	public WeightedGraph(Scanner in) {
		this(in.nextInt());
		int E = in.nextInt();
		if (E < 0) throw new IllegalArgumentException("Number of edges must be nonnegative");
		for (int i = 0; i < E; i++) {
			int v = in.nextInt();
			int w = in.nextInt();
			validateVertex(v);
			validateVertex(w);
			double weight = in.nextDouble();
			Edge e = new Edge(v, w, weight);
			addEdge(e);
		}
	}

	public int V() {
		return V;
	}

	public int E() {
		return E;
	}

	public void addEdge(Edge edge) {
		int v = edge.either();
		int w = edge.other(v);
		validateVertex(v);
		validateVertex(w);
		adj[v].push(edge);
		adj[w].push(edge);
		E++;
	}

	public Iterable<Edge> adjacents(int v) {
		validateVertex(v);
		return adj[v];
	}

	public int degree(int v) {
		validateVertex(v);
		return adj[v].size();
	}

	public Iterable<Edge> edges() {
		Stack<Edge> stack = new Stack<Edge>();
		for(int v = 0; v < V; v++) {
			int selfLoops = 0;
			for(Edge e : adj[v]) {
				if(e.other(v) > v) {					
					stack.push(e);
				} else if (e.other(v) == v) {
					if (selfLoops % 2 == 0) 
						stack.push(e);
					selfLoops++;
				}
			}
		}
		return stack;
	}

	public String toString() {
		StringBuilder s = new StringBuilder();
		s.append(V + " " + E + "\n");
		for (int v = 0; v < V; v++) {
			s.append(v + ": ");
			for (Edge e : adj[v]) {
				s.append(e + "  ");
			}
			s.append("\n");
		}
		return s.toString();
	}

	private void validateVertex(int v) {
		if (v < 0 || v >= V)
			throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V-1));
	}

	public static void main(String[] args) throws FileNotFoundException {
		WeightedGraph G = new WeightedGraph(new Scanner(new FileReader("resources/graph/tinyEWG.txt")));
		System.out.println(G);
	}
}