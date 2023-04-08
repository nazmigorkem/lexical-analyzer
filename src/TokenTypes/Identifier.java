package src.TokenTypes;

import src.Tokenizer;

public class Identifier extends Token {
    public Identifier(String value) {
        super(value, "IDENTIFIER");
    }

    @Override
    public Token match(String string) {
        int cursor = 0;
        while (Tokenizer.hasNextToken(cursor, string) && (string.charAt(cursor) != '\n' && string.charAt(cursor) != '\t'
                && string.charAt(cursor) != '\s')) {
            cursor++;
        }
        Token result = new Identifier(string.substring(0, cursor));
        if (Number.isNumber(result.value.charAt(0))) {
            return null;
        } else
            return result;
    }
}
