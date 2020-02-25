package graphs.graph;

import java.io.FileNotFoundException;

/**
 * Clint for a client for finding the degree of separation between one entity and
 *  every other entity in a graph.
 * 
 * It uses extra space proportional to O(E + V).
 * 
 * Initialization: O(E + V)
 */
public class DegreesOfSeparation 
{
	private DegreesOfSeparation() { }

	public static void main(String[] args) throws FileNotFoundException {
		String filename  = "resources/graph/routes.txt";
		String delimiter = " ";
		SymbolGraph sg = new SymbolGraph(filename, delimiter);
		Graph graph = sg.graph();
		String source    = "JFK";
		if (!sg.contains(source)) {
			System.out.println(source + " not in database.");
			return;
		}
		int s = sg.indexOf(source);
		BFSPaths bfs = new BFSPaths(graph, s);
		String[] destinations = { "LAS", "DFW", "JFK", "EWR"};
		for(int i = 0; i < destinations.length; i++) {
			String to = destinations[i];
			System.out.print(to);
			if (sg.contains(to)) {
				System.out.print(" (" + bfs.distTo(sg.indexOf(to)) + ")\n");
				int t = sg.indexOf(to);
				if (bfs.hasPathTo(t)) {
					for (int v : bfs.pathTo(t)) {
						System.out.println("   " + sg.nameOf(v));
					}
				}
				else {
					System.out.println("Not connected");
				}
			}
			else {
				System.out.println("   Not in database.");
			}
		}
	}
}
