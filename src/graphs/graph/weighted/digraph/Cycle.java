package graphs.graph.weighted.digraph;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

import fundamentals.Stack;

/**
 * Finds a simple directed cycle in an Weighted Directed Graph, using DFS.
 * 
 * Extra space: O(V)
 * 
 * Initialization: O(E + V) in the worst case.
 * Operations:
 *     hasCycle: O(1)
 *     cycle: O(length of cycle)
 */
public class Cycle 
{
	private boolean[] visited;
	private boolean[] onStack;
	private EdgeDirect[] edgeTo;
	private Stack<EdgeDirect> cycle;

	public Cycle(WeightedDigraph wDigraph) {
		visited = new boolean[wDigraph.V()];
		onStack = new boolean[wDigraph.V()];
		edgeTo = new EdgeDirect[wDigraph.V()];
		for(int v = 0; v < wDigraph.V(); v++) {
			if(!visited[v] && cycle == null) {
				dfs(wDigraph, v);
			}
		}
	}

	public boolean hasCycle() {
		return cycle != null;
	}

	public Iterable<EdgeDirect> cycle(){
		return cycle;
	}

	private void dfs(WeightedDigraph wDigraph, int v) {
		visited[v] = true;
		onStack[v] = true;
		for(EdgeDirect e : wDigraph.adjacents(v)) {
			int w = e.to();
			if(hasCycle()) return;
			if(!visited[w]) {
				edgeTo[w] = e;
				dfs(wDigraph, w);
			} else if(onStack[w]){
				cycle = new Stack<EdgeDirect>();
				EdgeDirect c = e;
				while (c.from() != w) {
					cycle.push(c);
					c = edgeTo[c.from()];
				}
				cycle.push(c);
				return;
			}
		}
		onStack[v] = false;
	}

	public static void main(String[] args) throws FileNotFoundException {
		WeightedDigraph wDigraph = new WeightedDigraph(new Scanner(new FileReader("resources/graph/tinyEWD.txt")));
		System.out.println(wDigraph.toString());
		Cycle finder = new Cycle(wDigraph);
		if (finder.hasCycle()) {
			System.out.print("Directed cycle: ");
			for (EdgeDirect e : finder.cycle()) {
				System.out.print(e + " ");
			}
			System.out.println();
		}
		else {
			System.out.println("No directed cycle.");
		}
		System.out.println();
	}
}