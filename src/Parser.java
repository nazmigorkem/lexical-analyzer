package src;

import java.util.Vector;

import src.TokenTypes.EoF;
import src.TokenTypes.Ignored;
import src.TokenTypes.Token;

public class Parser {
    Tokenizer tokenizer;
    String string;
    Token lookahead;

    Parser() {
        this.tokenizer = new Tokenizer();
    }

    Vector<Token> parse(String string) {
        this.string = string;
        this.tokenizer.initialize(string);
        this.lookahead = this.tokenizer.getNextToken();

        return this.program();
    }

    Vector<Token> program() {
        Vector<Token> programVector = new Vector<Token>();
        Token currentToken = null;
        do {
            currentToken = literal();
            if (currentToken instanceof Ignored)
                continue;
            programVector.add(currentToken);
        } while (!(currentToken instanceof EoF));
        return programVector;
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
            throw new Error("Unexpected token type. Expected " + tokenType);
        }

        if (tokenType != token.typeName) {
            throw new Error("Unexpected token type. Got " + token.typeName + " Expected " + tokenType);
        }

        if (!(token instanceof EoF))
            this.lookahead = this.tokenizer.getNextToken();

        return token;
    }
}
