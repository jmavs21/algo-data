package graphs.graph;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

import fundamentals.Queue;
import fundamentals.Stack;

/**
 * Find an Eulerian cycle in a graph, if one exists. Implementation uses nonrecursive DFS.
 * 
 * Extra space: O(E + V).
 * 
 * Initialization: O(E + V)
 * Operations:
 *     hasEulerianCycle, cycle: O(1)
 */
public class EulerianCycle 
{
	private Stack<Integer> cycle = null;

	private static class Edge {
		private final int v;
		private final int w;
		private boolean isUsed;
		public Edge(int v, int w) {
			this.v = v;
			this.w = w;
			isUsed = false;
		}
		public int other(int vertex) {
			if      (vertex == v) return w;
			else if (vertex == w) return v;
			else throw new IllegalArgumentException("Illegal endpoint");
		}
	}

	@SuppressWarnings("unchecked")
	public EulerianCycle(Graph graph) {
		if (graph.E() == 0) return;
		for (int v = 0; v < graph.V(); v++) 
			if (graph.degree(v) % 2 != 0)
				return;
		Queue<Edge>[] adj = (Queue<Edge>[]) new Queue[graph.V()];
		for (int v = 0; v < graph.V(); v++)
			adj[v] = new Queue<Edge>();
		for (int v = 0; v < graph.V(); v++) {
			int selfLoops = 0;
			for (int w : graph.adjacents(v)) {
				if (v == w) {
					if (selfLoops % 2 == 0) {
						Edge e = new Edge(v, w);
						adj[v].enqueue(e);
						adj[w].enqueue(e);
					}
					selfLoops++;
				}
				else if (v < w) {
					Edge e = new Edge(v, w);
					adj[v].enqueue(e);
					adj[w].enqueue(e);
				}
			}
		}
		int s = nonIsolatedVertex(graph);
		Stack<Integer> stack = new Stack<Integer>();
		stack.push(s);
		cycle = new Stack<Integer>();
		while (!stack.isEmpty()) {
			int v = stack.pop();
			while (!adj[v].isEmpty()) {
				Edge edge = adj[v].dequeue();
				if (edge.isUsed) continue;
				edge.isUsed = true;
				stack.push(v);
				v = edge.other(v);
			}
			cycle.push(v);
		}
		if (cycle.size() != graph.E() + 1)
			cycle = null;
	}

	public boolean hasEulerianCycle() {
		return cycle != null;
	}

	public Iterable<Integer> cycle() {
		return cycle;
	}

	private static int nonIsolatedVertex(Graph graph) { // returns any non-isolated vertex; -1 if no such vertex
		for (int v = 0; v < graph.V(); v++)
			if (graph.degree(v) > 0)
				return v;
		return -1;
	}

	public static void main(String[] args) throws FileNotFoundException {
		Graph graph = new Graph(new Scanner(new FileReader("resources/graph/tinyG.txt")));
		System.out.println(graph.toString());
		EulerianCycle finder = new EulerianCycle(graph);
		if (finder.hasEulerianCycle()) {
			System.out.print("Cycle: ");
			for (int v : finder.cycle()) {
				System.out.print(v + " ");
			}
			System.out.println();
		}
		else {
			System.out.println("Graph is acyclic");
		}
	}
}