package graphs.graph;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

import fundamentals.Stack;
import util.RandomN;

/**
 * FlowNetwork, implemented with adjacency-lists (vertex-index array) with vertices named 0 to V-1;
 * Parallel edges and self-loops allowed.
 * 
 * Initialization: O(V) where V is the number of vertices.
 * Operations:
 *     all methods: O(1) except when client iterates over vertices adjacent where is O(V).
 */
public class FlowNetwork 
{
	private final int V;
	private int E;
	private Stack<FlowEdge>[] adj;

	@SuppressWarnings("unchecked")
	public FlowNetwork(int V) {
		if (V < 0) throw new IllegalArgumentException("Number of vertices in a Graph must be nonnegative");
		this.V = V;
		this.E = 0;
		adj = (Stack<FlowEdge>[]) new Stack[V];
		for(int i = 0; i < V; i++) {
			adj[i] = new Stack<FlowEdge>();
		}
	}

	public FlowNetwork(int V, int E) {
		this(V);
		if (E < 0) throw new IllegalArgumentException("Number of edges must be nonnegative");
		for (int i = 0; i < E; i++) {
			int v = RandomN.getRandomInt(V);
			int w = RandomN.getRandomInt(V);
			double capacity = RandomN.getRandomInt(100);
			addEdge(new FlowEdge(v, w, capacity));
		}
	}

	public FlowNetwork(Scanner in) {
		this(in.nextInt());
		int E = in.nextInt();
		if (E < 0) throw new IllegalArgumentException("number of edges must be nonnegative");
		for (int i = 0; i < E; i++) {
			int v = in.nextInt();
			int w = in.nextInt();
			validateVertex(v);
			validateVertex(w);
			double capacity = in.nextDouble();
			addEdge(new FlowEdge(v, w, capacity));
		}
	}

	public int V() {
		return V;
	}

	public int E() {
		return E;
	}

	public void addEdge(FlowEdge edge) {
		int v = edge.from();
		int w = edge.to();
		validateVertex(v);
		validateVertex(w);
		adj[v].push(edge);
		adj[w].push(edge);
		E++;
	}

	public Iterable<FlowEdge> adjacents(int v) {
		validateVertex(v);
		return adj[v];
	}

	public Iterable<FlowEdge> edges(){
		Stack<FlowEdge> stack = new Stack<FlowEdge>();
		for(int v = 0; v < V; v++) {
			for(FlowEdge e : adjacents(v)) {
				if(e.to() != v) 
					stack.push(e);
			}
		}
		return stack;
	}

	public String toString() {
		StringBuilder s = new StringBuilder();
		s.append(V + " " + E + "\n");
		for (int v = 0; v < V; v++) {
			s.append(v + ":  ");
			for (FlowEdge e : adj[v]) {
				if (e.to() != v) s.append(e + "  ");
			}
			s.append("\n");
		}
		return s.toString();
	}

	private void validateVertex(int v) {
		if (v < 0 || v >= V) throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V-1));
	}

	public static void main(String[] args) throws FileNotFoundException {
		FlowNetwork flowNetwrok = new FlowNetwork(new Scanner(new FileReader("resources/graph/tinyFN.txt")));
		System.out.println(flowNetwrok);
	}
}