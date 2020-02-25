package sorting;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Min Priority Queue implementation with binary tree heap (array) and immutable objects as keys.
 * 
 * Initialization: O(n) where n is the number of items (capacity).
 * Operations:
 *     insert, delMin: O(log N) amortized time.
 *     min, size, isEmpty: O(1)
 *     
 * @author joaquin
 */
public class MinPQ<T extends Comparable<T>> implements Iterable<T>
{
	private T[] pq; // stores items from index 1 to n (0 not used)
	private int n; // items on pq

	@SuppressWarnings("unchecked")
	public MinPQ(int capacity) {
		pq = (T[]) new Comparable[capacity + 1];
		n = 0;
	}

	@SuppressWarnings("unchecked")
	public MinPQ(T[] keys) {
		n = keys.length;
		pq = (T[]) new Comparable[keys.length + 1];
		for (int i = 0; i < n; i++) {        	
			pq[i+1] = keys[i];
		}
		for (int k = n/2; k >= 1; k--) {        	
			sink(k);
		}
	}

	public MinPQ() {
		this(1);
	}

	public int size() {
		return n;
	}

	public boolean isEmpty() {
		return n == 0;
	}

	public T min() {
		if(isEmpty()) throw new NoSuchElementException("Priority queue underflow.");
		return pq[1];
	}

	public void insert(T key) {
		if (n+1 == pq.length) resize(2 * pq.length);
		pq[++n] = key;
		swim(n);
	}

	public T deleteMin() {
		if (isEmpty()) throw new NoSuchElementException("Priority queue underflow.");
		T min = pq[1];
		swap(1, n--);
		pq[n+1] = null; // prevent loitering, put it for garbage collector
		sink(1);
		if (n > 0 && (n+1 == (pq.length)/4)) resize(pq.length/2);
		return min;
	}

	private void swim(int k) {
		while(k > 1 && pq[k].compareTo(pq[k/2]) < 0) { // while k less than parent and not at root
			swap(k, k/2);
			k = k/2;
		}
	}

	private void sink(int k) {
		while(2*k <= n) { 
			int j = 2*k;
			if(j < n && pq[j+1].compareTo(pq[j]) < 0) j++;
			if(pq[k].compareTo(pq[j]) < 0) 
				break;
			swap(k, j);
			k = j;
		}
	}

	@Override
	public Iterator<T> iterator() {
		return new HeapIterator();
	}
	private class HeapIterator implements Iterator<T> {
		private MinPQ<T> copy;
		public HeapIterator() {
			copy = new MinPQ<T>(size());
			for (int i = 1; i <= n; i++) {				
				copy.insert(pq[i]);
			}
		}
		public boolean hasNext()  { return !copy.isEmpty(); }
		public T next() {
			if (!hasNext()) throw new NoSuchElementException();
			return copy.deleteMin();
		}
		public void remove()      { throw new UnsupportedOperationException();  }
	}

	@SuppressWarnings("unchecked")
	private void resize(int capacity) {
		T[] tmp = (T[]) new Comparable[capacity];
		for (int i = 1; i <= n; i++) {
			tmp[i] = pq[i];
		}
		pq = tmp;
	}

	private void swap(int i, int j) {
		T temp = pq[i];
		pq[i] = pq[j];
		pq[j] = temp;
	}

	public String toString() {
		StringBuilder s = new StringBuilder();
		for(T i : this) {			
			s.append(i);
			s.append(" ");
		}
		return s.toString();
	}

	public static void main(String[] args) {
		String test = "P Q E - X A M - P L E -";
		MinPQ<String> pq = new MinPQ<String>();
		String[] arr = test.split("\\s");
		for(int i = 0; i < arr.length; i++) {
			String item = arr[i];
			if (!item.equals("-")) pq.insert(item);
			else if (!pq.isEmpty())  System.out.println("delMin: " + pq.deleteMin() + " ");
			System.out.println(pq);
		}
	}
}