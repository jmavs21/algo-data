package util;

import java.util.Stack;

/**
 * Class to solve tower of hanoi iterative and recursive.
 *     
 *  Initialization: O(n) where n is the number of disks.
 *  Operations: 
 *      solveIterative: T = O(2^n) and S = (n)
 *      
 * @author joaquin
 */
public class TowersOfHanoi {

	int numberOfDisks;
	long totalMoves;
	Stack<Integer> source;
	Stack<Integer> destination;
	Stack<Integer> aux;

	public TowersOfHanoi(int numberOfDisks) {
		this.numberOfDisks = numberOfDisks;
		totalMoves = (long) Math.pow(2, numberOfDisks) - 1;
		source = new Stack<Integer>();
		destination = new Stack<Integer>();
		aux = new Stack<Integer>();
		for(int i = this.numberOfDisks; i > 0; i--) {
			source.push(i);
		}
	}
	void solveIterative() {
		for(long i = 1; i <= totalMoves; i++) {
			System.out.printf("\nmove=%d, with %d%%3=%d", i, i,  i%3);
			if(i % 3 == 1) {
				moveDisk(source, destination);
			} else if(i % 3 == 2) {
				moveDisk(source, aux);
			} else if(i % 3 == 0) {
				moveDisk(aux, destination);
			}
			printContentOfStacks();
		}
	}
	void moveDisk(Stack<Integer> from, Stack<Integer> to) {
		if(from.isEmpty()) {
			from.push(to.pop());
		} else if(to.isEmpty()) {
			to.push(from.pop());
		} else if(from.peek() > to.peek()) {
			from.push(to.pop());
		} else {
			to.push(from.pop());
		}
	}
	void printContentOfStacks() {
		System.out.println("\n    source="+ source);
		if(numberOfDisks % 2 == 0) {
			System.out.println("    aux="+ destination);
			System.out.println("    destination="+ aux);
		} else {
			System.out.println("    aux="+ aux);
			System.out.println("    destination="+ destination);
		}
	}

	//=================== recurisve ===================
	void solveRecursive(int nOfDisks, String from, String to, String aux) {
		if(nOfDisks == 1) {
			System.out.println("Move disk " + nOfDisks + ", from=" + from + ", to=" + to);
			return;
		}
		solveRecursive(nOfDisks - 1, from, aux, to);
		System.out.println("Move disk " + nOfDisks + ", from=" + from + ", to=" + to);
		solveRecursive(nOfDisks - 1, aux, to, from);
	}

	//=================== recurisve with stack ===================
	void solveRecursiveStack(int nOfDisks) {
		numberOfDisks = nOfDisks;
		Stack<Long> source = new Stack<Long>();
		Stack<Long> destination = new Stack<Long>();
		Stack<Long> aux = new Stack<Long>();
		for(long i = nOfDisks; i > 0; i--) {
			source.push(i);
		}
		printRecursiveStack(source, destination, aux);
		solveRecursiveStack(nOfDisks, source, destination, aux);
	}
	void solveRecursiveStack(int nOfDisks, Stack<Long> source, Stack<Long> destination, Stack<Long> aux) {
		if(nOfDisks > 0) {
			solveRecursiveStack(nOfDisks - 1, source, aux, destination);
			destination.push(source.pop());
			printRecursiveStack(source, destination, aux);
			solveRecursiveStack(nOfDisks - 1, aux, destination, source);
		}
	}
	void printRecursiveStack(Stack<Long> source, Stack<Long> destination, Stack<Long> aux) {
		System.out.println("\n    source="+ source);
		if(numberOfDisks % 2 == 0) {
			System.out.println("    aux="+ destination);
			System.out.println("    destination="+ aux);
		}
		else {
			System.out.println("    aux="+ aux);
			System.out.println("    destination="+ destination);
		}
	}

	/**
	 * ohter recursive
	 * @param args
	 */
	public static String hanoi(int n, boolean left) {
		if(n == 0) {
			return " ";
		}
		String move;
		if(left) {
			move = n + "L";
		} else {
			move = n + "R";
		}
		return hanoi(n - 1, !left) + move + hanoi(n - 1, !left);
	}

	// TESTS ========================================================
	public static void main(String[] args) {
		TowersOfHanoi towersOfHanoi = new TowersOfHanoi(3);
		System.out.printf("Towers Of Hanoi, with %d disks, there are %d total moves.", towersOfHanoi.numberOfDisks, towersOfHanoi.totalMoves);
		towersOfHanoi.printContentOfStacks();
		towersOfHanoi.solveIterative();

		System.out.println("================== recursive ==================");
		towersOfHanoi.solveRecursive(3, "source", "destination", "aux");

		System.out.println("================== recursive with stack ==================");
		towersOfHanoi.solveRecursiveStack(3);
	}
}