package searching;

import java.util.NoSuchElementException;
import java.util.Scanner;

import fundamentals.Queue;

/**
 * Red Black BST (balanced) of and generic key-value pairs.
 * 
 * Initialization: O(1)
 * Operations:
 *     get, put, contains, deleteMin, deleteMax, delete, min, max ceiling, floor, select, rank: O(log n) worst case.
 *     size, isEmpty. O(1)
 *  
 *  Notes: 
 *      Left-leaning Red Black BST, where all left nodes are less and all right nodes are equal or larger than root node.
 *      No node has 2 red links connected to it.
 *      Every path from root to null link has the same number of black links (perfect black balance).
 *      Red links lean left.
 *              x
 *           //   \
 *          y      z
 *        /  \   // \
 *       a       b
 *     // \     / \
 *       
 */
public class RedBlackBST<K extends Comparable<K>, V> 
{
	private static final boolean RED = true;
	private static final boolean BLACK = false;

	private Node root;

	private class Node {
		private K key;
		private V value;
		private Node left, right;
		private boolean color; // color of parent link
		private int size;
		public Node(K key, V value, boolean color, int size) {
			this.key = key;
			this.value = value;
			this.color = color;
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

	private boolean isRed(Node x) {
		if(x == null) return false; // null links are black
		return x.color == RED;
	}

	/**
	 * Left orientation. Orient a (temporarily) right-leaning red link to lean left.
	 * 			|
	 *          X
	 *        /   \\
	 *       B     C
	 *            / \
	 *           D   E
	 * =============================
	 *          |
	 *          C
	 *        //  \
	 *       X     E
	 *      / \
	 *     B   D
	 */
	private Node rotateLeft(Node x) {
		Node c = x.right;
		x.right = c.left;
		c.left = x;
		c.color = c.left.color;
		c.left.color = RED;
		c.size = x.size;
		x.size = size(x.left) + size(x.right) + 1;
		return c;
	}

	/**
	 * Right orientation. Orient a right-leaning red link to a (temporarily) lean right.
	 *          |
	 *          X
	 *        //  \
	 *       C     B
	 *     // \
	 *     E   D
	 * =============================
	 * 			|
	 *          C
	 *        //  \\
	 *       E     X
	 *            / \
	 *           D   B
	 */
	private Node rotateRight(Node x) {
		Node c = x.left;
		x.left = c.right;
		c.right = x;
		c.color = c.right.color;
		c.right.color = RED;
		c.size = x.size;
		x.size = size(x.left) + size(x.right) + 1;
		return c;
	}

	/**
	 * Recolor to split a (temporary) 4-node (BAC).
	 *          |
	 *          X
	 *        //  \\
	 *       B     C
	 *      / \   / \
	 *     D   E F   G
	 * =======================
	 *          ||
	 *          X
	 *        /   \
	 *       B     C
	 *      / \   / \
	 *     D   E F   G
	 */
	private void flipColors(Node x) {
		x.color = !x.color;
		x.left.color = !x.left.color;
		x.right.color = !x.right.color;
	}

	public V get(K key) {
		if(key == null) throw new IllegalArgumentException("Argument to get() is null.");
		return get(root, key);
	}
	private V get(Node x, K key) {
		while(x != null) {
			int cmp = key.compareTo(x.key);
			if     (cmp < 0) x = x.left;
			else if(cmp > 0) x = x.right;
			else             return x.value;
		}
		return null;
	}

	public boolean contains(K key) {
		return get(key) != null;
	}

	/**
	 * Only 3 cases because of recursion path on nodes touched:
	 * 1) left.black && right.red ==> rotate left (returns C)
	 *          |
	 *          A
	 *        /   \\
	 *       B     C
	 * ============================
	 * 2) left.red && left.left.red ==> rotate right (returns A)
	 *          |
	 *          C
	 *        //  
	 *       A     
	 *     //   
	 *    B   
	 * ============================
	 * 3) left.red && right.red ==> flip colors
	 *          |
	 *          A
	 *        //  \\
	 *       B     C
	 */
	public void put(K key, V value) {
		if(key == null) throw new IllegalArgumentException("First argument to put() is null.");
		if(value == null) {
			delete(key);
			return;
		}
		root = put(root, key, value);
		root.color = BLACK;
	}
	private Node put(Node x, K key, V value) {
		if(x == null) return new Node(key, value, RED, 1); // new node with red link
		int cmp = key.compareTo(x.key);
		if     (cmp < 0) x.left = put(x.left, key, value);
		else if(cmp > 0) x.right = put(x.right, key, value);
		else             x.value = value;
		if(isRed(x.right) && !isRed(x.left))    x = rotateLeft(x);
		if(isRed(x.left) && isRed(x.left.left)) x = rotateRight(x);
		if(isRed(x.left) && isRed(x.right))     flipColors(x);
		x.size = 1 + size(x.left) + size(x.right);
		return x;
	}

	public K min() {
		if(isEmpty()) throw new NoSuchElementException("Calls min() with empty RedBlackBST.");
		return min(root).key;
	}
	private Node min(Node x) {
		if(x.left == null) return x;
		return min(x.left);
	}

	public K max() {
		if(isEmpty()) throw new NoSuchElementException("Calls max() with empty RedBlackBST.");
		return max(root).key;
	}
	private Node max(Node x) {
		if(x.right == null) return x;
		return max(x.right);
	}

	private Node moveRedLeft(Node x) {
		flipColors(x);
		if (isRed(x.right.left)) { 
			x.right = rotateRight(x.right);
			x = rotateLeft(x);
			flipColors(x);
		}
		return x;
	}

	private Node moveRedRight(Node x) {
		flipColors(x);
		if (isRed(x.left.left)) { 
			x = rotateRight(x);
			flipColors(x);
		}
		return x;
	}

	private Node balance(Node x) {
		if (isRed(x.right))                      x = rotateLeft(x);
		if (isRed(x.left) && isRed(x.left.left)) x = rotateRight(x);
		if (isRed(x.left) && isRed(x.right))     flipColors(x);
		x.size = size(x.left) + size(x.right) + 1;
		return x;
	}

	public void deleteMin() {
		if (isEmpty()) throw new NoSuchElementException("RedBlackBST underflow.");
		if (!isRed(root.left) && !isRed(root.right)) root.color = RED;
		root = deleteMin(root);
		if (!isEmpty()) root.color = BLACK;
	}
	private Node deleteMin(Node x) { 
		if (x.left == null) return null;
		if (!isRed(x.left) && !isRed(x.left.left)) x = moveRedLeft(x);
		x.left = deleteMin(x.left);
		return balance(x);
	}

	public void deleteMax() {
		if (isEmpty()) throw new NoSuchElementException("RedBlackBST underflow.");
		if (!isRed(root.left) && !isRed(root.right)) root.color = RED;
		root = deleteMax(root);
		if (!isEmpty()) root.color = BLACK;
	}
	private Node deleteMax(Node x) { 
		if (isRed(x.left)) x = rotateRight(x);
		if (x.right == null) return null;
		if (!isRed(x.right) && !isRed(x.right.left)) x = moveRedRight(x);
		x.right = deleteMax(x.right);
		return balance(x);
	}

	public void delete(K key) { 
		if (key == null) throw new IllegalArgumentException("Argument to delete() is null.");
		if (!contains(key)) return;
		if (!isRed(root.left) && !isRed(root.right)) root.color = RED;
		root = delete(root, key);
		if (!isEmpty()) root.color = BLACK;
	}
	private Node delete(Node x, K key) { 
		if (key.compareTo(x.key) < 0)  {
			if (!isRed(x.left) && !isRed(x.left.left)) x = moveRedLeft(x);
			x.left = delete(x.left, key);
		}
		else {
			if (isRed(x.left)) x = rotateRight(x);
			if (key.compareTo(x.key) == 0 && (x.right == null)) return null;
			if (!isRed(x.right) && !isRed(x.right.left)) x = moveRedRight(x);
			if (key.compareTo(x.key) == 0) {
				Node tmp = min(x.right);
				x.key = tmp.key;
				x.value = tmp.value;
				x.right = deleteMin(x.right);
			}
			else x.right = delete(x.right, key);
		}
		return balance(x);
	}

	public int height() {
		return height(root);
	}
	private int height(Node x) {
		if(x == null) return -1;
		return 1 + Math.max(height(x.left), height(x.right));
	}

	public K floor(K key) {
		if (key == null) throw new IllegalArgumentException("Argument to floor() is null.");
		if (isEmpty()) throw new NoSuchElementException("Calls floor() with empty RedBlackBST.");
		Node x = floor(root, key);
		if(x == null) return null;
		return x.key;
	}
	private Node floor(Node x, K key) {
		if(x == null) return null;
		int cmp = key.compareTo(x.key);
		if(cmp < 0)  return floor(x.left, key);
		if(cmp == 0) return x;
		Node tmp = floor(x.right, key);
		if(tmp != null) return tmp;
		return x;
	}

	public K ceiling(K key) {
		if (key == null) throw new IllegalArgumentException("Argument to ceiling() is null.");
		if (isEmpty()) throw new NoSuchElementException("Calls ceiling() with empty RedBlackBST.");
		Node x = ceiling(root, key);
		if(x == null) return null;
		return x.key;
	}
	private Node ceiling(Node x, K key) {
		if(x == null) return null;
		int cmp = key.compareTo(x.key);
		if(cmp > 0)  return ceiling(x.right, key);
		if(cmp == 0) return x;
		Node tmp = ceiling(x.left, key);
		if(tmp != null) return tmp;
		return x;
	}

	public int rank(K key) {
		if (key == null) throw new IllegalArgumentException("Argument to rank() is null.");
		return rank(root, key);
	}
	private int rank(Node x, K key) {
		if(x == null) return 0;
		int cmp = key.compareTo(x.key);
		if(cmp < 0) return rank(x.left, key);
		else if(cmp > 0) return 1 + size(x.left) + rank(x.right, key);
		else return size(x.left);
	}

	public K select(int r) {
		if(r < 0 || r >= size()) throw new IllegalArgumentException("Argument to select() is invalid: " + r);
		return select(root, r).key;
	}
	private Node select(Node x, int r) {
		int sizeL = size(x.left);
		if(r < sizeL) return select(x.left, r);
		else if(r > sizeL) return select(x.right, r-sizeL-1);
		else return x;
	}

	public int countRange(K lo, K hi) {
		if(lo == null) throw new IllegalArgumentException("First argument to keys() is null.");
		if(hi == null) throw new IllegalArgumentException("Second argument to keys() is null.");
		if(lo.compareTo(hi) > 0) return 0;
		if(contains(hi)) return rank(hi) - rank(lo) + 1;
		else             return rank(hi) - rank(lo);
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
		if(lo != null && x.key.compareTo(lo) <= 0) return false;
		if(hi != null && x.key.compareTo(hi) >= 0) return false;
		return isBST(x.left, lo, x.key) && isBST(x.right, x.key, hi);
	}

	// TESTS ========================================================
	public static void main(String[] args) {
		RedBlackBST<String, Integer> rbBst = new RedBlackBST<>();
		rbBst.put("2", 1);
		rbBst.put("5", 2);
		rbBst.put("3", 3);
		rbBst.put("6", 4);
		rbBst.put("7", 5);
		rbBst.put("1", 6);
		System.out.println("BST level order: " + rbBst.levelOrder());
		for (String s : rbBst.keys())
            System.out.println(s + " " + rbBst.get(s));
		System.out.println("contains '3' = " + rbBst.contains("3"));
		System.out.println("get '3' = " + rbBst.get("3"));
		System.out.println("root size = " + rbBst.size());
		System.out.println("root height = " + rbBst.height());
		System.out.println("max = " + rbBst.max());
		System.out.println("min = " + rbBst.min());
		System.out.println("floor '4' = " + rbBst.floor("4"));
		System.out.println("ceiling '4' = " + rbBst.ceiling("4"));
		System.out.println("count range 2 to 5 = " + rbBst.countRange("2", "5"));
		System.out.println("level order: " + rbBst.levelOrder());
		System.out.println("             : 0 1 2 3 4 5 ");
		System.out.println("keys in order: " + rbBst.keys());
		System.out.println("rank '1' = " + rbBst.rank("1"));
		System.out.println("rank '2' = " + rbBst.rank("2"));
		System.out.println("rank '3' = " + rbBst.rank("3"));
		System.out.println("rank '4' = " + rbBst.rank("4"));
		System.out.println("rank '5' = " + rbBst.rank("5"));
		System.out.println("rank '6' = " + rbBst.rank("6"));
		System.out.println("rank '7' = " + rbBst.rank("7"));
		System.out.println("select 1 = " + rbBst.select(1));
		System.out.println("select 2 = " + rbBst.select(2));
		System.out.println("select 3 = " + rbBst.select(3));
		System.out.println("select 4 = " + rbBst.select(4));
		System.out.println("select 5 = " + rbBst.select(5));
		rbBst.deleteMax();
		System.out.println("deleteMax: " + rbBst.levelOrder());
		rbBst.deleteMin();
		System.out.println("deleteMin: " + rbBst.levelOrder());
		rbBst.delete("5") ;
		System.out.println("delete 5: " + rbBst.levelOrder());
		rbBst.put("0", 0);
		System.out.println("put '0' = " + rbBst.levelOrder());
		System.out.println("is BST: " + rbBst.isBST());
		System.out.println("keys: ");
		for (String s : rbBst.keys()) {        	
			System.out.println(s + " " + rbBst.get(s));
		}
		
		System.out.println("Test : S E A R C H E X A M P L E");
		RedBlackBST<String, Integer> st = new RedBlackBST<String, Integer>();
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
