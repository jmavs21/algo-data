package searching;

/**
 * Sparse Vector represents a dimensional mathematical vector. Vectors are mutable:
 * their values can be changed after they are created.
 * 
 * Initialization: O(1)
 * Operations: Where n is the number of items.
 *     dot, scale, plus, toString: O(n)
 *     put, get: O(log n)
 *     nnz, dimension, magnitude: O(1)
 * 
 * Notes: 
 *    The implementation is a symbol table of indices and values for which the vector
 *    coordinates are nonzero. This makes it efficient when most of the vector coordindates are zero.
 */
public class SparseVector 
{
	private int d;                           // dimension of vector
	private RedBlackBST<Integer, Double> st; // the vector, represented by index-value pairs

	public SparseVector(int d) {
		this.d  = d;
		this.st = new RedBlackBST<Integer, Double>();
	}

	public void put(int i, double value) {
		if (i < 0 || i >= d) throw new IllegalArgumentException("Illegal index");
		if (value == 0.0) st.delete(i);
		else              st.put(i, value);
	}

	public double get(int i) {
		if (i < 0 || i >= d) throw new IllegalArgumentException("Illegal index");
		if (st.contains(i)) return st.get(i);
		else                return 0.0;
	}

	public int size() {
		return st.size();
	}

	public int dimension() {
		return d;
	}

	public double dot(SparseVector that) {
		if (this.d != that.d) throw new IllegalArgumentException("Vector lengths disagree");
		double sum = 0.0;
		if (this.st.size() <= that.st.size()) {
			for (int i : this.st.keys())
				if (that.st.contains(i)) sum += this.get(i) * that.get(i);
		}
		else  {
			for (int i : that.st.keys())
				if (this.st.contains(i)) sum += this.get(i) * that.get(i);
		}
		return sum;
	}

	public double dot(double[] that) {
		double sum = 0.0;
		for (int i : st.keys())
			sum += that[i] * this.get(i);
		return sum;
	}

	public double magnitude() { // also known as the L2 norm or the Euclidean norm.
		return Math.sqrt(this.dot(this));
	}

	public SparseVector scale(double alpha) {
		SparseVector c = new SparseVector(d);
		for (int i : this.st.keys()) {			
			c.put(i, alpha * this.get(i));
		}
		return c;
	}

	public SparseVector plus(SparseVector that) {
		if (this.d != that.d) throw new IllegalArgumentException("Vector lengths disagree");
		SparseVector c = new SparseVector(d);
		for (int i : this.st.keys()) 
			c.put(i, this.get(i));                // c = this
		for (int i : that.st.keys()) 
			c.put(i, that.get(i) + c.get(i));     // c = c + that
		return c;
	}

	public String toString() {
		StringBuilder s = new StringBuilder();
		for (int i : st.keys()) {
			s.append("(" + i + ", " + st.get(i) + ") ");
		}
		return s.toString();
	}

	// TESTS ========================================================
	public static void main(String[] args) {
		SparseVector a = new SparseVector(10);
		SparseVector b = new SparseVector(10);
		a.put(3, 0.50);
		a.put(9, 0.75);
		a.put(6, 0.11);
		a.put(6, 0.00);
		b.put(3, 0.60);
		b.put(4, 0.90);
		System.out.println("a = " + a);
		System.out.println("b = " + b);
		System.out.println("a dot b = " + a.dot(b));
		System.out.println("a + b   = " + a.plus(b));
	}
}
