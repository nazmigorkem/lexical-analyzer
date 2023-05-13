package src;

import src.TokenTypes.Token;

public class Synthesizer {
    private static int tokenCursor = 0;

    static public Token getNextToken() {
        return Parser.getInstance().getTokenVector().get(tokenCursor);
    }

    static public void consumeToken() {
        tokenCursor++;
    }

}
