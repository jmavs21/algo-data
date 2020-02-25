package graphs.graph.weighted.digraph;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

import fundamentals.Queue;
import fundamentals.Stack;

/**
 * Solves single-source Shortest Paths in Weighted Digraphs or returns a negative cycle,
 *    the edges weights can be positive, negative or zero.
 * 
 * Extra space: O(V)
 * 
 * Initialization: O(V(V + E)) in the worst case.
 * Operations:
 *     distTo, hasPathTo, hasNegativeCycle: O(1)
 *     pathTo, negativeCycle: O(length of path)
 * 
 * NOTE: uses Bellman-Ford-Moore algorithm.
 */
public class BellmanFordSP 
{
	private double[] distTo;
	private EdgeDirect[] edgeTo;
	private boolean[] onQueue;          // onQueue[v] = is v currently on the queue?
	private Queue<Integer> queue;       // queue of vertices to relax
	private int cost;                   // number of calls to relax()
	private Iterable<EdgeDirect> cycle; // negative cycle (or null if no such cycle)

	public BellmanFordSP(WeightedDigraph wDigraph, int s) {
		distTo  = new double[wDigraph.V()];
		edgeTo  = new EdgeDirect[wDigraph.V()];
		onQueue = new boolean[wDigraph.V()];
		for (int v = 0; v < wDigraph.V(); v++)
			distTo[v] = Double.POSITIVE_INFINITY;
		distTo[s] = 0.0;
		queue = new Queue<Integer>();
		queue.enqueue(s);
		onQueue[s] = true;
		while (!queue.isEmpty() && !hasNegativeCycle()) {
			int v = queue.dequeue();
			onQueue[v] = false;
			for (EdgeDirect e : wDigraph.adjacents(v)) 
				relax(e, wDigraph, v);
		}
		// other way to do without the use of a Queue: O(V(V+E))
		//		for (int pass = 0; pass < wDigraph.V(); pass++)
		//			for (int v = 0; v < wDigraph.V(); v++)
		//				for (EdgeDirect e : wDigraph.adjacents(v)) 				
		//					relax(e, wDigraph, v);
	}

	private void relax(EdgeDirect e, WeightedDigraph wDigraph, int v) {
		int w = e.to();
		if (distTo[w] > distTo[v] + e.weight()) {
			distTo[w] = distTo[v] + e.weight();
			edgeTo[w] = e;
			if (!onQueue[w]) {
				queue.enqueue(w);
				onQueue[w] = true;
			}
		}
		if (cost++ % wDigraph.V() == 0) {
			findNegativeCycle();
		}
	}

	public boolean hasNegativeCycle() {
		return cycle != null;
	}

	public Iterable<EdgeDirect> negativeCycle() {
		return cycle;
	}

	private void findNegativeCycle() {
		int V = edgeTo.length;
		WeightedDigraph wDigraph = new WeightedDigraph(V);
		for (int v = 0; v < V; v++)
			if (edgeTo[v] != null)
				wDigraph.addEdge(edgeTo[v]);
		Cycle finder = new Cycle(wDigraph);
		cycle = finder.cycle();
	}

	public double distTo(int v) {
		validateVertex(v);
		if (hasNegativeCycle())
			throw new UnsupportedOperationException("Negative cost cycle exists");
		return distTo[v];
	}

	public boolean hasPathTo(int v) {
		validateVertex(v);
		return distTo[v] < Double.POSITIVE_INFINITY;
	}

	public Iterable<EdgeDirect> pathTo(int v) {
		validateVertex(v);
		if (hasNegativeCycle())
			throw new UnsupportedOperationException("Negative cost cycle exists");
		if (!hasPathTo(v)) return null;
		Stack<EdgeDirect> path = new Stack<EdgeDirect>();
		for (EdgeDirect e = edgeTo[v]; e != null; e = edgeTo[e.from()]) {
			path.push(e);
		}
		return path;
	}

	private void validateVertex(int v) {
		int V = distTo.length;
		if (v < 0 || v >= V)
			throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V-1));
	}

	public static void main(String[] args) throws FileNotFoundException {
		Scanner in = new Scanner(new FileReader("resources/graph/tinyEWDn.txt")); // tinyEWDnc.txt for negative cycle.
		int s = 0;
		WeightedDigraph wDigraph = new WeightedDigraph(in);
		BellmanFordSP sp = new BellmanFordSP(wDigraph, s);
		if (sp.hasNegativeCycle()) {
			for (EdgeDirect e : sp.negativeCycle())
				System.out.println(e);
		}
		else {
			for (int v = 0; v < wDigraph.V(); v++) {
				if (sp.hasPathTo(v)) {
					System.out.printf("%d to %d (%5.2f)  ", s, v, sp.distTo(v));
					for (EdgeDirect e : sp.pathTo(v)) {
						System.out.print(e + "   ");
					}
					System.out.println();
				}
				else {
					System.out.printf("%d to %d           no path\n", s, v);
				}
			}
		}
	}
}
