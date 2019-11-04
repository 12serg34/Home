package temp.subtree;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

class Solution {
    private TreeNode headOfSubtree;

    public boolean isSubtree(TreeNode s, TreeNode t) {
        headOfSubtree = t;
        TreeNode hashedSubTree = hashTree(t, (node, hashedNode)  -> {});

        Map<TreeNode, TreeNode> candidates = new HashMap<>(3);
        TreeNode hashedMainTree = hashTree(s, (node, hashedNode) -> {
            if (hashedNode.val == hashedSubTree.val) {
                candidates.put(node, hashedNode);
            }
        });

        for (Map.Entry<TreeNode, TreeNode> entry : candidates.entrySet()) {
            if (check(entry.getKey(), t, entry.getValue(), hashedSubTree)) {
                return true;
            }
        }
        return false;
    }

    private TreeNode hashTree(TreeNode head, BiConsumer<TreeNode, TreeNode> action) {
        TreeNode result = new TreeNode(head.val);
        if (head.left != null) {
            TreeNode hashedLeftSubtree = hashTree(head.left, action);
            result.left = hashedLeftSubtree;
            result.val = 31 * result.val + hashedLeftSubtree.val;
        }
        if (head.right != null) {
            TreeNode hashedRightSubtree = hashTree(head.right, action);
            result.right = hashedRightSubtree;
            result.val = 31 * result.val + hashedRightSubtree.val;
        }
        action.accept(head, result);
        return result;
    }

    private boolean check(TreeNode s, TreeNode t, TreeNode hs, TreeNode ht) {
        if (hs.val != ht.val || s.val != t.val) {
            return false;
        }
        if (s.left != null && t.left != null) {
            if (!check(s.left, t.left, hs.left, ht.left)) {
                return false;
            }
        }
        if (s.right != null && t.right != null) {
            if (!check(s.right, t.right, hs.right, ht.right)) {
                return false;
            }
        }
        return true;
    }
}

