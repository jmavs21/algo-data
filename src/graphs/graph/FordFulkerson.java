package graphs.graph;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

import fundamentals.Queue;

/**
 * FordFulkerson represents a data type for computing a maximum st-flow and minimum st-cut in a flow network.
 * It uses the algorithm Ford-Fulkerson with the shortest augmenting path heuristic.
 * 
 * Initialization: O(E*V(E + V)) time and space in the worst case.
 * Operations:
 *     inCut, maxValue: O(1)
 *     
 * NOTE: 
 *  In practice the algorithm performs much faster.
 *  If the capacities and initial flow values are all integers, then this implementation guarantees to compute an integer-valued maximum flow.
 *  If the capacities and floating-point numbers, then floating-point roundoff error can accumulate.
 */
public class FordFulkerson 
{
	private final int V;
	private boolean[] visited;
	private FlowEdge[] edgeTo;
	private double maxValue;

	public FordFulkerson(FlowNetwork flowNetwork, int s, int t) {
		V = flowNetwork.V();
		validate(s);
		validate(t);
		if (s == t)                         throw new IllegalArgumentException("Source equals sink");
		// while there exists an augmenting path, use it
		maxValue = 0.0;
		while(hasAugmentingPath(flowNetwork, s, t)) {
			double bottle = Double.POSITIVE_INFINITY;
			for(int v = t; v != s; v = edgeTo[v].other(v)) {
				bottle = Math.min(bottle, edgeTo[v].residualCapacityTo(v));
			}
			for(int v = t; v != s; v = edgeTo[v].other(v)) {
				edgeTo[v].addResidualFlowTo(v, bottle);
			}
			maxValue += bottle;
		}
	}

	public double maxValue() {
		return maxValue;
	}

	public boolean inCut(int v) {
		validate(v);
		return visited[v];
	}

	private boolean hasAugmentingPath(FlowNetwork flowNetwork, int s, int t) {
		visited = new boolean[flowNetwork.V()];
		edgeTo = new FlowEdge[flowNetwork.V()];
		Queue<Integer> queue = new Queue<Integer>();
		visited[s] = true;
		queue.enqueue(s);
		while(!queue.isEmpty() && !visited[t]) {
			int v = queue.dequeue();
			for(FlowEdge e : flowNetwork.adjacents(v)) {
				int w = e.other(v);
				if(!visited[w] && e.residualCapacityTo(w) > 0) {
					visited[w] = true;
					edgeTo[w] = e;
					queue.enqueue(w);
				}
			}
		}
		return visited[t];
	}

	private void validate(int v)  {
		if (v < 0 || v >= V) throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V-1));
	}

	public static void main(String[] args) throws FileNotFoundException {
		FlowNetwork flowNetwrok = new FlowNetwork(new Scanner(new FileReader("resources/graph/tinyFN.txt")));
		int V = flowNetwrok.V();
		int s = 0, t = V-1;
		System.out.println(flowNetwrok);
		// compute maximum flow and minimum cut
		FordFulkerson maxflow = new FordFulkerson(flowNetwrok, s, t);
		System.out.println("Max flow from " + s + " to " + t);
		for (int v = 0; v < flowNetwrok.V(); v++) {
			for (FlowEdge e : flowNetwrok.adjacents(v)) {
				if ((v == e.from()) && e.flow() > 0)
					System.out.println("   " + e);
			}
		}
		// print min-cut
		System.out.print("Min cut: ");
		for (int v = 0; v < flowNetwrok.V(); v++) {
			if (maxflow.inCut(v)) System.out.print(v + " ");
		}
		System.out.println();
		System.out.println("Max flow value = " +  maxflow.maxValue());
	}
}