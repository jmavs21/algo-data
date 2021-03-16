package graphs.graph.weighted.digraph;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.LinkedHashMap;
import java.util.Scanner;

import fundamentals.Stack;

/**
 * Finds Depth-Search Ordering of the vertices in a wighted digraph, including: preorder, postorder,
 * reverse postorder (topological order).
 *
 * <p>Extra space: O(V)
 *
 * <p>Initialization: O(E + V) in the worst case. Operations: pre, post, reversePost: O(V)
 *
 * <p>Note: implementation uses a LinkedHashMap in order to mantiain insertion order (similar to a
 * queue). where: key is vertix, and value is index (pre/order)-
 */
public class DepthFirstOrder {
  private boolean[] visited;
  private LinkedHashMap<Integer, Integer> preorder;
  private LinkedHashMap<Integer, Integer> postorder;

  public DepthFirstOrder(WeightedDigraph wDigraph) {
    visited = new boolean[wDigraph.V()];
    preorder = new LinkedHashMap<Integer, Integer>();
    postorder = new LinkedHashMap<Integer, Integer>();
    for (int v = 0; v < wDigraph.V(); v++) {
      if (!visited[v]) {
        dfs(wDigraph, v);
      }
    }
  }

  private void dfs(WeightedDigraph wDigraph, int v) {
    visited[v] = true;
    preorder.put(v, preorder.size());
    for (EdgeDirect e : wDigraph.adjacents(v)) {
      int w = e.to();
      if (!visited[w]) {
        dfs(wDigraph, w);
      }
    }
    postorder.put(v, postorder.size());
  }

  public int preorder(int v) {
    validateVertex(v);
    return preorder.get(v);
  }

  public int postorder(int v) {
    validateVertex(v);
    return postorder.get(v);
  }

  public Iterable<Integer> preorder() {
    return preorder.keySet();
  }

  public Iterable<Integer> postorder() {
    return postorder.keySet();
  }

  public Iterable<Integer> reversePostorder() {
    Stack<Integer> reverse = new Stack<Integer>();
    for (int v : postorder()) reverse.push(v);
    return reverse;
  }

  private void validateVertex(int v) {
    int V = visited.length;
    if (v < 0 || v >= V)
      throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V - 1));
  }

  public static void main(String[] args) throws FileNotFoundException {
    try (Scanner in = new Scanner(new FileReader("resources/graph/tinyEWDAG.txt"))) {
      WeightedDigraph wDigraph = new WeightedDigraph(in);
      System.out.println("EWD: ");
      System.out.println(wDigraph);
      DepthFirstOrder dfs = new DepthFirstOrder(wDigraph);
      System.out.println("   v  pre post");
      System.out.println("--------------");
      for (int v = 0; v < wDigraph.V(); v++) {
        System.out.printf("%4d %4d %4d\n", v, dfs.preorder(v), dfs.postorder(v));
      }
      System.out.print("Preorder:  ");
      for (int v : dfs.preorder()) {
        System.out.print(v + " ");
      }
      System.out.println();
      System.out.print("Postorder: ");
      for (int v : dfs.postorder()) {
        System.out.print(v + " ");
      }
      System.out.println();
      System.out.print("Reverse postorder: ");
      for (int v : dfs.reversePostorder()) {
        System.out.print(v + " ");
      }
      System.out.println();
    }
  }
}
