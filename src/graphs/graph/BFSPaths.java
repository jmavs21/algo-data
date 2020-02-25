package graphs.graph;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

import fundamentals.Queue;
import fundamentals.Stack;

/**
 * Breadth First Search in Undirected Graph for finding shortest paths from a source vertex 's' to every other vertex.
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

	public BFSPaths(Graph graph, int s) {
		visited = new boolean[graph.V()];
		edgeTo = new int[graph.V()];
		distTo = new int[graph.V()];
		for (int v = 0; v < graph.V(); v++) {        	
			distTo[v] = Integer.MAX_VALUE;
		}
		validateVertex(s);
		bfs(graph, s);
	}

	public BFSPaths(Graph graph, Iterable<Integer> sources) {
		visited = new boolean[graph.V()];
		edgeTo = new int[graph.V()];
		distTo = new int[graph.V()];
		for (int v = 0; v < graph.V(); v++) {        	
			distTo[v] = Integer.MAX_VALUE;
		}
		bfs(graph, sources);
	}

	public boolean hasPathTo(int v) {
		validateVertex(v);
		return visited[v];
	}

	public int distTo(int v) {
		validateVertex(v);
		return distTo[v];
	}

	public Iterable<Integer> pathTo(int v) {
		validateVertex(v);
		if(!hasPathTo(v)) return null;
		Stack<Integer> stack = new Stack<Integer>();
		int x;
		for(x = v; distTo[x] != 0; x = edgeTo[x]) {
			stack.push(x);
		}
		stack.push(x);
		return stack;
	}

	private void bfs(Graph graph, int s) {
		Queue<Integer> q = new Queue<Integer>();
		visited[s] = true;
		distTo[s] = 0;
		q.enqueue(s);
		while(!q.isEmpty()) {
			int v = q.dequeue();
			for(int w : graph.adjacents(v)) {
				if(!visited[w]) {
					visited[w] = true;
					edgeTo[w] = v;
					distTo[w] = distTo[v] + 1;
					q.enqueue(w);
				}
			}
		}
	}

	private void bfs(Graph graph, Iterable<Integer> sources) {
		Queue<Integer> q = new Queue<Integer>();
		for(int s : sources) {
			validateVertex(s);
			visited[s] = true;
			distTo[s] = 0;
			q.enqueue(s);
		}
		while(!q.isEmpty()) {
			int v = q.dequeue();
			for(int w : graph.adjacents(v)) {
				if(!visited[w]) {
					visited[w] = true;
					edgeTo[w] = v;
					distTo[w] = distTo[v] + 1;
					q.enqueue(w);
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
		Graph graph = new Graph(new Scanner(new FileReader("resources/graph/tinyCG.txt")));
		System.out.println(graph.toString());
		int s = 0;
		BFSPaths bfs = new BFSPaths(graph, s);
		for (int v = 0; v < graph.V(); v++) {
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