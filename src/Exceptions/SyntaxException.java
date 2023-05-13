package src.Exceptions;

import src.TokenTypes.Token;

public class SyntaxException extends Exception {
    private SyntaxException(Token currentToken, String expectedTokenValue) {
        super(String.format("SYNTAX ERROR [%d:%d]: '%s' is expected", currentToken.location[0], currentToken.location[1], expectedTokenValue));
    }

    private SyntaxException(String message) {
        super(message);
    }

    public static SyntaxException NonTerminalException(String message) {
        return new SyntaxException(message);
    }

    public static SyntaxException TokenException(Token currentToken, String expectedTokenValue) {
        return new SyntaxException(currentToken, expectedTokenValue);
    }
}
