package graphs.graph.weighted.digraph;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

/**
 * Dijsktra algorithm for solving the all-paris Shortest Paths problem in Weighted Digraphs,
 *  where the edge weights are nonnegative.
 * 
 * Extra space: O(V^2)
 * 
 * Initialization: O(V(E log V)) in the worst case.
 * Operations: 
 *     hasPathTo, distTo: O(1)
 *     pathTo: O(length of path)
 */
public class SPDijkstraAllPairs 
{
	private SPDijkstra[] all;

	public SPDijkstraAllPairs(WeightedDigraph wDigraph) {
		all = new SPDijkstra[wDigraph.V()];
		for(int v = 0; v < wDigraph.V(); v++) {
			all[v] = new SPDijkstra(wDigraph, v);
		}
	}

	public Iterable<EdgeDirect> path(int s, int t) {
		validateVertex(s);
		validateVertex(t);
		return all[s].pathTo(t);
	}

	public double dist(int s, int t) {
		validateVertex(s);
		validateVertex(t);
		return all[s].distTo(t);
	}

	public boolean hasPath(int s, int t) {
		validateVertex(s);
		validateVertex(t);
		return dist(s, t) < Double.POSITIVE_INFINITY;
	}

	private void validateVertex(int v) {
		int V = all.length;
		if (v < 0 || v >= V) throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V-1));
	}

	public static void main(String[] args) throws FileNotFoundException {
		WeightedDigraph wDigraph = new WeightedDigraph(new Scanner(new FileReader("resources/graph/tinyEWD.txt")));
		SPDijkstraAllPairs spt = new SPDijkstraAllPairs(wDigraph);
		System.out.printf("  ");
		for (int v = 0; v < wDigraph.V(); v++) {
			System.out.printf("%6d ", v);
		}
		System.out.println();
		for (int v = 0; v < wDigraph.V(); v++) {
			System.out.printf("%3d: ", v);
			for (int w = 0; w < wDigraph.V(); w++) {
				if (spt.hasPath(v, w)) System.out.printf("%6.2f ", spt.dist(v, w));
				else System.out.printf("  Inf ");
			}
			System.out.println();
		}
		System.out.println();
		for (int v = 0; v < wDigraph.V(); v++) {
			for (int w = 0; w < wDigraph.V(); w++) {
				if (spt.hasPath(v, w)) {
					System.out.printf("%d to %d (%5.2f)  ", v, w, spt.dist(v, w));
					for (EdgeDirect e : spt.path(v, w))
						System.out.print(e + "  ");
					System.out.println();
				}
				else {
					System.out.printf("%d to %d no path\n", v, w);
				}
			}
		}
	}
}
