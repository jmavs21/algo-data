package util;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Pow class with different implementations.
 * 
 * @author joaquin
 */
public class Pow {

	// T = O(n), S = O(log n), where n is y
	public static float powRecur(float x, int y) {
		if (y == 0)
			return 1;
		if (y % 2 == 0)
			return powRecur(x, y / 2) * powRecur(x, y / 2);
		return x * powRecur(x, y / 2) * powRecur(x, y / 2);
	}

	// T = O(log n), S = O(log n), where n is y
	public static float powRecurTmp(float x, int y) {
		if (y == 0)
			return 1;
		float tmp = powRecurTmp(x, y / 2);
		if (y % 2 == 0)
			return tmp * tmp;
		return x * tmp * tmp;
	}

	// T = O(log n), S = O(log n), where n is y
	public static float pow(float x, int y) {
		float tmp;
		if (y == 0)
			return 1;
		tmp = pow(x, y / 2);
		if (y % 2 == 0)
			return tmp * tmp;
		if (y > 0)
			return x * tmp * tmp;
		return (tmp * tmp) / x;
	}

	// T = O(log n), S = O(log n), where n is y
	public static float powIter(float x, int y) {
		Deque<Integer> stack = new ArrayDeque<>();
		while (y != 0) {
			stack.push(y);
			y /= 2;
		}
		float tmp = 1;
		while (!stack.isEmpty()) {
			int val = stack.pop();
			if (val % 2 == 0)
				tmp = tmp * tmp;
			if (val > 0)
				tmp = x * tmp * tmp;
			else
				tmp = (tmp * tmp) / x;
		}
		return tmp;
	}
	
	public static void main(String[] args) {
		System.out.println(powRecur(5, 7));
		System.out.println(powRecurTmp(5, 7));
		System.out.println(pow(5, -7));
		System.out.println(powIter(5, -7));
	}
}
