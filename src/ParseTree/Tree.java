package src.ParseTree;

import java.util.Vector;

public class Tree {
    static public Vector<TreeNode> treeNodeVector = new Vector<>();

    public static int addTreeNode(int level, TreeNodeValue treeNode) {
        treeNodeVector.add(new TreeNode(level, treeNode));
        return treeNodeVector.size() - 1;
    }

    public static void removeTreeNodesAfterIndex(int index) {
        Vector<TreeNode> newtree = new Vector<>();
        for (int i = 0; i < index; i++) {
            newtree.add(treeNodeVector.get(i));
        }
        treeNodeVector = newtree;
    }

    public static String printTree() {
        StringBuilder string = new StringBuilder();
        for (TreeNode treeNode : treeNodeVector) {
            string.append("  ".repeat(Math.max(0, treeNode.level())));
            string.append(String.format("%s\n", treeNode.nodeType()));
        }
        return string.toString();
    }
}
