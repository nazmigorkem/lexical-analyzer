package src.ParseTree;

import src.TokenTypes.Token;

import java.util.Vector;

public class Tree {
    static public Vector<TreeNode> treeNodeVector = new Vector<>();

    public static int addTreeNode(int level, TreeNodeValue treeNode, Token token) {
        treeNodeVector.add(new TreeNode(level, treeNode, token));
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
            string.append("\t".repeat(Math.max(0, treeNode.level())));
            if (treeNode.nodeType() != TreeNodeValue.Token) {
                string.append(String.format("<%s>\n", treeNode.nodeType()));
            } else {
                string.append(String.format("%s (%s)\n", treeNode.token().typeName.toUpperCase(), treeNode.token().value));
            }
        }
        return string.toString();
    }
}
