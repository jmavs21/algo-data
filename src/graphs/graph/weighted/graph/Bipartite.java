package graphs.graph.weighted.graph;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

import fundamentals.Stack;

/**
 * Checks if an Weighted Graph is Bipartite or it has an odd-length cycle, using DFS.
 *  The isBipartite operation determines whether the graph is bipartite. 
 *  If so, the color operation determines a bipartition; 
 *  if not, the oddCycle operation determines a cycle with an odd number of edges.
 * 
 * Extra space: O(V)
 * 
 * Initialization: O(E + V) in the worst case.
 * Operations:
 *     isBipartite, color: O(1)
 *     oddCycle: O(length of cycle)
 */
public class Bipartite 
{
	private boolean isBipartite;
	private boolean[] visited;
	private boolean[] color;       // false=black and true=white
	private int[] edgeTo;
	private Stack<Integer> cycle;  // odd-length cycle

	public Bipartite(WeightedGraph graph) {
		isBipartite = true;
		visited = new boolean[graph.V()];
		color = new boolean[graph.V()];
		edgeTo = new int[graph.V()];
		for(int v = 0; v < graph.V(); v++) {
			if(!visited[v]) {
				dfs(graph, v);
			}
		}
	}

	public boolean isBipartite() {
		return isBipartite;
	}

	public boolean color(int v) {
		validateVertex(v);
		if (!isBipartite) throw new UnsupportedOperationException("graph is not bipartite");
		return color[v];
	}

	public Iterable<Integer> oddCycle() {
		return cycle; 
	}

	private void dfs(WeightedGraph graph, int v) {
		visited[v] = true;
		for(Edge e : graph.adjacents(v)) {
			int w = e.other(v);
			if(cycle != null) return;
			if(!visited[w]) {
				edgeTo[w] = v;
				color[w] = !color[v];
				dfs(graph, w);
			} else if(color[w] == color[v]) {
				isBipartite = false;
				cycle = new Stack<Integer>();
				cycle.push(w);
				for (int x = v; x != w; x = edgeTo[x]) {
					cycle.push(x);
				}
				cycle.push(w);
			}
		}
	}

	private void validateVertex(int v) {
		int V = visited.length;
		if (v < 0 || v >= V)
			throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V-1));
	}

	public static void main(String[] args) throws FileNotFoundException {
		WeightedGraph wGraph = new WeightedGraph(new Scanner(new FileReader("resources/graph/tinyEWD.txt")));
		System.out.println(wGraph);

		Bipartite b = new Bipartite(wGraph);
		if (b.isBipartite()) {
			System.out.println("Graph is bipartite.");
			for (int v = 0; v < wGraph.V(); v++) {
				System.out.println(v + ": " + b.color(v));
			}
		}
		else {
			System.out.print("Graph has an odd-length cycle: ");
			for (int x : b.oddCycle()) {
				System.out.print(x + " ");
			}
			System.out.println();
		}
	}
}