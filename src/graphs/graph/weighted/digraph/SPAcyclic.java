package graphs.graph.weighted.digraph;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

import fundamentals.Stack;

/**
 * Algorithm for solving the single-source Shortest Paths problem in Acyclic Weighted Digraphs (DAG),
 *  where the edge can have positive, negative and zero weights.
 * 
 * Extra space: O(V)
 * 
 * Initialization: O(E + V) in the worst case.
 * Operations: 
 *     hasPathTo, distTo: O(1)
 *     pathTo: O(length of path)
 */
public class SPAcyclic 
{
	private double[] distTo;
	private EdgeDirect[] edgeTo;

	public SPAcyclic(WeightedDigraph wDigraph, int s) {
		distTo = new double[wDigraph.V()];
		edgeTo = new EdgeDirect[wDigraph.V()];
		for(int v = 0; v < wDigraph.V(); v++) {
			distTo[v] = Double.POSITIVE_INFINITY;
		}
		validateVertex(s);
		distTo[s] = 0.0;
		Topological topo = new Topological(wDigraph);
		if(!topo.hasOrder()) throw new IllegalArgumentException("Weighted Digraph is not acyclic");
		for(int v : topo.order()) {
			for(EdgeDirect e: wDigraph.adjacents(v)) {
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

	public Iterable<EdgeDirect> pathTo(int v) {
		validateVertex(v);
		if(!hasPathTo(v)) return null;
		Stack<EdgeDirect> stack = new Stack<EdgeDirect>();
		for(EdgeDirect e = edgeTo[v]; e != null; e = edgeTo[e.from()]) {
			stack.push(e);
		}
		return stack;
	}

	private void validateVertex(int v) {
		int V = distTo.length;
		if(v < 0 || v >= V) throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V-1));
	}

	public static void main(String[] args) throws FileNotFoundException {
		WeightedDigraph wDigraph = new WeightedDigraph(new Scanner(new FileReader("resources/graph/tinyEWDAG.txt")));
		int s = 5;
		SPAcyclic sp = new SPAcyclic(wDigraph, s);
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