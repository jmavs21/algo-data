package graphs.graph.digraph;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

import fundamentals.Stack;

/**
 * Finds a simple directed cycle in an Directed Graph, using DFS.
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
	private int[] edgeTo;
	private Stack<Integer> cycle;

	public Cycle(Digraph digraph) {
		visited = new boolean[digraph.V()];
		onStack = new boolean[digraph.V()];
		edgeTo = new int[digraph.V()];
		for(int v = 0; v < digraph.V(); v++) {
			if(!visited[v] && cycle == null) {
				dfs(digraph, v);
			}
		}
	}

	public boolean hasCycle() {
		return cycle != null;
	}

	public Iterable<Integer> cycle(){
		return cycle;
	}

	private void dfs(Digraph digraph, int v) {
		visited[v] = true;
		onStack[v] = true;
		for(int w : digraph.adjacents(v)) {
			if(hasCycle()) return;
			if(!visited[w]) {
				edgeTo[w] = v;
				dfs(digraph, w);
			} else if(onStack[w]){
				cycle = new Stack<Integer>();
				for(int x = v; x != w; x = edgeTo[x]) {
					cycle.push(x);
				}
				cycle.push(w);
				cycle.push(v);
			}
		}
		onStack[v] = false;
	}


	public static void main(String[] args) throws FileNotFoundException {
		Digraph digraph = new Digraph(new Scanner(new FileReader("resources/graph/tinyDG.txt")));
		System.out.println(digraph.toString());
		Cycle finder = new Cycle(digraph);
		if (finder.hasCycle()) {
			System.out.print("Directed cycle: ");
			for (int v : finder.cycle()) {
				System.out.print(v + " ");
			}
			System.out.println();
		}
		else {
			System.out.println("No directed cycle.");
		}
		System.out.println();
	}
}