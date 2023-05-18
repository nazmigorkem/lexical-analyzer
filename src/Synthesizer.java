package src;

import src.ParseTree.Tree;
import src.ParseTree.TreeNodeValue;
import src.TokenTypes.EoF;
import src.TokenTypes.Token;

public class Synthesizer {
    private static int tokenCursor = 0;

    static public Token getNextToken() {
        if (Parser.getInstance().getTokenVector().size() == tokenCursor) {
            EoF eof = new EoF();
            eof.location = Parser.getInstance().getTokenVector().lastElement().location;
            return eof;
        }
        return Parser.getInstance().getTokenVector().get(tokenCursor);
    }

    static public void consumeToken(int treeLevel) {
        Tree.addTreeNode(treeLevel, TreeNodeValue.Token, Parser.getInstance().getTokenVector().get(tokenCursor));
        tokenCursor++;
    }

}
