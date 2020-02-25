package graphs.graph.weighted.graph;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

import fundamentals.Stack;

/**
 * Finds a simple cycle in an Undirected Graph, using DFS.
 * 
 * Extra space: O(V)
 * Initialization: O(E + V) in the worst case.
 * Operations:
 *     hasCycle: O(1)
 *     cycle: O(length of cycle)
 */
public class Cycle 
{
	private boolean[] visited;
	private int[] edgeTo;
	private Stack<Integer> cycle;

	public Cycle(WeightedGraph wGraph) {
		if (hasSelfLoop(wGraph)) return;
		if (hasParallelEdges(wGraph)) return;
		visited = new boolean[wGraph.V()];
		edgeTo = new int[wGraph.V()];
		for(int v = 0; v < wGraph.V(); v++) {
			if(!visited[v]) {
				dfs(wGraph, v, -1);				
			}
		}
	}

	public boolean hasCycle() {
		return cycle != null;
	}

	public Iterable<Integer> cycle() {
		return cycle;
	}

	private void dfs(WeightedGraph wGraph, int v, int u) {
		visited[v] = true;
		for(Edge e : wGraph.adjacents(v)) {
			if(hasCycle()) return;
			int w = e.other(v);
			if(!visited[w]) {
				edgeTo[w] = v;
				dfs(wGraph, w, v);
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

	private boolean hasSelfLoop(WeightedGraph wGraph) {
		for (int v = 0; v < wGraph.V(); v++) {
			for (Edge e : wGraph.adjacents(v)) {
				int w = e.other(v);
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

	private boolean hasParallelEdges(WeightedGraph wGraph) {
		visited = new boolean[wGraph.V()];
		for (int v = 0; v < wGraph.V(); v++) {
			for (Edge e: wGraph.adjacents(v)) {
				int w = e.other(v);
				if (visited[w]) {
					cycle = new Stack<Integer>();
					cycle.push(v);
					cycle.push(w);
					cycle.push(v);
					return true;
				}
				visited[w] = true;
			}
			for (Edge e : wGraph.adjacents(v)) {
				int w = e.other(v);
				visited[w] = false;
			}
		}
		return false;
	}

	public static void main(String[] args) throws FileNotFoundException {
		WeightedGraph wGraph = new WeightedGraph(new Scanner(new FileReader("resources/graph/tinyEWD.txt")));
		System.out.println(wGraph.toString());
		Cycle finder = new Cycle(wGraph);
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