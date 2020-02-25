package strings;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

import fundamentals.Queue;

/**
 * Implementation of a Ternary search trie (TST).
 * 
 * Extra space: O(N*W)
 *   where:
 *  	N is the number of keys.
 *  	W is the length of the word.
 * 
 * Initialization: O(1)
 * Operations:
 *     get, contains, put, longestPrefixOf, keysWithPrefix, keysThatMatch: O(length of key) worst case.
 *     size, isEmpty: O(1)
 *     
 * NOTE: O(W) hit match, O(log W) hit miss
 */
public class TST<V>
{
	private Node<V> root;
	private int n;

	private static class Node<V> {
		private char c;
		private V value;
		private Node<V> left, mid, right;
	}

	public int size() {
		return n;
	}

	public boolean isEmpty() {
		return size() == 0;
	}

	public boolean contains(String key) {
		if (key == null) throw new IllegalArgumentException("argument to contains() is null");
		return get(key) != null;
	}
	public V get(String key) {
		if (key == null) throw new IllegalArgumentException("calls get() with null argument");
		if (key.length() == 0) throw new IllegalArgumentException("key must have length >= 1");
		Node<V> x = get(root, key, 0);
		if(x == null) return null;
		return x.value;
	}
	private Node<V> get(Node<V> x, String key, int d){
		if(x == null) return null;
		if (key.length() == 0) throw new IllegalArgumentException("key must have length >= 1");
		char c = key.charAt(d);
		if     (c < x.c)              return get(x.left,  key, d);
		else if(c > x.c)              return get(x.right, key, d);
		else if(d < key.length() - 1) return get(x.mid,   key, d+1);
		else                          return x;
	}

	public void put(String key, V value) {
		if (key == null) throw new IllegalArgumentException("calls put() with null key");
		if (!contains(key)) n++;
		root = put(root, key, value, 0);
	}
	private Node<V> put(Node<V> x, String key, V value, int d) {
		char c = key.charAt(d);
		if(x == null) {
			x = new Node<V>();
			x.c = c;
		}
		if     (c < x.c)              x.left  = put(x.left,  key, value, d);
		else if(c > x.c)              x.right = put(x.right, key, value, d);
		else if(d < key.length() - 1) x.mid   = put(x.mid,   key, value, d+1);
		else                          x.value = value;
		return x;
	}

	public Iterable<String> keys(){
		return keysWithPrefix("");
	}
	public Iterable<String> keysWithPrefix(String prefix){
		if (prefix == null) throw new IllegalArgumentException("calls keysWithPrefix() with null argument");
		Queue<String> queue = new Queue<String>();
		if(prefix.equals("")) keysWithPrefix(root, "", queue);
		else {
			Node<V> x = get(root, prefix, 0);
			if(x == null)       return queue;
			if(x.value != null) queue.enqueue(prefix);
			keysWithPrefix(x.mid, prefix, queue);
		}
		return queue;
	}
	private void keysWithPrefix(Node<V> x, String prefix, Queue<String> queue) {
		if(x == null) return;
		keysWithPrefix(x.left, prefix, queue);
		if(x.value != null) queue.enqueue(prefix + x.c);
		keysWithPrefix(x.mid, prefix + x.c, queue);
		keysWithPrefix(x.right, prefix, queue);
	}

	public Iterable<String> keysThatMatch(String pattern) {
		Queue<String> queue = new Queue<String>();
		keysThatMatch(root, new StringBuilder(), 0, pattern, queue);
		return queue;
	}
	private void keysThatMatch(Node<V> x, StringBuilder prefix, int d, String pattern, Queue<String> queue) {
		if (x == null) 
			return;
		char c = pattern.charAt(d);
		if (c == '.' || c < x.c) 
			keysThatMatch(x.left, prefix, d, pattern, queue);
		if (c == '.' || c == x.c) {
			if (d == pattern.length() - 1 && x.value != null) 
				queue.enqueue(prefix.toString() + x.c);
			if (d < pattern.length() - 1) {
				keysThatMatch(x.mid, prefix.append(x.c), d+1, pattern, queue);
				prefix.deleteCharAt(prefix.length() - 1);
			}
		}
		if (c == '.' || c > x.c) 
			keysThatMatch(x.right, prefix, d, pattern, queue);
	}

	public String longestPrefixOf(String query) {
		if (query == null) {
			throw new IllegalArgumentException("calls longestPrefixOf() with null argument");
		}
		if (query.length() == 0) return null;
		int length = 0;
		Node<V> x = root;
		int i = 0;
		while (x != null && i < query.length()) {
			char c = query.charAt(i);
			if      (c < x.c) x = x.left;
			else if (c > x.c) x = x.right;
			else {
				i++;
				if (x.value != null) length = i;
				x = x.mid;
			}
		}
		return query.substring(0, length);
	}

	public static void main(String[] args) throws FileNotFoundException {
		Scanner sc = new Scanner(new FileReader("resources/strings/shellsST.txt"));
		TST<Integer> tst = new TST<Integer>();
		int i = 0;
		while(sc.hasNext()) {
			String key = sc.next();
			tst.put(key, i);
			i++;
		}
		sc.close();
		System.out.println("keys(\"\"):");
		for (String key : tst.keys()) {
			System.out.println(key + " " + tst.get(key));
		}
		System.out.println();
		System.out.println("longestPrefixOf(\"shellsort\"):");
		System.out.println(tst.longestPrefixOf("shellsort"));
		System.out.println();
		System.out.println("keysWithPrefix(\"shor\"):");
		for (String s : tst.keysWithPrefix("shor"))
			System.out.println(s);
		System.out.println();
		System.out.println("keysThatMatch(\".he.l.\"):");
		for (String s : tst.keysThatMatch(".he.l."))
			System.out.println(s);
	}
}