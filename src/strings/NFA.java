package strings;
import fundamentals.Stack;
import graphs.graph.digraph.DFSPaths;
import graphs.graph.digraph.Digraph;

/**
 * Creates a Non-deterministic Finite state automaton (NFA) from a regular expression
 * to test wether a text is match by that regular expression.
 * 
 * Initialization of NFA regex: O(M) time and space.
 * Operations:
 *     find: O(M*N) in the worst case.
 *     
 *  Where:
 *     N = length of text
 *     M = length of regular expression
 *     
 * NOTE: only supported the following metacaracters: ()|.*
 */
public class NFA 
{
	private char[] re;
	private final int m;
	private Digraph digraph;

	public NFA(String regex) {
		re = regex.toCharArray();
		m = regex.length();
		digraph = new Digraph(m + 1);
		Stack<Integer> ops = new Stack<Integer>();
		for(int i = 0; i < m; i++) {
			int lp = i;
			if(re[i] == '(' || re[i] == '|') ops.push(i);
			else if(re[i] == ')') {
				int or = ops.pop();
				if(re[or] == '|') {
					lp = ops.pop();
					digraph.addEdge(lp, or+1);
					digraph.addEdge(or, i);
				} else lp = or;
			}
			if(i < m-1 && re[i+1] == '*') {
				digraph.addEdge(lp, i+1);
				digraph.addEdge(i+1, lp);
			}
			if(re[i] == '(' || re[i] == '*' || re[i] == ')') {
				digraph.addEdge(i, i+1);
			}
		}
		if (ops.size() != 0) throw new IllegalArgumentException("Invalid regular expression");
	}

	public boolean find(String txt) {
		DFSPaths dfs = new DFSPaths(digraph, 0);
		Stack<Integer> possible = new Stack<Integer>();
		for(int v = 0; v < digraph.V(); v++) 
			if(dfs.hasPathTo(v)) possible.push(v);
		for(int i = 0; i < txt.length(); i++) {
			if (txt.charAt(i) == '*' || txt.charAt(i) == '|' || txt.charAt(i) == '(' || txt.charAt(i) == ')') throw new IllegalArgumentException("text contains the metacharacter '" + txt.charAt(i) + "'");
			Stack<Integer> match = new Stack<Integer>();
			for(int v : possible) {
				if(v == m) continue;
				if(re[v] == txt.charAt(i) || re[v] == '.') match.push(v+1);
			}
			possible = new Stack<Integer>();
			dfs = new DFSPaths(digraph, match);
			for(int v = 0; v < digraph.V(); v++) 
				if(dfs.hasPathTo(v)) possible.push(v);
			if (possible.size() == 0) return false;
		}
		for(int v : possible) 
			if(v == m) return true;
		return false;
	}
	
	public static void main(String[] args) {
		String regex = ".*a.*b";
		String text = "adceb";
		NFA nfa = new NFA("(" + regex + ")");
		System.out.println("regex = " + regex);
		System.out.println("text  = " + text);
		System.out.println("find  = " + nfa.find(text));
	}
}