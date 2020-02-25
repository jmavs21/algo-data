package graphs.graph.weighted.graph;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

import fundamentals.Queue;
import fundamentals.UnionFind;
import sorting.MinPQ;

/**
 * Compute a Minimum Spanning Tree(s) using Kruskal's algorithm for a Weighted Graph.
 * Edge weights can be positive, zero, or negative and need not be distinct.
 * 
 * Extra space: O(E)
 * Initialization: O(E log E) in the worst case.
 * Operations:
 *     weight: O(1)
 *     edges: O(V)
 */
public class MST 
{
	private Queue<Edge> mst = new Queue<Edge>();
	private double weight;

	public MST(WeightedGraph wGraph) {
		MinPQ<Edge> pq = new MinPQ<Edge>();
		for (Edge e : wGraph.edges()) {
			pq.insert(e);
		}
		UnionFind uf = new UnionFind(wGraph.V());
		while(!pq.isEmpty() && mst.size() < wGraph.V() - 1) {
			Edge edge = pq.deleteMin();
			int v = edge.either();
			int w = edge.other(v);
			if(!uf.connected(v, w)) {				
				uf.union(v, w);
				mst.enqueue(edge);
				weight += edge.weight();
			}
		}
	}

	public Iterable<Edge> edges(){
		return mst;
	}

	public double weight() {
		return weight;
	}

	public static void main(String[] args) throws FileNotFoundException {
		WeightedGraph G = new WeightedGraph(new Scanner(new FileReader("resources/graph/tinyEWG.txt")));
		MST mst = new MST(G);
		for (Edge e : mst.edges()) {
			System.out.println(e);
		}
		System.out.printf("MST weight: %.5f\n", mst.weight());
	}
}