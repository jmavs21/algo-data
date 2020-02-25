package searching;

import java.util.Scanner;

import fundamentals.Queue;

/**
 * Linked list of generic key-value pairs, implemented with a single linked list and sequential search.
 * 
 * Initialization: O(1)
 * Operations: Where n is the number of items.
 *     put, delete: O(n).
 *     get, contains: O(n) in the worst case.
 *     size, isEmpty: O(1)
 */
public class LinkedListST<K, V> 
{
	private int n;
	private Node first;

	private class Node {
		private K key;
		private V value;
		private Node next;
		public Node(K key, V value, Node next)  {
			this.key = key;
			this.value = value;
			this.next = next;
		}
	}

	public int size() {
		return n;
	}

	public boolean isEmpty() {
		return size() == 0;
	}

	public boolean contains(K key) {
		if (key == null) throw new IllegalArgumentException("Argument to contains() is null.");
		return get(key) != null;
	}

	public V get(K key) {
		if (key == null) throw new IllegalArgumentException("Argument to get() is null."); 
		for (Node x = first; x != null; x = x.next) {
			if (key.equals(x.key)) return x.value;
		}
		return null;
	}

	public void put(K key, V value) {
		if (key == null) throw new IllegalArgumentException("First argument to put() is null."); 
		if (value == null) {
			delete(key);
			return;
		}
		for (Node x = first; x != null; x = x.next) {
			if (key.equals(x.key)) {
				x.value = value;
				return;
			}
		}
		first = new Node(key, value, first);
		n++;
	}

	public void delete(K key) {
		if (key == null) throw new IllegalArgumentException("Argument to delete() is null."); 
		first = delete(first, key);
	}

	private Node delete(Node x, K key) {
		if (x == null) return null;
		if (key.equals(x.key)) {
			n--;
			return x.next;
		}
		x.next = delete(x.next, key);
		return x;
	}

	public Iterable<K> keys()  {
		Queue<K> queue = new Queue<K>();
		for (Node x = first; x != null; x = x.next) {			
			queue.enqueue(x.key);
		}
		return queue;
	}

	// TESTS ========================================================
	public static void main(String[] args) {
		System.out.println("Test : S E A R C H E X A M P L E");
		LinkedListST<String, Integer> st = new LinkedListST<String, Integer>();
		Scanner sc = new Scanner("S E A R C H E X A M P L E");
		for (int i = 0; sc.hasNext(); i++) {
			String key = sc.next();
			st.put(key, i);
		}
		for (String s : st.keys())
			System.out.println(s + " " + st.get(s));
		sc.close();
	}
}
