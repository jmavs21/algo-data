package searching;

import java.util.NoSuchElementException;

import fundamentals.Queue;

/**
 * Binary Search Tree (unbalanced) of generic key-value pairs.
 * 
 * Initialization: O(1)
 * Operations:
 *     get, put, contains, deleteMin, deleteMax, delete, min, max ceiling, floor, select, rank: O(n) worst case (umbalance).
 *     size, isEmpty. O(1)
 */
public class BST<K extends Comparable<K>, V> 
{
	private Node root;

	private class Node {
		private K key;
		private V value;
		private Node left, right;
		private int size;
		public Node(K key, V value, int size) {
			this.key = key;
			this.value = value;
			this.size = size;
		}
	}

	public boolean isEmpty() {
		return size() == 0;
	}

	public int size() {
		return size(root);
	}
	private int size(Node x) {
		if(x == null) return 0;
		return x.size;
	}

	public V get(K key) {
		if(key == null) throw new IllegalArgumentException("Calls get() with a null key.");
		return get(root, key);
	}
	private V get(Node x, K key) {
		if(x == null) return null;
		int cmp = key.compareTo(x.key);
		if     (cmp < 0) return get(x.left, key);
		else if(cmp > 0) return get(x.right, key);
		else             return x.value;
	}

	public boolean contains(K key) {
		if(key == null) throw new IllegalArgumentException("Argument to contains() is null.");
		return get(key) != null;
	}

	public void put(K key, V value) {
		if(key == null) throw new IllegalArgumentException("Calls put() with a null key.");
		if(value == null) {
			delete(key);
			return;
		}
		root = put(root, key, value);
	}
	private Node put(Node x, K key, V value) {
		if(x == null) return new Node(key, value, 1);
		int cmp = key.compareTo(x.key);
		if     (cmp < 0) x.left = put(x.left, key, value);
		else if(cmp > 0) x.right = put(x.right, key, value);
		else             x.value = value;
		x.size = 1 + size(x.left) + size(x.right);
		return x;
	}

	public K min() {
		if(isEmpty()) throw new NoSuchElementException("Called min() with empty BST.");
		return min(root).key;
	}
	private Node min(Node x) {
		if(x.left == null) return x;
		else return min(x.left);
	}

	public K max() {
		if(isEmpty()) throw new NoSuchElementException("Called max() with empty BST.");
		return max(root).key;
	}
	private Node max(Node x) {
		if(x.right == null) return x;
		else return max(x.right);
	}

	public void deleteMin() {
		if(isEmpty()) throw new NoSuchElementException("BST underflow.");
		root = deleteMin(root);
	}
	private Node deleteMin(Node x) {
		if(x.left == null) return x.right;
		x.left = deleteMin(x.left);
		x.size = 1 + size(x.left) + size(x.right);
		return x;
	}

	public void deleteMax() {
		if(isEmpty()) throw new NoSuchElementException("BST underflow.");
		root = deleteMax(root);
	}
	private Node deleteMax(Node x) {
		if(x.right == null) return x.left;
		x.right = deleteMax(x.right);
		x.size = 1 + size(x.left) + size(x.right);
		return x;
	}

	public void delete(K key) {
		if(key == null) throw new IllegalArgumentException("Calls delete() with a null key.");
		root = delete(root, key);
	}
	private Node delete(Node x, K key) {
		if(x == null) return null;
		int cmp = key.compareTo(x.key);
		if     (cmp < 0) x.left = delete(x.left, key);
		else if(cmp > 0) x.right = delete(x.right, key);
		else {
			if(x.left == null)  return x.right;
			if(x.right == null) return x.left;
			Node tmp = x;
			x = min(tmp.right);
			x.right = deleteMin(tmp.right);
			x.left = tmp.left;
		}
		x.size = 1 + size(x.left) + size(x.right);
		return x;
	}

	public K floor(K key) {
		if(key == null) throw new IllegalArgumentException("Argument to floor() is null.");
		if(isEmpty()) throw new NoSuchElementException("Calls floor() with empty BST.");
		Node x = floor(root, key);
		if(x != null) return x.key;
		else return null;
	}
	private Node floor(Node x, K key) {
		if(x == null) return null;
		int cmp = key.compareTo(x.key);
		if(cmp == 0) return x;
		if(cmp < 0) return floor(x.left, key);
		Node tmp = floor(x.right, key);
		if(tmp != null) return tmp;
		else return x;
	}

	public K ceiling(K key) {
		if(key == null) throw new IllegalArgumentException("Argument to ceiling() is null.");
		if(isEmpty()) throw new NoSuchElementException("Calls ceiling() with empty BST.");
		Node x = ceiling(root, key);
		if(x != null) return x.key;
		else return null;
	}
	private Node ceiling(Node x, K key) {
		if(x == null) return null;
		int cmp = key.compareTo(x.key);
		if(cmp == 0) return x;
		if(cmp > 0) return ceiling(x.right, key);
		Node tmp = ceiling(x.left, key);
		if(tmp != null) return tmp;
		else return x;
	}
	
	public int rank(K key) {
		if(key == null) throw new IllegalArgumentException("Argument to rank() is null.");
		return rank(root, key);
	}
	private int rank(Node x, K key) {
		if(x == null) return 0;
		int cmp = key.compareTo(x.key);
		if     (cmp < 0) return rank(x.left, key);
		else if(cmp > 0) return 1 + size(x.left) + rank(x.right, key);
		else             return size(x.left);
	}

	public K select(int r) {
		if (r < 0 || r >= size()) throw new IllegalArgumentException("Argument to select() is invalid: " + r);
		return select(root, r).key;
	}
	private Node select(Node x, int r) {
		int sizeL = size(x.left);
		if     (r < sizeL) return select(x.left, r);
		else if(r > sizeL) return select(x.right, r-sizeL-1);
		else               return x;
	}

	public int countRange(K lo, K hi) {
		if(lo == null) throw new IllegalArgumentException("First argument to keys() is null.");
		if(hi == null) throw new IllegalArgumentException("Second argument to keys() is null.");
		if(lo.compareTo(hi) > 0) return 0;
		if(contains(hi)) return rank(hi) - rank(lo) + 1;
		else             return rank(hi) - rank(lo);
	}

	public int height() {
		return height(root);
	}
	private int height(Node x) {
		if(x == null) return -1;
		return 1 + Math.max(height(x.left), height(x.right));
	}

	public Iterable<K> keys(){
		if(isEmpty()) return new Queue<K>();
		return keys(min(), max());
	}
	public Iterable<K> keys(K lo, K hi) {
		if(lo == null) throw new IllegalArgumentException("First argument to keys() is null.");
		if(hi == null) throw new IllegalArgumentException("Second argument to keys() is null.");
		Queue<K> queue = new Queue<K>();
		keys(root, queue, lo, hi);
		return queue;
	} 
	private void keys(Node x, Queue<K> queue, K lo, K hi) {
		if(x == null) return;
		int cmpLo = lo.compareTo(x.key);
		int cmpHi = hi.compareTo(x.key);
		if(cmpLo < 0) keys(x.left, queue, lo, hi);
		if(cmpLo <= 0 && cmpHi >= 0) queue.enqueue(x.key);
		if(cmpHi > 0) keys(x.right, queue, lo, hi);
	}

	public Iterable<K> levelOrder() {
		Queue<K> keys = new Queue<K>();
		Queue<Node> nodes = new Queue<Node>();
		nodes.enqueue(root);
		while (!nodes.isEmpty()) {
			Node x = nodes.dequeue();
			if(x == null) continue;
			keys.enqueue(x.key);
			nodes.enqueue(x.left);
			nodes.enqueue(x.right);
		}
		return keys;
	}

	private boolean isBST() {
		return isBST(root, null, null);
	}
	private boolean isBST(Node x, K lo, K hi) {
		if(x == null) return true;
		if(lo != null && x.key.compareTo(lo) >= 0) return false;
		if(hi != null && x.key.compareTo(hi) <= 0) return false;
		return isBST(x.left, x.key, hi) && isBST(x.right, lo, x.key);
	}

	// TESTS ========================================================
	public static void main(String[] args) {
		BST<String, Integer> bst = new BST<>();
		bst.put("2", 1);
		bst.put("5", 2);
		bst.put("3", 3);
		bst.put("6", 4);
		bst.put("7", 5);
		bst.put("1", 6);
		System.out.println("BST level order: " + bst.levelOrder());
		System.out.println("keys: ");
		for (String s : bst.keys())
            System.out.println(s + " " + bst.get(s));
		System.out.println("contains '3' = " + bst.contains("3"));
		System.out.println("get '3' = " + bst.get("3"));
		System.out.println("root size = " + bst.size());
		System.out.println("root height = " + bst.height());
		System.out.println("max = " + bst.max());
		System.out.println("min = " + bst.min());
		System.out.println("floor '4' = " + bst.floor("4"));
		System.out.println("ceiling '4' = " + bst.ceiling("4"));
		System.out.println("count range 2 to 5 = " + bst.countRange("2", "5"));
		System.out.println("level order: " + bst.levelOrder());
		System.out.println("             : 0 1 2 3 4 5 ");
		System.out.println("keys in order: " + bst.keys());
		System.out.println("rank '1' = " + bst.rank("1"));
		System.out.println("rank '2' = " + bst.rank("2"));
		System.out.println("rank '3' = " + bst.rank("3"));
		System.out.println("rank '4' = " + bst.rank("4"));
		System.out.println("rank '5' = " + bst.rank("5"));
		System.out.println("rank '6' = " + bst.rank("6"));
		System.out.println("rank '7' = " + bst.rank("7"));
		System.out.println("select 1 = " + bst.select(1));
		System.out.println("select 2 = " + bst.select(2));
		System.out.println("select 3 = " + bst.select(3));
		System.out.println("select 4 = " + bst.select(4));
		System.out.println("select 5 = " + bst.select(5));
		bst.deleteMax();
		System.out.println("deleteMax: " + bst.levelOrder());
		bst.deleteMin();
		System.out.println("deleteMin: " + bst.levelOrder());
		bst.delete("5") ;
		System.out.println("delete 5: " + bst.levelOrder());
		bst.put("0", 0);
		System.out.println("put '0' = " + bst.levelOrder());
		System.out.println("is BST: " + bst.isBST());
		System.out.println("keys: ");
		for (String s : bst.keys()) {        	
			System.out.println(s + " " + bst.get(s));
		}
	}
}
