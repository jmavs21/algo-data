package graphs.graph;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Iterator;
import java.util.Scanner;

import fundamentals.Stack;

/**
 * Depth First Search in Undirected Graph for finding paths from a source vertex
 * 's' to every other vertex.
 * 
 * Extra space: O(V)
 * 
 * Initialization: O(V + E) where V is number of vertices and E number of edges.
 * Operations: hasPathTo: O(1) pathTo: O(length of path)
 */
public class DFSPaths {
	private boolean[] visited;
	private int[] edgeTo;
	private int s;

	public DFSPaths(Graph graph, int s) {
		this.s = s;
		visited = new boolean[graph.V()];
		edgeTo = new int[graph.V()];
		validateVertex(s);
		dfs(graph, s);
	}

	public DFSPaths(Graph graph, int s, boolean nonrecursive) {
		if (nonrecursive) {
			this.s = s;
			visited = new boolean[graph.V()];
			edgeTo = new int[graph.V()];
			validateVertex(s);
			nonrecursiveDFS(graph, s);
		} else {
			System.out.println("Call only with nonrecursive to true.");
		}
	}

	public boolean hasPathTo(int v) {
		validateVertex(v);
		return visited[v];
	}

	public Iterable<Integer> pathTo(int v) {
		validateVertex(v);
		if (!hasPathTo(v)) return null;
		Stack<Integer> path = new Stack<Integer>();
		for (int x = v; x != s; x = edgeTo[x]) {
			path.push(x);
		}
		path.push(s);
		return path;
	}

	private void dfs(Graph graph, int v) {
		visited[v] = true;
		for (int w : graph.adjacents(v)) {
			if (!visited[w]) {
				edgeTo[w] = v;
				dfs(graph, w);
			}
		}
	}

	private void validateVertex(int v) {
		int V = visited.length;
		if (v < 0 || v >= V)
			throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V - 1));
	}

	@SuppressWarnings("unchecked")
	private void nonrecursiveDFS(Graph graph, int s) {
		Iterator<Integer>[] adj = (Iterator<Integer>[]) new Iterator[graph.V()];
		for (int v = 0; v < graph.V(); v++) {
			adj[v] = graph.adjacents(v).iterator();
		}
		Stack<Integer> stack = new Stack<Integer>();
		visited[s] = true;
		stack.push(s);
		while (!stack.isEmpty()) {
			int v = stack.peek();
			if (adj[v].hasNext()) {
				int w = adj[v].next();
				if (!visited[w]) {
					visited[w] = true;
					edgeTo[w] = v;
					stack.push(w);
				}
			} else {
				stack.pop();
			}
		}
	}

	public static void main(String[] args) throws FileNotFoundException {
		Graph graph = new Graph(new Scanner(new FileReader("resources/graph/tinyCG.txt")));
		System.out.println(graph.toString());
		int s = 0;

		System.out.println("----- Recursive DFS: -----");
		DFSPaths dfs = new DFSPaths(graph, s);
		for (int v = 0; v < graph.V(); v++) {
			if (dfs.hasPathTo(v)) {
				System.out.printf("%d to %d:  ", s, v);
				for (int x : dfs.pathTo(v)) {
					if (x == s)
						System.out.print(x);
					else
						System.out.print("-" + x);
				}
				System.out.println();
			} else {
				System.out.printf("%d to %d:  not connected%n", s, v);
			}

		}
		System.out.println();

		System.out.println("----- Non-recursive DFS -----");
		dfs = new DFSPaths(graph, s, true);
		for (int v = 0; v < graph.V(); v++) {
			if (dfs.hasPathTo(v)) {
				System.out.printf("%d to %d:  ", s, v);
				for (int x : dfs.pathTo(v)) {
					if (x == s)
						System.out.print(x);
					else
						System.out.print("-" + x);
				}
				System.out.println();
			} else {
				System.out.printf("%d to %d:  not connected%n", s, v);
			}
		}
	}
}