package fundamentals;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Queue implementation with a resizing arrayr espresenting a first-in-first-out (FIFO) of generic items.
 * 
 * Operations: where n is the number of elements in array.
 *      enqueue, dequeue: O(1) amortized time.
 *      size, isEmpty, peek: O(1) worst case.
 */
public class QueueResizingArray<T> implements Iterable<T>
{
	private T[] arr;
	private int n;
	private int first;
	private int last;

	@SuppressWarnings("unchecked")
	public QueueResizingArray() {
		arr = (T[]) new Object[2];
		n = 0;
		first = 0;
		last = 0;
	}

	public boolean isEmpty() {
		return n == 0;
	}

	public int size() {
		return n;
	}

	@SuppressWarnings({ "unchecked" })
	private void resize(int capacity) {
		T[] tmp = (T[]) new Object[capacity];
		for (int i = 0; i < n; i++) {
			tmp[i] = arr[(first + i) % arr.length];
		}
		arr = tmp;
		first = 0;
		last  = n;
	}

	public void enqueue(T item) {
		if(n == arr.length) resize(2 * arr.length);
		arr[last++] = item;
		if(last == arr.length) last = 0;
		n++;
	}

	public T dequeue() {
		if(isEmpty()) throw new NoSuchElementException("Queue underflow.");
		T item = arr[first];
		arr[first] = null;
		first++;
		n--;
		if(first == arr.length) first = 0;
		if(n > 0 && n == arr.length/4) resize(arr.length/2);
		return item;
	}

	public T peek() {
		if(isEmpty()) throw new NoSuchElementException("Queue underflow.");
		return arr[first];
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		for(T item : this) {
			sb.append(item);
			sb.append(" ");
		}
		return sb.toString();
	}

	public Iterator<T> iterator() {
		return new ArrayIterator();
	}
	private class ArrayIterator implements Iterator<T> {
		private int i = 0;
		public boolean hasNext() {
			return i < n;
		}
		public T next() {
			if(!hasNext()) throw new NoSuchElementException();
			T item = arr[(i + first) % arr.length];
			i++;
			return item;
		}
		public void remove() { throw new UnsupportedOperationException(); }
	}

	// TESTS ========================================================
	public static void main(String[] args) {
		String test = "1 2 3 4 5 - 6 - - 7 - - - 8";
		QueueResizingArray<String> queue = new QueueResizingArray<String>();
		String[] items = test.split("\\s");
		for(String s : items) {
			if(!queue.isEmpty() && s.equals("-")) {
				queue.dequeue();
			} else {
				queue.enqueue(s);
			}
			System.out.println(queue.toString());
		}
	}
}
