package sorting;

import java.util.Scanner;

/**
 * Multiway merges together the sorted input stream given as array
 *     into a single sorted output string on standard output.
 *  
 * @author joaquin
 */
public class Multiway 
{
	private Multiway() { }

	private static void merge(Scanner[] streams) {
		int n = streams.length;
		IndexMinPQ<String> pq = new IndexMinPQ<String>(n);
		for (int i = 0; i < n; i++) 	{
			if (streams[i].hasNext()) pq.insert(i, streams[i].next());			
		}
		while (!pq.isEmpty()) {
			System.out.print(pq.minKey() + " ");
			int i = pq.delMin();
			if (streams[i].hasNext()) pq.insert(i, streams[i].next());
		}
		System.out.println();
	}

	public static void main(String[] args) {
		Scanner[] streams = {new Scanner("A B C F G I I Z"), new Scanner("B D H P Q Q"), new Scanner("A B E F J N")};
		merge(streams);
	}
}
