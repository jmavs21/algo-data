package graphs.graph.digraph;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

import fundamentals.Queue;
import fundamentals.Stack;

/**
 * Breadth First Search in Directed Graph for finding shortest paths from a source vertex 's' to every other vertex.
 * 
 * Extra space: O(V)
 * 
 * Initialization: O(V + E) where V is number of vertices and E number of edges.
 * Operations: 
 *     hasPathTo, distTo: O(1)
 *     pathTo: O(length of path)
 */
public class BFSPaths 
{
	private boolean[] visited;
	private int[] edgeTo;
	private int[] distTo;

	public BFSPaths(Digraph digraph, int s) {
		visited = new boolean[digraph.V()];
		edgeTo = new int[digraph.V()];
		distTo = new int[digraph.V()];
		for(int v = 0; v < digraph.V(); v++) {
			distTo[v] = Integer.MAX_VALUE;
		}
		validateVertex(s);
		bfs(digraph, s);
	}

	public BFSPaths(Digraph digraph, Iterable<Integer> sources) {
		visited = new boolean[digraph.V()];
		edgeTo = new int[digraph.V()];
		distTo = new int[digraph.V()];
		for(int v = 0; v < digraph.V(); v++) {
			distTo[v] = Integer.MAX_VALUE;
		}
		bfs(digraph, sources);
	}

	public int distTo(int v) {
		validateVertex(v);
		return distTo[v];
	}

	public boolean hasPathTo(int v) {
		validateVertex(v);
		return visited[v];
	}

	public Iterable<Integer> pathTo(int v){
		validateVertex(v);
		Stack<Integer> stack = new Stack<Integer>();
		int x;
		for(x = v; distTo[x] != 0; x = edgeTo[x]) {
			stack.push(x);
		}
		stack.push(x);
		return stack;
	}

	private void bfs(Digraph digraph , int s) {
		Queue<Integer> queue = new Queue<Integer>();
		visited[s] = true;
		distTo[s] = 0;
		queue.enqueue(s);
		while(!queue.isEmpty()) {
			int v = queue.dequeue();
			for(int w : digraph.adjacents(v)) {
				if(!visited[w]) {
					edgeTo[w] = v;
					distTo[w] = distTo[v] + 1;
					visited[w] = true;
					queue.enqueue(w);
				}
			}
		}
	}

	private void bfs(Digraph digraph , Iterable<Integer> sources) {
		Queue<Integer> queue = new Queue<Integer>();
		for(int s : sources) {	
			validateVertex(s);
			visited[s] = true;
			distTo[s] = 0;
			queue.enqueue(s);
		}
		while(!queue.isEmpty()) {
			int v = queue.dequeue();
			for(int w : digraph.adjacents(v)) {
				if(!visited[w]) {
					edgeTo[w] = v;
					distTo[w] = distTo[v] + 1;
					visited[w] = true;
					queue.enqueue(w);
				}
			}
		}
	}

	private void validateVertex(int v) {
		int V = visited.length;
		if (v < 0 || v >= V)
			throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V-1));
	}

	public static void main(String[] args) throws FileNotFoundException {
		Digraph digraph = new Digraph(new Scanner(new FileReader("resources/graph/tinyDG.txt")));
		System.out.println(digraph.toString());
		int s = 3;
		BFSPaths bfs = new BFSPaths(digraph, s);
		for (int v = 0; v < digraph.V(); v++) {
			if (bfs.hasPathTo(v)) {
				System.out.printf("%d to %d (%d):  ", s, v, bfs.distTo(v));
				for (int x : bfs.pathTo(v)) {
					if (x == s) System.out.print(x);
					else        System.out.print("-" + x);
				}
				System.out.println();
			}
			else {
				System.out.printf("%d to %d (-):  not connected%n", s, v);
			}
		}
	}
}
