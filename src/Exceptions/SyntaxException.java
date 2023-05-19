package src.Exceptions;

import src.TokenTypes.Token;

import java.util.HashMap;

public class SyntaxException extends Exception {

    private final static HashMap<String, String> LOOKUP_TABLE = new HashMap<>() {{
        put("LEFTSQUAREB", "[");
        put("LEFTCURLYB", "{");
        put("LEFTPAR", "(");
        put("RIGHTSQUAREB", "]");
        put("RIGHTCURLYB", "}");
        put("RIGHTPAR", ")");

    }};
    private SyntaxException(Token currentToken, String expectedTokenValue) {
        super(String.format("SYNTAX ERROR [%d:%d]: '%s' is expected but got '%s'", currentToken.location[0], currentToken.location[1], expectedTokenValue, currentToken.value));
    }

    private SyntaxException(String message) {
        super(message);
    }

    public static SyntaxException NonTerminalException(String message) {
        return new SyntaxException(message);
    }

    public static SyntaxException TokenException(Token currentToken, String expectedTokenValue) {
        expectedTokenValue = LOOKUP_TABLE.get(expectedTokenValue) != null ? LOOKUP_TABLE.get(expectedTokenValue) : expectedTokenValue.toLowerCase();
        return new SyntaxException(currentToken, expectedTokenValue);
    }
}
