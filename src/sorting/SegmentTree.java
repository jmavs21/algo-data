package sorting;

class SegmentTree {

  private TreeNode root;

  // T = O(N), S = O(N), where sumRange operation is O(log(N))
  // Note: using segment tree, where all leaves are values in nums[i],
  //       and only those are updated,
  //       and rest going up are aggregate sum ranges.
  public SegmentTree(int[] nums) {
    root = buildTree(nums, 0, nums.length - 1);
  }

  private TreeNode buildTree(int[] nums, int lo, int hi) {
    if (lo > hi) return null;
    TreeNode node = new TreeNode(lo, hi);
    if (lo == hi) {
      node.sum = nums[lo];
      return node;
    }
    int mid = (hi - lo) / 2 + lo;
    node.left = buildTree(nums, lo, mid);
    node.right = buildTree(nums, mid + 1, hi);
    node.sum = node.left.sum + node.right.sum;
    return node;
  }

  public void update(int i, int val) {
    update(root, i, val);
  }

  private void update(TreeNode node, int i, int val) {
    if (i == node.lo && i == node.hi) {
      node.sum = val;
      return;
    }
    int mid = (node.hi - node.lo) / 2 + node.lo;
    if (i <= mid) update(node.left, i, val);
    else update(node.right, i, val);
    node.sum = node.left.sum + node.right.sum;
  }

  public int sumRange(int lo, int hi) {
    return sumRange(root, lo, hi);
  }

  private int sumRange(TreeNode node, int lo, int hi) {
    if (node.lo == lo && node.hi == hi) return node.sum;
    int mid = (node.hi - node.lo) / 2 + node.lo;
    if (lo > mid) return sumRange(node.right, lo, hi);
    else if (hi <= mid) return sumRange(node.left, lo, hi);
    else return sumRange(node.left, lo, mid) + sumRange(node.right, mid + 1, hi);
  }

  private static class TreeNode {
    int lo, hi;
    int sum;
    TreeNode left, right;

    TreeNode(int lo, int hi) {
      this.lo = lo;
      this.hi = hi;
    }
  }
}