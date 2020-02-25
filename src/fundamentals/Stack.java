package fundamentals;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Stack implementation with a single linked list representing a last-in-first-out (LIFO) of generic items.
 * 
 *  Operations: where n is the number of elements in list.
 *      push, pop, peek, size, isEmpty: O(1) in the worst case.
 */
public class Stack<T> implements Iterable<T>
{
	private Node<T> first;
	private int n;

	private static class Node<T> {
		private T item;
		private Node<T> next;
	}
	
	public Stack() {
		first = null;
		n = 0;
	}

	public boolean isEmpty() {
		return first == null;
	}

	public int size() {
		return n;
	}

	public void push(T item) {
		Node<T> node = new Node<T>();
		node.item = item;
		node.next = first;
		first = node;
		n++;
	}

	public T pop() {
		if(isEmpty()) throw new NoSuchElementException("Stack underflow.");
		T item = first.item;
		first = first.next;
		n--;
		return item;
	}

	public T peek() {
		if(isEmpty()) throw new NoSuchElementException("Stack underflow.");
		return first.item;
	}

	public String toString() {
		StringBuilder s = new StringBuilder();
		for(T i : this) {			
			s.append(i);
			s.append(" ");
		}
		return s.toString();
	}

	@Override
	public Iterator<T> iterator() {
		return new ListIterator();
	}
	private class ListIterator implements Iterator<T> {
		private Node<T> current = first;
		public boolean hasNext() {
			return current != null;
		}
		public T next() {
			if(!hasNext()) throw new NoSuchElementException();
			T item = current.item;
			current = current.next;
			return item;
		}
		public void remove() { throw new UnsupportedOperationException(); }
	}

	// TESTS ========================================================
	public static void main(String[] args) {
		String test = "1 2 3 4 5 - 6 - - 7 - - - 8";
		Stack<String> stack1 = new Stack<String>();
		String[] items = test.split("\\s");
		for(String s : items) {
			if(!stack1.isEmpty() && s.equals("-")) {
				stack1.pop();
			} else {
				stack1.push(s);
			}
			System.out.println(stack1.toString());
		}
		System.out.println("---------- using Stack ----------");
		Stack<Integer> stack = new Stack<Integer>();
		stack.push(1);
		stack.push(2);
		stack.push(3);
		stack.push(4);
		System.out.println(stack);
		System.out.println("stack size = " + stack.size());
		while(!stack.isEmpty()) {			
			System.out.print(stack.pop() + " ");
		}
		System.out.println();

		System.out.println("---------- using Deque ----------");
		Deque<Integer> deque = new ArrayDeque<Integer>();
		deque.addFirst(1);
		deque.addFirst(2);
		deque.addFirst(3);
		deque.addFirst(4);
		System.out.println(deque);
		System.out.println("deque size = " + deque.size());
		while(!deque.isEmpty()) {
			System.out.print(deque.removeFirst() + " ");
		}
		System.out.println();

	}
}
