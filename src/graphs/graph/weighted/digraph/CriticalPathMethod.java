package graphs.graph.weighted.digraph;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

/**
 * Client that solves the parallel precedence-constrained job scheduling problem
 *  via the critical path method (CPM).
 * 
 * Extra space: O(V)
 * 
 * Initialization: O(E + V) in the worst case.
 */
public class CriticalPathMethod 
{
	private CriticalPathMethod() {}

	public static void main(String[] args) throws FileNotFoundException {
		Scanner sc = new Scanner(new FileReader("resources/graph/jobsPC.txt"));
		int n = sc.nextInt();
		int source = 2*n;
		int sink   = 2*n + 1;
		WeightedDigraph G = new WeightedDigraph(2*n + 2);
		for (int i = 0; i < n; i++) {
			double duration = sc.nextDouble();
			G.addEdge(new EdgeDirect(source, i, 0.0));
			G.addEdge(new EdgeDirect(i+n, sink, 0.0));
			G.addEdge(new EdgeDirect(i, i+n,    duration));
			int m = sc.nextInt();
			for (int j = 0; j < m; j++) {
				int precedent = sc.nextInt();
				G.addEdge(new EdgeDirect(n+i, precedent, 0.0));
			}
		}
		LPAcyclic lp = new LPAcyclic(G, source);
		System.out.println(" job   start  finish");
		System.out.println("--------------------");
		for (int i = 0; i < n; i++) {
			System.out.printf("%4d %7.1f %7.1f\n", i, lp.distTo(i), lp.distTo(i+n));
		}
		System.out.printf("Finish time: %7.1f\n", lp.distTo(sink));
		sc.close();
	}
}
