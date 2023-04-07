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
                return this.consume("NUMBER");
            case "STRING":
                return this.consume("STRING");
            case "RESERVED_KEYWORD":
                return this.consume("RESERVED_KEYWORD");
            case "IDENTIFIER":
                return this.consume("IDENTIFIER");
            case "IGNORED":
                return this.consume("IGNORED");
            case "EoF":
                return this.consume("EoF");
        }

        throw new Error("Unexpected token.");
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
