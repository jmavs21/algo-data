package searching;

import java.util.Scanner;

import fundamentals.Queue;

/**
 * HashST Separate Chaining for generic key-value pairs.
 * 
 * Initialization: O(1)
 * Operations:
 *     get, contains, put, delete: O(1) subject to uniform hashing assumption.
 *     size, isEmpty. O(1)
 */
public class HashST<K, V> 
{
	private static final int INIT_CAPACITY = 4;

	private int n;
	private int m;
	private LinkedListST<K, V>[] st;

	public HashST() {
		this(INIT_CAPACITY);
	} 

	@SuppressWarnings("unchecked")
	public HashST(int m) {
		this.m = m;
		st = (LinkedListST<K, V>[]) new LinkedListST[m];
		for (int i = 0; i < m; i++) {			
			st[i] = new LinkedListST<K, V>();
		}
	} 
	
	public int size() {
		return n;
	} 

	public boolean isEmpty() {
		return size() == 0;
	}

	private void resize(int chains) {
		HashST<K, V> tmp = new HashST<K, V>(chains);
		for (int i = 0; i < m; i++) {
			for (K key : st[i].keys()) {
				tmp.put(key, st[i].get(key));
			}
		}
		this.m  = tmp.m;
		this.n  = tmp.n;
		this.st = tmp.st;
	}

	private int hash(K key) {
		return (key.hashCode() & 0x7fffffff) % m; // hash value between 0 and m-1
	} 

	public boolean contains(K key) {
		if (key == null) throw new IllegalArgumentException("Argument to contains() is null.");
		return get(key) != null;
	} 

	public V get(K key) {
		if (key == null) throw new IllegalArgumentException("Argument to get() is null.");
		int i = hash(key);
		return st[i].get(key);
	} 

	public void put(K key, V value) {
		if (key == null) throw new IllegalArgumentException("first Argument to put() is null.");
		if (value == null) {
			delete(key);
			return;
		}
		if (n >= 10*m) resize(2*m);
		int i = hash(key);
		if (!st[i].contains(key)) n++;
		st[i].put(key, value);
	} 

	public void delete(K key) {
		if (key == null) throw new IllegalArgumentException("Argument to delete() is null.");
		int i = hash(key);
		if (st[i].contains(key)) n--;
		st[i].delete(key);
		if (m > INIT_CAPACITY && n <= 2*m) resize(m/2);
	} 

	public Iterable<K> keys() {
		Queue<K> queue = new Queue<K>();
		for (int i = 0; i < m; i++) {
			for (K key : st[i].keys()) {				
				queue.enqueue(key);
			}
		}
		return queue;
	} 

	// TESTS ========================================================
	public static void main(String[] args) { 
		System.out.println("Test : S E A R C H E X A M P L E");
		HashST<String, Integer> st = new HashST<String, Integer>();
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
