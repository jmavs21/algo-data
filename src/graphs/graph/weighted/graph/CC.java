package graphs.graph.weighted.graph;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

import fundamentals.Queue;
import graphs.graph.weighted.graph.WeightedGraph;

/**
 * Compute connected components using DFS on an Edge Weighted Undirected Graph.
 * 
 * Extra space: O(V)
 * Initialization: O(E + V)
 * Operations:
 *     count, size, id, connected: O(1)
 */
public class CC 
{
	private boolean[] visited;
	private int[] id;   // id of CC for each vertex, id from 0 to count-1.
	private int[] size; // number of vertices in CC from vertex.
	private int count;  // number of connected components.

	public CC(WeightedGraph wGraph) {
		visited = new boolean[wGraph.V()];
		id = new int[wGraph.V()];
		size = new int[wGraph.V()];
		count = 0;
		for (int v = 0; v < wGraph.V(); v++) {
			if (!visited[v]) {
				dfs(wGraph, v);
				count++;
			}
		}
	}

	public int count() {
		return count;
	}

	public int size(int v) {
		return size[id(v)];
	}

	public int id(int v) {
		validateVertex(v);
		return id[v];
	}

	public boolean connected(int v, int w) {
		return id(v) == id(w);
	}

	private void dfs(WeightedGraph wGraph, int v) {
		visited[v] = true;
		id[v] = count;
		size[count]++;
		for (Edge e : wGraph.adjacents(v)) {
			int w = e.other(v);
			if (!visited[w]) {
				dfs(wGraph, w);
			}
		}
	}

	private void validateVertex(int v) {
		int V = visited.length;
		if (v < 0 || v >= V)
			throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V-1));
	}

	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws FileNotFoundException {
		WeightedGraph wGraph = new WeightedGraph(new Scanner(new FileReader("resources/graph/tinyEWG.txt")));
		System.out.println(wGraph.toString());
		CC cc = new CC(wGraph);
		int m = cc.count();
		System.out.println(m + " components:");
		Queue<Integer>[] components = (Queue<Integer>[]) new Queue[m];
		for (int i = 0; i < m; i++) {
			components[i] = new Queue<Integer>();
		}
		for (int v = 0; v < wGraph.V(); v++) {
			components[cc.id(v)].enqueue(v);
		}
		for (int i = 0; i < m; i++) {
			for (int v : components[i]) {
				System.out.print(v + "  ");
			}
			System.out.println();
		}
		System.out.println("\ncomponents sizes: ");
		for (int i = 0; i < m; i++) {
			System.out.print("CC " + (i+1) + ": " + components[i].size() + "\n");
		}
	}
}
