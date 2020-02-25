package graphs.graph.digraph;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Iterator;
import java.util.Scanner;

import fundamentals.Stack;

/**
 * Find an Eulerian path in a digraph, if one exists. Implementation uses nonrecursive DFS.
 * An Eulerian path is a path (not necessarily simple) that uses every edge in the digraph exactly once.
 * 
 * Extra space: O(V)
 * 
 * Initialization: O(E + V)
 * Operations:
 *     hasEulerianPath, path: O(1)
 */
public class EulerianPath 
{
	private Stack<Integer> path = null;

	@SuppressWarnings("unchecked")
	public EulerianPath(Digraph digraph) {
		// find vertex from which to start potential Eulerian path:
		// a vertex v with outdegree(v) > indegree(v) if it exits;
		// otherwise a vertex with outdegree(v) > 0
		int deficit = 0;
		int s = nonIsolatedVertex(digraph);
		for (int v = 0; v < digraph.V(); v++) {
			if (digraph.outdegree(v) > digraph.indegree(v)) {
				deficit += (digraph.outdegree(v) - digraph.indegree(v));
				s = v;
			}
		}
		// digraph can't have an Eulerian path
		// (this condition is needed)
		if (deficit > 1) return;
		// special case for digraph with zero edges (has a degenerate Eulerian path)
		if (s == -1) s = 0;
		// create local view of adjacency lists, to iterate one vertex at a time
		Iterator<Integer>[] adj = (Iterator<Integer>[]) new Iterator[digraph.V()];
		for (int v = 0; v < digraph.V(); v++)
			adj[v] = digraph.adjacents(v).iterator();
		// greedily add to cycle, depth-first search style
		Stack<Integer> stack = new Stack<Integer>();
		stack.push(s);
		path = new Stack<Integer>();
		while (!stack.isEmpty()) {
			int v = stack.pop();
			while (adj[v].hasNext()) {
				stack.push(v);
				v = adj[v].next();
			}
			// push vertex with no more available edges to path
			path.push(v);
		}
		// check if all edges have been used
		if (path.size() != digraph.E() + 1)
			path = null;

	}

	public boolean hasEulerianPath() {
		return path != null;
	}

	public Iterable<Integer> path() {
		return path;
	}

	private static int nonIsolatedVertex(Digraph digraph) { // returns any non-isolated vertex; -1 if no such vertex
		for (int v = 0; v < digraph.V(); v++)
			if (digraph.outdegree(v) > 0)
				return v;
		return -1;
	}

	public static void main(String[] args) throws FileNotFoundException {
		Digraph digraph = new Digraph(new Scanner(new FileReader("resources/graph/tinyEWD.txt")));
		System.out.println(digraph);
		EulerianPath euler = new EulerianPath(digraph);
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