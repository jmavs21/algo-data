package sorting;

import java.util.Iterator;
import java.util.NoSuchElementException;

import fundamentals.KnuthShuffle;
import util.RandomN;

/**
 * Index max riority queue of comparable keys implemented with binary tree heap (array).
 * 
 * Initialization: O(n) where n is the number of items (capacity).
 * Operations:
 *     insert, delMax, delete, changeKey, decreaseKey, increaseKey: O(log N) amortized time.
 *     maxIndex, maxKey, size, isEmpty, contains, keyOf: O(1)
 * 
 * @author joaquin
 */
public class IndexMaxPQ<T extends Comparable<T>> implements Iterable<Integer> {
	private int n;           // number of elements on PQ
	private int[] pq;        // binary heap using 1-based indexing
	private int[] qp;        // inverse of pq - qp[pq[i]] = pq[qp[i]] = i
	private T[] keys;      // keys[i] = priority of i

	@SuppressWarnings("unchecked")
	public IndexMaxPQ(int maxN) {
		if (maxN < 0) throw new IllegalArgumentException();
		n = 0;
		keys = (T[]) new Comparable[maxN + 1];
		pq   = new int[maxN + 1];
		qp   = new int[maxN + 1];
		for (int i = 0; i <= maxN; i++) {			
			qp[i] = -1;
		}
	}

	public boolean isEmpty() {
		return n == 0;
	}

	public boolean contains(int i) {
		return qp[i] != -1;
	}

	public int size() {
		return n;
	}

	public void insert(int i, T key) {
		if (contains(i)) throw new IllegalArgumentException("Index is already in the priority queue.");
		n++;
		qp[i] = n;
		pq[n] = i;
		keys[i] = key;
		swim(n);
	}

	public int maxIndex() {
		if (n == 0) throw new NoSuchElementException("Priority queue underflow.");
		return pq[1];
	}

	public T maxKey() {
		if (n == 0) throw new NoSuchElementException("Priority queue underflow.");
		return keys[pq[1]];
	}

	public int delMax() {
		if (n == 0) throw new NoSuchElementException("Priority queue underflow.");
		int max = pq[1];
		swap(1, n--);
		sink(1);
		qp[max] = -1;        // delete
		keys[max] = null;    // to help with garbage collection
		pq[n+1] = -1;        // not needed
		return max;
	}

	public T keyOf(int i) {
		if (!contains(i)) throw new NoSuchElementException("Index is not in the priority queue.");
		else return keys[i];
	}

	public void changeKey(int i, T key) {
		if (!contains(i)) throw new NoSuchElementException("Index is not in the priority queue.");
		keys[i] = key;
		swim(qp[i]);
		sink(qp[i]);
	}

	public void increaseKey(int i, T key) {
		if (!contains(i)) throw new NoSuchElementException("Index is not in the priority queue.");
		if (keys[i].compareTo(key) >= 0)
			throw new IllegalArgumentException("Calling increaseKey() with given argument would not strictly increase the key.");
		keys[i] = key;
		swim(qp[i]);
	}

	public void decreaseKey(int i, T key) {
		if (!contains(i)) throw new NoSuchElementException("index is not in the priority queue");
		if (keys[i].compareTo(key) <= 0)
			throw new IllegalArgumentException("Calling decreaseKey() with given argument would not strictly decrease the key.");
		keys[i] = key;
		sink(qp[i]);
	}

	public void delete(int i) {
		if (!contains(i)) throw new NoSuchElementException("Index is not in the priority queue.");
		int index = qp[i];
		swap(index, n--);
		swim(index);
		sink(index);
		keys[i] = null;
		qp[i] = -1;
	}

	private boolean less(int i, int j) {
		return keys[pq[i]].compareTo(keys[pq[j]]) < 0;
	}

	private void swap(int i, int j) {
		int swap = pq[i];
		pq[i] = pq[j];
		pq[j] = swap;
		qp[pq[i]] = i;
		qp[pq[j]] = j;
	}

	private void swim(int k) {
		while (k > 1 && less(k/2, k)) {
			swap(k, k/2);
			k = k/2;
		}
	}

	private void sink(int k) {
		while (2*k <= n) {
			int j = 2*k;
			if (j < n && less(j, j+1)) j++;
			if (!less(k, j)) break;
			swap(k, j);
			k = j;
		}
	}

	public Iterator<Integer> iterator() {
		return new HeapIterator();
	}
	
	private class HeapIterator implements Iterator<Integer> {
		private IndexMaxPQ<T> copy;
		public HeapIterator() {
			copy = new IndexMaxPQ<T>(pq.length - 1);
			for (int i = 1; i <= n; i++)
				copy.insert(pq[i], keys[pq[i]]);
		}
		public boolean hasNext()  { return !copy.isEmpty();                     }
		public Integer next() {
			if (!hasNext()) throw new NoSuchElementException();
			return copy.delMax();
		}
		public void remove()      { throw new UnsupportedOperationException();  }
	}

	// TESTS ========================================================
	public static void main(String[] args) {
		// insert a bunch of strings
		String[] strings = { "it", "was", "the", "best", "of", "times", "it", "was", "the", "worst" };
		IndexMaxPQ<String> pq = new IndexMaxPQ<String>(strings.length);
		for (int i = 0; i < strings.length; i++) pq.insert(i, strings[i]);
		// print each key using the iterator
		for (int i : pq) System.out.println(i + " " + strings[i]);
		System.out.println();
		// increase or decrease the key
		for (int i = 0; i < strings.length; i++) {
			if (RandomN.getRandomDouble() < 0.5)
				pq.increaseKey(i, strings[i] + strings[i]);
			else
				pq.decreaseKey(i, strings[i].substring(0, 1));
		}
		// delete and print each key
		while (!pq.isEmpty()) {
			String key = pq.maxKey();
			int i = pq.delMax();
			System.out.println(i + " " + key);
		}
		System.out.println();
		// reinsert the same strings
		for (int i = 0; i < strings.length; i++) pq.insert(i, strings[i]);
		// delete them in random order
		int[] perm = new int[strings.length];
		for (int i = 0; i < strings.length; i++) perm[i] = i;
		KnuthShuffle.shuffle(perm);
		for (int i = 0; i < perm.length; i++) {
			String key = pq.keyOf(perm[i]);
			pq.delete(perm[i]);
			System.out.println(perm[i] + " " + key);
		}
	}
}
