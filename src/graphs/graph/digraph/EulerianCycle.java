package graphs.graph.digraph;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Iterator;
import java.util.Scanner;

import fundamentals.Stack;

/**
 * Find an Eulerian cycle in a digraph, if one exists. Implementation uses nonrecursive DFS.
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

	@SuppressWarnings("unchecked")
	public EulerianCycle(Digraph digraph) {
		if (digraph.E() == 0) return;
		for (int v = 0; v < digraph.V(); v++) 
			if (digraph.outdegree(v) != digraph.indegree(v))
				return;
		// create local view of adjacency lists, to iterate one vertex at a time
		Iterator<Integer>[] adj = (Iterator<Integer>[]) new Iterator[digraph.V()];
		for (int v = 0; v < digraph.V(); v++)
			adj[v] = digraph.adjacents(v).iterator();
		// initialize stack with any non-isolated vertex
		int s = nonIsolatedVertex(digraph);
		Stack<Integer> stack = new Stack<Integer>();
		stack.push(s);
		cycle = new Stack<Integer>();
		while (!stack.isEmpty()) {
			int v = stack.pop();
			while (adj[v].hasNext()) {
				stack.push(v);
				v = adj[v].next();
			}
			// add vertex with no more leaving edges to cycle
			cycle.push(v);
		}
		// check if all edges have been used
		// (in case there are two or more vertex-disjoint Eulerian cycles)
		if (cycle.size() != digraph.E() + 1)
			cycle = null;
	}

	public boolean hasEulerianCycle() {
		return cycle != null;
	}

	public Iterable<Integer> cycle() {
		return cycle;
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
		EulerianCycle euler = new EulerianCycle(digraph);
		System.out.print("Eulerian cycle: ");
		if (euler.hasEulerianCycle()) {
			for (int v : euler.cycle()) {
				System.out.print(v + " ");
			}
			System.out.println();
		}
		else {
			System.out.println("none");
		}
	}
}