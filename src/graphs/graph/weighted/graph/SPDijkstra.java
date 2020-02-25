package graphs.graph.weighted.graph;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

import fundamentals.Stack;
import sorting.IndexMinPQ;

/**
 * Dijsktra algorithm for solving the single-source Shortest Paths problem in Weighted Graphs,
 *  where all edge weights are nonnegative.
 * 
 * Extra space: O(V)
 * Initialization: O(E log V) in the worst case.
 * Operations: 
 *     hasPathTo, distTo: O(1)
 *     pathTo: O(length of path)
 */
public class SPDijkstra 
{
	private double[] distTo;
	private Edge[] edgeTo;
	private IndexMinPQ<Double> pq;

	public SPDijkstra(WeightedGraph wGraph, int s) {
		for(Edge e: wGraph.edges()) {
			if(e.weight() < 0) throw new IllegalArgumentException("edge " + e + " has negative weight");
		}
		distTo = new double[wGraph.V()];
		edgeTo = new Edge[wGraph.V()];
		for(int i = 0; i < wGraph.V(); i++) {
			distTo[i] = Double.POSITIVE_INFINITY;
		}
		validateVertex(s);
		distTo[s] = 0.0;
		pq = new IndexMinPQ<Double>(wGraph.V());
		pq.insert(s, distTo[s]);
		while(!pq.isEmpty()) {
			int v = pq.delMin();
			for(Edge e : wGraph.adjacents(v)) {
				relax(e, v);
			}
		}
	}

	private void relax(Edge e, int v) {
		int w = e.other(v);
		if(distTo[v] + e.weight() < distTo[w]) {
			distTo[w] = distTo[v] + e.weight();
			edgeTo[w] = e;
			if(pq.contains(v)) pq.decreaseKey(w, distTo[w]);
			else               pq.insert(w, distTo[w]);
		}
	}

	public double distTo(int v) {
		validateVertex(v);
		return distTo[v];
	}

	public boolean hasPathTo(int v) {
		validateVertex(v);
		return distTo[v] < Double.POSITIVE_INFINITY;
	}

	public Iterable<Edge> pathTo(int v){
		validateVertex(v);
		if(!hasPathTo(v)) return null;
		Stack<Edge> path = new Stack<Edge>();
		int x = v;
		for(Edge e = edgeTo[v]; e != null; e = edgeTo[x]) {
			path.push(e);
			x = e.other(x);
		}
		return path;
	}

	private void validateVertex(int v) {
		int V = distTo.length;
		if(v < 0 || v >= V) throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V-1));
	}

	public static void main(String[] args) throws FileNotFoundException {
		WeightedGraph wGraph = new WeightedGraph(new Scanner(new FileReader("resources/graph/tinyEWG.txt")));
		int s = 6;
		SPDijkstra sp = new SPDijkstra(wGraph, s);
		for (int t = 0; t < wGraph.V(); t++) {
			if (sp.hasPathTo(t)) {
				System.out.printf("%d to %d (%.2f)  ", s, t, sp.distTo(t));
				for (Edge e : sp.pathTo(t)) {
					System.out.print(e + "   ");
				}
				System.out.println();
			}
			else {
				System.out.printf("%d to %d         no path\n", s, t);
			}
		}
	}
}
