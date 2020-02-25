package fundamentals;

import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Stack implementation with a resizing array representing a last-in-first-out (LIFO) of generic items.
 *     This implementation uses a resizing array, which double the underlying array when it is full 
 *     and halves the underlying array when it is one-quarter full.
 *     
 * Operations: where n is the number of elements in array.
 *      push, pop: O(1) amortized time.
 *      size, isEmpty, peek: O(1) worst case.
 */
public class StackResizingArray<T> implements Iterable<T>
{
	private T[] arr;
	private int n;

	@SuppressWarnings("unchecked")
	public StackResizingArray() {
		arr = (T[]) new Object[2];
		n = 0;
	}

	public boolean isEmpty() {
		return n == 0;
	}

	public int size() {
		return n;
	}

	private void resize(int capacity) {
		arr = Arrays.copyOf(arr, capacity);
	}

	public void push(T item) {
		if(n == arr.length) resize(2 * arr.length);
		arr[n++] = item;
	}

	public T pop() {
		if(isEmpty()) throw new NoSuchElementException("Stack underflow.");
		T item = arr[--n];
		arr[n] = null;
		if(n > 0 && n == arr.length / 4) resize(arr.length / 2);
		return item;
	}

	public T peek() {
		if(isEmpty()) throw new NoSuchElementException("Stack underflow.");
		return arr[n - 1];
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		for(T item : this) {
			sb.append(item);
			sb.append(" ");
		}
		return sb.toString();
	}

	public Iterator<T> iterator(){
		return new ReverseArrayIterator();
	}
	private class ReverseArrayIterator implements Iterator<T>{
		private int i;
		public ReverseArrayIterator() {
			i = n;
		}
		public boolean hasNext() {
			return i - 1 >= 0;
		}
		public T next() {
			if(!hasNext()) throw new NoSuchElementException();
			return arr[--i];
		}
		public void remove() { throw new UnsupportedOperationException(); }
	}

	// TESTS ========================================================
	public static void main(String[] args) {
		String test = "1 2 3 4 5 - 6 - - 7 - - - 8";
		StackResizingArray<String> stack = new StackResizingArray<String>();
		String[] items = test.split("\\s");
		for(String s : items) {
			if(!stack.isEmpty() && s.equals("-")) {
				stack.pop();
			} else {
				stack.push(s);
			}
			System.out.println(stack.toString());
		}
	}
}
