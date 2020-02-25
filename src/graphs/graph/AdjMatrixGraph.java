package graphs.graph;

import java.util.Iterator;
import java.util.NoSuchElementException;

import util.RandomN;

public class AdjMatrixGraph 
{
	private final int V;
	private int E;
	private boolean[][] adj;

	public AdjMatrixGraph(int V) {
		if (V < 0) throw new IllegalArgumentException("Too few vertices");
		this.V = V;
		this.E = 0;
		this.adj = new boolean[V][V];
	}

	public AdjMatrixGraph(int V, int E) {
		this(V);
		if (E > (long) V*(V-1)/2 + V) throw new IllegalArgumentException("Too many edges");
		if (E < 0)                    throw new IllegalArgumentException("Too few edges");
		while (this.E != E) {
			int v = RandomN.getRandomInt(V);
			int w = RandomN.getRandomInt(V);
			addEdge(v, w);
		}
	}

	public int V() { return V; }
	public int E() { return E; }

	public void addEdge(int v, int w) {
		if (!adj[v][w]) E++;
		adj[v][w] = true;
		adj[w][v] = true;
	}

	public boolean contains(int v, int w) {
		return adj[v][w];
	}

	public Iterable<Integer> adj(int v) {
		return new AdjIterator(v);
	}

	private class AdjIterator implements Iterator<Integer>, Iterable<Integer> {
		private int v;
		private int w = 0;
		AdjIterator(int v) {
			this.v = v;
		}
		public Iterator<Integer> iterator() {
			return this;
		}
		public boolean hasNext() {
			while (w < V) {
				if (adj[v][w]) 
					return true;
				w++;
			}
			return false;
		}
		public Integer next() {
			if (!hasNext()) {
				throw new NoSuchElementException();
			}
			return w++;
		}
		public void remove()  { throw new UnsupportedOperationException(); }
	}

	public String toString() {
		StringBuilder s = new StringBuilder();
		s.append(V + " " + E + "\n");
		for (int v = 0; v < V; v++) {
			s.append(v + ": ");
			for (int w : adj(v)) {
				s.append(w + " ");
			}
			s.append("\n");
		}
		return s.toString();
	}

	public static void main(String[] args) {
		int V = 6;
		int E = 8;
		AdjMatrixGraph G = new AdjMatrixGraph(V, E);
		System.out.println(G);
	}
}
