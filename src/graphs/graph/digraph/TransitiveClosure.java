package graphs.graph.digraph;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

/**
 * Compute transitive closure of a digraph and support reachability queries.
 * 
 * Extra space: O(V^2).
 * 
 * Initialization: O(V(E + V)) in the worst case.
 * Operations:
 *     reachable: O(1)
 */
public class TransitiveClosure 
{
	private DFSPaths[] tc;

	public TransitiveClosure(Digraph graph) {
		tc = new DFSPaths[graph.V()];
		for (int v = 0; v < graph.V(); v++) {			
			tc[v] = new DFSPaths(graph, v);
		}
	}

	public boolean reachable(int v, int w) {
		validateVertex(v);
		validateVertex(w);
		return tc[v].hasPathTo(w);
	}

	private void validateVertex(int v) {
		int V = tc.length;
		if (v < 0 || v >= V) throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V-1));
	}

	public static void main(String[] args) throws FileNotFoundException {
		Scanner sc = new Scanner(new FileReader("resources/graph/tinyDG.txt"));
		Digraph digraph = new Digraph(sc);
		TransitiveClosure tc = new TransitiveClosure(digraph);
		// print header
		System.out.print("     ");
		for (int v = 0; v < digraph.V(); v++) {			
			System.out.printf("%3d", v);
		}
		System.out.println();
		System.out.println("--------------------------------------------");
		// print transitive closure
		for (int v = 0; v < digraph.V(); v++) {
			System.out.printf("%3d: ", v);
			for (int w = 0; w < digraph.V(); w++) {
				if (tc.reachable(v, w)) System.out.printf("  T");
				else                    System.out.printf("   ");
			}
			System.out.println();
		}
		sc.close();
	}
}
