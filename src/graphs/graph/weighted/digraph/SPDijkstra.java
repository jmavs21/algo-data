package graphs.graph.weighted.digraph;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

import fundamentals.Stack;
import sorting.IndexMinPQ;

/**
 * Dijsktra algorithm for solving the single-source Shortest Paths problem in Weighted Digraphs,
 *  where the edge weights are nonnegative.
 * 
 * Extra space: O(V)
 * 
 * Initialization: O(E log V) in the worst case.
 * Operations: 
 *     hasPathTo, distTo: O(1)
 *     pathTo: O(length of path)
 */
public class SPDijkstra 
{
	private double[] distTo;
	private EdgeDirect[] edgeTo;
	private IndexMinPQ<Double> pq;

	public SPDijkstra(WeightedDigraph wDigraph, int s) {
		for(EdgeDirect e: wDigraph.edges()) {
			if(e.weight() < 0) throw new IllegalArgumentException("edge " + e + " has negative weight");
		}
		distTo = new double[wDigraph.V()];
		edgeTo = new EdgeDirect[wDigraph.V()];
		for(int i = 0; i < wDigraph.V(); i++) {
			distTo[i] = Double.POSITIVE_INFINITY;
		}
		validateVertex(s);
		distTo[s] = 0.0;
		pq = new IndexMinPQ<Double>(wDigraph.V());
		pq.insert(s, distTo[s]);
		while(!pq.isEmpty()) {
			int v = pq.delMin();
			for(EdgeDirect e : wDigraph.adjacents(v)) {
				relax(e);
			}
		}
	}

	private void relax(EdgeDirect e) {
		int v = e.from();
		int w = e.to();
		if(distTo[v] + e.weight() < distTo[w]) {
			distTo[w] = distTo[v] + e.weight();
			edgeTo[w] = e;
			if(pq.contains(w)) pq.decreaseKey(w, distTo[w]);
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

	public Iterable<EdgeDirect> pathTo(int v){
		validateVertex(v);
		if(!hasPathTo(v)) return null;
		Stack<EdgeDirect> path = new Stack<EdgeDirect>();
		for(EdgeDirect e = edgeTo[v]; e != null; e = edgeTo[e.from()]) {
			path.push(e);
		}
		return path;
	}

	private void validateVertex(int v) {
		int V = distTo.length;
		if(v < 0 || v >= V) throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V-1));
	}

	public static void main(String[] args) throws FileNotFoundException {
		WeightedDigraph wDigraph = new WeightedDigraph(new Scanner(new FileReader("resources/graph/tinyEWD.txt")));
		int s = 0;
		SPDijkstra sp = new SPDijkstra(wDigraph, s);
		for (int v = 0; v < wDigraph.V(); v++) {
			if (sp.hasPathTo(v)) {
				System.out.printf("%d to %d (%.2f)  ", s, v, sp.distTo(v));
				for (EdgeDirect e : sp.pathTo(v)) {
					System.out.print(e + "   ");
				}
				System.out.println();
			}
			else {
				System.out.printf("%d to %d         no path\n", s, v);
			}
		}
	}
}
