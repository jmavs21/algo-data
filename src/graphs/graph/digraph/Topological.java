package graphs.graph.digraph;

import java.io.FileNotFoundException;

/**
 * Finds the topological* order, only if the is a directed acyclic graph (DAG).
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
 *     
 * *Topological: given a digraph, put the vertices in order such that all its directed edges 
 * point from a vertex earlier in the order to a vertex later in the order (or report that doing so is not possible).
 */
public class Topological 
{
	private Iterable<Integer> order;
	private int[] rank;

	public Topological(Digraph digraph) {
		Cycle cycle = new Cycle(digraph);
		if(!cycle.hasCycle()) {
			DepthFirstOrder dfo = new DepthFirstOrder(digraph);
			order = dfo.reversePostorder();
			rank = new int[digraph.V()];
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
		SymbolDigraph sg = new SymbolDigraph("resources/graph/jobs.txt", "/");
		Topological topological = new Topological(sg.digraph());
		for (int v : topological.order()) {
			System.out.println(sg.nameOf(v));
		}
	}
}
