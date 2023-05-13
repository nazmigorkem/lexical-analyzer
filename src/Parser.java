package src;

import java.util.Objects;
import java.util.Vector;

import src.TokenTypes.EoF;
import src.TokenTypes.Ignored;
import src.TokenTypes.Token;

public class Parser {
    Tokenizer tokenizer;
    String string;
    Token lookahead;
    private static Parser instance;
    private final Vector<Token> tokenVector = new Vector<>();

    private Parser() {
        this.tokenizer = new Tokenizer();
    }

    static public Parser getInstance() {
        return Objects.requireNonNullElseGet(instance, () -> instance = new Parser());
    }

    Vector<Token> parse(String string) {
        this.string = string;
        this.tokenizer.initialize(string);
        this.lookahead = this.tokenizer.getNextToken();
        Token currentToken;
        do {
            currentToken = literal();
            if (currentToken instanceof Ignored || currentToken instanceof EoF)
                continue;
            this.tokenVector.add(currentToken);
        } while (!(currentToken instanceof EoF));
        return this.tokenVector;
    }

    Token literal() {
        switch (this.lookahead.typeName) {
            case "NUMBER":
            case "STRING":
            case "RESERVED_KEYWORD":
            case "IDENTIFIER":
            case "LEFTSQUAREB":
            case "LEFTCURLYB":
            case "LEFTPAR":
            case "DEFINE":
            case "LET":
            case "COND":
            case "BOOLEAN":
            case "CHARACTER":
            case "IF":
            case "BEGIN":
            case "RIGHTSQUAREB":
            case "RIGHTCURLYB":
            case "RIGHTPAR":
            case "IGNORED":
            case "EoF":
                return this.consume(this.lookahead.typeName);
        }
        throw new Error("Unexpected token. " + this.lookahead.typeName);
    }

    Token consume(String tokenType) {
        Token token = this.lookahead;

        if (tokenType == null) {
            throw new Error("Unexpected token type.");
        }

        if (!tokenType.equals(token.typeName)) {
            throw new Error("Unexpected token type. Got " + token.typeName + " Expected " + tokenType);
        }

        if (!(token instanceof EoF))
            this.lookahead = this.tokenizer.getNextToken();

        return token;
    }

    public Vector<Token> getTokenVector() {
        return tokenVector;
    }
}
