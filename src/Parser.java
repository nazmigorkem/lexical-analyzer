package src;

import java.util.Vector;

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
        programVector.add(literal());
        programVector.add(literal());
        programVector.add(literal());
        return programVector;
    }

    Token literal() {
        switch (this.lookahead.typeName) {
            case "NUMBER":
                return this.numericLiteral();
            case "STRING":
                return this.stringLiteral();
        }

        throw new Error("Unexpected token.");
    }

    Token numericLiteral() {
        return this.consume("NUMBER");
    }

    Token stringLiteral() {
        return this.consume("STRING");
    }

    Token consume(String tokenType) {
        Token token = this.lookahead;

        if (tokenType == null) {
            throw new Error("Unexpected token type. Expected " + tokenType);
        }

        if (tokenType != token.typeName) {
            throw new Error("Unexpected token type. Got " + token.typeName + " Expected " + tokenType);
        }

        this.lookahead = this.tokenizer.getNextToken();

        return token;
    }
}
