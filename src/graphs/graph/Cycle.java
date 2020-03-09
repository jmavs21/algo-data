package graphs.graph;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

import fundamentals.Stack;

/**
 * Finds a simple cycle in an Undirected Graph, using DFS.
 * 
 * Extra space: O(V)
 * 
 * Initialization: O(E + V) in the worst case.
 * Operations:
 *     hasCycle: O(1)
 *     cycle: O(length of cycle)
 *     
 * Note: for every v, if adjacent w is already visited and w is not parent (u) -> v, then is a cycle in graph.
 * If      w == u --> means w came from his parent u -> v, so v -> w are connected so is ok.
 * else if w != u --> means w came from different parent u -> z, so cycle v -> w -> z -> v 
 */
public class Cycle 
{
	private boolean[] visited;
	private int[] edgeTo;
	private Stack<Integer> cycle;

	public Cycle(Graph graph) {
		if (hasSelfLoop(graph)) return;
		if (hasParallelEdges(graph)) return;
		visited = new boolean[graph.V()];
		edgeTo = new int[graph.V()];
		for(int v = 0; v < graph.V(); v++) {
			if(!visited[v]) {
				dfs(graph, v, -1);				
			}
		}
	}

	public boolean hasCycle() {
		return cycle != null;
	}

	public Iterable<Integer> cycle() {
		return cycle;
	}

	private void dfs(Graph graph, int v, int u) {
		visited[v] = true;
		for(int w : graph.adjacents(v)) {
			if(hasCycle()) return;
			if(!visited[w]) {
				edgeTo[w] = v;
				dfs(graph, w, v);
			} else if(w != u) {
				cycle = new Stack<Integer>();
				for(int x = v; x != w; x = edgeTo[x]) {
					cycle.push(x);
				}
				cycle.push(w);
				cycle.push(v);
			}
		}
	}

	private boolean hasSelfLoop(Graph graph) {
		for (int v = 0; v < graph.V(); v++) {
			for (int w : graph.adjacents(v)) {
				if (v == w) {
					cycle = new Stack<Integer>();
					cycle.push(v);
					cycle.push(v);
					return true;
				}
			}
		}
		return false;
	}

	private boolean hasParallelEdges(Graph graph) {
		visited = new boolean[graph.V()];
		for (int v = 0; v < graph.V(); v++) {
			for (int w : graph.adjacents(v)) {
				if (visited[w]) {
					cycle = new Stack<Integer>();
					cycle.push(v);
					cycle.push(w);
					cycle.push(v);
					return true;
				}
				visited[w] = true;
			}
			for (int w : graph.adjacents(v)) {
				visited[w] = false;
			}
		}
		return false;
	}

	public static void main(String[] args) throws FileNotFoundException {
		Graph graph = new Graph(new Scanner(new FileReader("resources/graph/tinyCG.txt")));
		System.out.println(graph.toString());
		Cycle finder = new Cycle(graph);
		if (finder.hasCycle()) {
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