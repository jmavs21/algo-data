package graphs.graph.weighted.digraph;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

/**
 * Finds the topological order, only if Weighted Acyclic Digraph (DAG).
 * It uses Cycle and DepthFirstOrder.
 * 
 * Extra space: O(V)
 * 
 * Initialization: O(E + V) in the worst case.
 * Operations:
 *     hasOrder, rank: O(1)
 *     order: O(V)
 *     
 * Note: implementation uses a LinkedHashMap in order to mantiain insertion order (similar to a queue FIFO).
 *     where: key is a vertex, and value is index of topological order (reverse postorder).
 */
public class Topological 
{
	private Iterable<Integer> order;
	private int[] rank;
	
	public Topological(WeightedDigraph wDigraph) {
		Cycle cycle = new Cycle(wDigraph);
		if(!cycle.hasCycle()) {
			DepthFirstOrder dfo = new DepthFirstOrder(wDigraph);
			order = dfo.reversePostorder();
			rank = new int[wDigraph.V()];
			int i = 0;
			for(int v : order) {
				rank[v] = i++;
			}
		}
	}

	public boolean hasOrder() {
		return order != null;
	}

	public Iterable<Integer> order(){
		return order;
	}

	public int rank(int v) {
		validateVertex(v);
		if(hasOrder()) return rank[v];
		return -1;
	}

	private void validateVertex(int v) {
		int V = rank.length;
		if (v < 0 || v >= V)
			throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V-1));
	}

	public static void main(String[] args) throws FileNotFoundException {
		WeightedDigraph wDigraph = new WeightedDigraph(new Scanner(new FileReader("resources/graph/tinyEWDAG.txt")));
		Topological topological = new Topological(wDigraph);
		for (int v : topological.order()) {
			System.out.print(v + " ");
		}
		System.out.println();
	}
}
