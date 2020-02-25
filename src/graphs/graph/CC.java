package graphs.graph;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

import fundamentals.Queue;

/**
 * Compute connected components using DFS on Undirected and Edge Weighted Undirected graphs.
 * 
 * Extra space: O(V)
 * 
 * Initialization: O(E + V)
 * Operations:
 *     count, size, id, connected: O(1)
 */
public class CC 
{
	private boolean[] visited;
	private int[] id;   // id of CC for each vertex, id from 0 to count-1.
	private int[] size; // number of vertices in CC from vertex.
	private int count;  // number of connected components, and also use as ID.

	public CC(Graph graph) {
		visited = new boolean[graph.V()];
		id = new int[graph.V()];
		size = new int[graph.V()];
		count = 0;
		for(int v = 0; v < graph.V(); v++) {
			if(!visited[v]) {
				dfs(graph, v);
				count++;
			}
		}
	}

	public int count() {
		return count;
	}

	public int id(int v) {
		validateVertex(v);
		return id[v];
	}

	public int size(int v) {
		return size[id(v)];
	}

	public boolean connected(int v, int w) {
		return id(v) == id(w);
	}

	private void dfs(Graph graph, int v) {
		visited[v] = true;
		id[v] = count;
		size[count]++;
		for(int w : graph.adjacents(v)) {
			if(!visited[w]) {
				dfs(graph, w);
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
		Graph graph = new Graph(new Scanner(new FileReader("resources/graph/tinyG.txt")));
		System.out.println(graph.toString());
		CC cc = new CC(graph);
		int m = cc.count();
		System.out.println(m + " components:");
		Queue<Integer>[] components = (Queue<Integer>[]) new Queue[m];
		for (int i = 0; i < m; i++) {
			components[i] = new Queue<Integer>();
		}
		for (int v = 0; v < graph.V(); v++) {
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
