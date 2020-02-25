package strings;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

import fundamentals.Queue;

/**
 * Implementation of a R-way Trie, where R is 256.
 * 
 * Extra space: O(N*W*R)
 *   where:
 *  	N is the number of keys.
 *  	R is the number of chars in alphabet.
 *  	W is the length of the word.
 * 
 * Initialization: O(1)
 * Operations:
 *     get, contains, put, delete, longestPrefixOf, keysWithPrefix, keysThatMatch: O(length of key) worst case.
 *     size, isEmpty: O(1)
 *     
 * NOTE: O(W) hit match, O(log W) hit miss.
 */
public class TrieST<V> 
{
	private static final int R = 256; // extended ASCII
	private Node root;
	private int n;

	private static class Node {
		private Object value;
		private Node[] next = new Node[R];
	}

	public int size() {
		return n;
	}

	public boolean isEmpty() {
		return size() == 0;
	}

	public boolean contains(String key) {
		if (key == null) throw new IllegalArgumentException("argument to contains() is null");
		return get(root, key, 0) != null;
	}
	
	@SuppressWarnings("unchecked")
	public V get(String key) {
		if (key == null) throw new IllegalArgumentException("argument to get() is null");
		Node x = get(root, key, 0);
		if (x == null) return null;
		return (V) x.value;
	}
	private Node get(Node x, String key, int d) {
		if (x == null) return null;
		if (d == key.length()) return x;
		char c = key.charAt(d);
		return get(x.next[c], key, d+1);
	}

	public void put(String key, V val) {
		if (key == null) throw new IllegalArgumentException("first argument to put() is null");
		if (val == null) delete(key);
		root = put(root, key, val, 0);
	}
	private Node put(Node x, String key, V val, int d) {
		if(x == null) x = new Node();
		if(d == key.length()) {
			if(x.value == null) n++;
			x.value = val;
			return x;
		}		
		char c = key.charAt(d);
		x.next[c] = put(x.next[c], key, val, d+1);
		return x;
	}

	public void delete(String key) {
		if (key == null) throw new IllegalArgumentException("argument to delete() is null");
		root = delete(root, key, 0);
	}
	private Node delete(Node x, String key, int d) {
		if(x == null) return null;
		if(d == key.length()) {
			if(x.value != null) n--;
			x.value = null;
		} else {
			char c = key.charAt(d);
			x.next[c] = delete(x.next[c], key, d+1);
		}
		if(x.value != null) return x;
		for(char c = 0; c < R; c++) {
			if(x.next[c] != null) 
				return x;
		}
		return null;
	}

	public Iterable<String> keys(){
		return keysWithPrefix("");
	}
	public Iterable<String> keysWithPrefix(String prefix){
		Queue<String> queue = new Queue<String>();
		keysWithPrefix(get(root, prefix, 0), prefix, queue);
		return queue;
	}
	private void keysWithPrefix(Node x, String prefix, Queue<String> queue) {
		if(x == null) return;
		if(x.value != null) queue.enqueue(prefix);
		for(char c = 0; c < R; c++) 
			keysWithPrefix(x.next[c], prefix + c, queue);
	}

	public Iterable<String> keysThatMatch(String pattern){
		Queue<String> queue = new Queue<String>();
		keysThatMatch(root, "", pattern, queue, 0);
		return queue;
	}
	private void keysThatMatch(Node x, String prefix, String pattern, Queue<String> queue, int d) {
		if(x == null) 
			return;
		if(d == pattern.length() && x.value != null) 
			queue.enqueue(prefix);
		if(d == pattern.length()) 
			return;
		char next = pattern.charAt(d);
		for(char c = 0; c < R; c++) {
			if(c == next || '.' == next) {
				keysThatMatch(x.next[c], prefix + c, pattern, queue, d+1);
			}
		}
	}

	public String longestPrefixOf(String query) {
		if (query == null) throw new IllegalArgumentException("argument to longestPrefixOf() is null");
		int length = longestPrefixOf(root, query, 0, -1);
		if (length == -1) return null;
		return query.substring(0, length);
	}
	private int longestPrefixOf(Node x, String query, int d, int length) {
		if(x == null) return length;
		if(x.value != null) length = d;
		if(d == query.length()) return length;
		char c = query.charAt(d);
		return longestPrefixOf(x.next[c], query, d+1, length);
	}

	public static void main(String[] args) throws FileNotFoundException {
		Scanner sc = new Scanner(new FileReader("resources/strings/shellsST.txt"));
		TrieST<Integer> trie = new TrieST<Integer>();
		int i = 0;
		while(sc.hasNext()) {
			String key = sc.next();
			trie.put(key, i);
			i++;
		}
		sc.close();
		System.out.println("keys(\"\"):");
		for (String key : trie.keys()) {
			System.out.println(key + " " + trie.get(key));
		}
		System.out.println();
		System.out.println("longestPrefixOf(\"shellsort\"):");
		System.out.println(trie.longestPrefixOf("shellsort"));
		System.out.println();
		System.out.println("keysWithPrefix(\"shor\"):");
		for (String s : trie.keysWithPrefix("shor"))
			System.out.println(s);
		System.out.println();
		System.out.println("keysThatMatch(\".he.l.\"):");
		for (String s : trie.keysThatMatch(".he.l."))
			System.out.println(s);
	}
}