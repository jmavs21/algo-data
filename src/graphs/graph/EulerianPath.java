package graphs.graph;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

import fundamentals.Queue;
import fundamentals.Stack;

/**
 * Find an Eulerian path in a graph, if one exists. Implementation uses nonrecursive DFS.
 * An Eulerian path is a path (not necessarily simple) that uses every edge in the graph exactly once.
 * 
 * Extra space: O(E + V).
 * 
 * Initialization: O(E + V)
 * Operations:
 *     hasEulerianPath, path: O(1)
 */
public class EulerianPath 
{
	private Stack<Integer> path = null;

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
	public EulerianPath(Graph graph) {
		int oddDegreeVertices = 0;
		int s = nonIsolatedVertex(graph);
		for (int v = 0; v < graph.V(); v++) {
			if (graph.degree(v) % 2 != 0) {
				oddDegreeVertices++;
				s = v;
			}
		}
		if (oddDegreeVertices > 2) return;
		if (s == -1) s = 0;
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
		Stack<Integer> stack = new Stack<Integer>();
		stack.push(s);
		path = new Stack<Integer>();
		while (!stack.isEmpty()) {
			int v = stack.pop();
			while (!adj[v].isEmpty()) {
				Edge edge = adj[v].dequeue();
				if (edge.isUsed) continue;
				edge.isUsed = true;
				stack.push(v);
				v = edge.other(v);
			}
			path.push(v);
		}
		if (path.size() != graph.E() + 1)
			path = null;

	}

	public boolean hasEulerianPath() {
		return path != null;
	}

	public Iterable<Integer> path() {
		return path;
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
		EulerianPath euler = new EulerianPath(graph);
		System.out.print("Eulerian path: ");
		if (euler.hasEulerianPath()) {
			for (int v : euler.path()) {
				System.out.print(v + " ");
			}
			System.out.println();
		}
		else {
			System.out.println("none");
		}
	}
}