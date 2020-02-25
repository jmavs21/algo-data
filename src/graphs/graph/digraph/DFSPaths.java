package graphs.graph.digraph;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Iterator;
import java.util.Scanner;

import fundamentals.Stack;

/**
 * Depth First Search in Directed Graph for finding paths from a source vertex 's' to every other vertex.
 * 
 * Extra space: O(V)
 * 
 * Initialization: O(V + E) where V is number of vertices and E number of edges.
 * Operations: 
 *     hasPathTo: O(1)
 *     pathTo: O(length of path)
 */
public class DFSPaths 
{
	private boolean[] visited;
	private int[] edgeTo;
	private int s;

	public DFSPaths(Digraph digraph, int s) {
		this.s = s;
		visited = new boolean[digraph.V()];
		edgeTo = new int[digraph.V()];
		validateVertex(s);
		dfs(digraph, s);
	}
	
	// not support for paths (edgeTo)
	public DFSPaths(Digraph digraph, Iterable<Integer> sources) {
		visited = new boolean[digraph.V()];
		edgeTo = new int[digraph.V()];
        for (int v : sources) {
        	validateVertex(v);
            if (!visited[v]) dfs(digraph, v);
        }
    }

	public DFSPaths(Digraph digraph, int s, boolean nonrecursive) {
		if(nonrecursive) {
			this.s = s;
			visited = new boolean[digraph.V()];
			edgeTo = new int[digraph.V()];
			validateVertex(s);
			nonrecursiveDFS(digraph, s);
		} else {
			System.out.println("Call only with nonrecursive to true.");
		}
	}

	public boolean hasPathTo(int v) {
		validateVertex(v);
		return visited[v];
	}

	public Iterable<Integer> pathTo(int v){
		validateVertex(v);
		if(!hasPathTo(v)) return null;
		Stack<Integer> stack = new Stack<Integer>();
		for(int x = v; x != s; x = edgeTo[x]) {
			stack.push(x);
		}
		stack.push(s);
		return stack;
	}

	private void dfs(Digraph digraph, int v) {
		visited[v] = true;
		for(int w : digraph.adjacents(v)) {
			if(!visited[w]) {
				edgeTo[w] = v;
				dfs(digraph, w);
			}
		}
	}

	@SuppressWarnings("unchecked")
	private void nonrecursiveDFS(Digraph digraph, int s) {
		Iterator<Integer>[] adj = (Iterator<Integer>[]) new Iterator[digraph.V()];
		for(int v = 0; v < digraph.V(); v++) {
			adj[v] = digraph.adjacents(v).iterator();
		}
		Stack<Integer> stack = new Stack<Integer>();
		visited[s] = true;
		stack.push(s);
		while(!stack.isEmpty()) {
			int v = stack.peek();
			if(adj[v].hasNext()) {
				int w = adj[v].next();
				if(!visited[w]) {
					edgeTo[w] = v;
					visited[w] = true;
					stack.push(w);
				}
			} else {
				stack.pop();
			}
		}
	}

	private void validateVertex(int v) {
		int V = visited.length;
		if(v < 0 || v >= V) throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V-1));
	}

	public static void main(String[] args) throws FileNotFoundException {
		Digraph digraph = new Digraph(new Scanner(new FileReader("resources/graph/tinyDG.txt")));
		System.out.println(digraph.toString());
		int s = 3;
		System.out.println("----- Recursive DFS: -----");
		DFSPaths dfs = new DFSPaths(digraph, s);
		for (int v = 0; v < digraph.V(); v++) {
			if (dfs.hasPathTo(v)) {
				System.out.printf("%d to %d:  ", s, v);
				for (int x : dfs.pathTo(v)) {
					if (x == s) System.out.print(x);
					else        System.out.print("-" + x);
				}
				System.out.println();
			}
			else {
				System.out.printf("%d to %d:  not connected%n", s, v);
			}
		}
		System.out.println();
		
		System.out.println("----- Nonrecursive DFS: -----");
		dfs = new DFSPaths(digraph, s, true);
		for (int v = 0; v < digraph.V(); v++) {
			if (dfs.hasPathTo(v)) {
				System.out.printf("%d to %d:  ", s, v);
				for (int x : dfs.pathTo(v)) {
					if (x == s) System.out.print(x);
					else        System.out.print("-" + x);
				}
				System.out.println();
			}
			else {
				System.out.printf("%d to %d:  not connected%n", s, v);
			}
		}
		System.out.println();
	}

}
