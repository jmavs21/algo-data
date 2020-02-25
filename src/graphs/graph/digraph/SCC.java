package graphs.graph.digraph;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

import fundamentals.Queue;

/**
 * Compute the Strong Connected Component using Kosaraju-Sharir algorithm.
 * 
 * Extra space: O(V)
 * 
 * Initialization: O(E + V) in the worst case.
 * Operations:
 *     id, count, areStronglyConnected: O(1)
 *     
 * NOTE: Steps of algorithm:
 *     1. Get topological order from the Reverse Digraph.
 *     2. Do DFS's on topolocial order in Original Digraph to count SCC.
 *       0      1       2      3      4 components
 *     (.:)<--(:.:)<--(:.)<--(::.)
 */
public class SCC 
{
	private boolean[] visited;
	private int[] id;
	private int count;

	public SCC(Digraph digraph) {
		visited = new boolean[digraph.V()];
		id = new int[digraph.V()];
		DepthFirstOrder dfo = new DepthFirstOrder(digraph.reverse());
		System.out.println("Reverse G topological: " + dfo.reversePostorder() + "\n");
		for(int v : dfo.reversePostorder()) {
			if(!visited[v]) {
				dfs(digraph, v);
				count++;
			}
		}
	}

	private void dfs(Digraph digraph, int v) {
		visited[v] = true;
		id[v] = count;
		for(int w : digraph.adjacents(v)) {
			if(!visited[w]) {
				dfs(digraph, w);
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

	public boolean stronglyConnected(int v, int w) {
		return id(v) == id(w);
	}

	private void validateVertex(int v) {
		int V = id.length;
		if(v < 0 || v >= V) throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V-1));
	}

	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws FileNotFoundException {
		Digraph digraph = new Digraph(new Scanner(new FileReader("resources/graph/tinyDG.txt")));
		System.out.println(digraph);
		SCC scc = new SCC(digraph);
		int m = scc.count();
		System.out.println(m + " strong components");
		Queue<Integer>[] components = (Queue<Integer>[]) new Queue[m];
		for (int i = 0; i < m; i++) {
			components[i] = new Queue<Integer>();
		}
		for (int v = 0; v < digraph.V(); v++) {
			components[scc.id(v)].enqueue(v);
		}
		for (int i = 0; i < m; i++) {
			System.out.print("SCC " + (i+1) + ": ");
			for (int v : components[i]) {
				System.out.print(v + " ");
			}
			System.out.println();
		}
	}
}