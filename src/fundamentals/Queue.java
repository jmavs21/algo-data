package fundamentals;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Queue implementation with a linked list respresenting a first-in-first-out (FIFO) of generic items.
 * 
 * Operations: where n is the number of elements in list.
 *      enqueue, dequeue, peek, size, isEmpty: O(1) in the worst case.
 */
public class Queue<T> implements Iterable<T>
{
	private Node<T> first;
	private Node<T> last;
	private int n;

	private static class Node<T> {
		private T item;
		private Node<T> next;
	}

	public Queue() {
		first = null;
		last = null;
		n = 0;
	}

	public boolean isEmpty() {
		return first == null;
	}

	public int size() {
		return n;
	}

	public void enqueue(T item) {
		Node<T> oldLast = last;
		last = new Node<T>();
		last.item = item;
		if(isEmpty()) first = last;
		else oldLast.next = last;
		n++;
	}

	public T dequeue() {
		if (isEmpty()) throw new NoSuchElementException("Queue underflow.");
		T item = first.item;
		first = first.next;
		if(isEmpty()) last = null;
		n--;
		return item;
	}

	public T peek() {
		if (isEmpty()) throw new NoSuchElementException("Queue underflow.");
		return first.item;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		for(T i : this) {
			sb.append(i);
			sb.append(" ");
		}
		return sb.toString();
	}

	public Iterator<T> iterator() {
		return new Iterator<T>() {
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
		};
	}

	// TESTS ========================================================
	public static void main(String[] args) {
		String test = "1 2 3 4 5 - 6 - - 7 - - - 8";
		Queue<String> q = new Queue<String>();
		String[] items = test.split("\\s");
		for(String s : items) {
			if(!q.isEmpty() && s.equals("-")) {
				q.dequeue();
			} else {
				q.enqueue(s);
			}
			System.out.println(q.toString());
		}
		System.out.println("---------- using Queue ----------");
		Queue<Integer> queue = new Queue<Integer>();
		queue.enqueue(1);
		queue.enqueue(2);
		queue.enqueue(3);
		queue.enqueue(4);
		System.out.println(queue);
		System.out.println("queue size = " + queue.size());
		while(!queue.isEmpty()) {
			System.out.print(queue.dequeue() + " ");
		}
		System.out.println();

		System.out.println("---------- using Deque ----------");
		Deque<Integer> deque = new ArrayDeque<Integer>();
		deque.addLast(1);
		deque.addLast(2);
		deque.addLast(3);
		deque.addLast(4);
		System.out.println(deque);
		System.out.println("deque size = " + deque.size());
		while(!deque.isEmpty()) {
			System.out.print(deque.removeFirst() + " ");
		}
		System.out.println();
	}
}
