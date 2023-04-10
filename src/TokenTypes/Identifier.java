package src.TokenTypes;

import src.Tokenizer;
import src.Util;

public class Identifier extends Token {
    public Identifier(String value) {
        super(value, "IDENTIFIER");
    }

    @Override
    public Token match(String string) {
        int cursor = 0;
        while (Tokenizer.hasNextToken(cursor, string)
                && !(Util.contains(string.charAt(cursor), Ignored.ignoredCharacters)
                        || Util.contains(string.charAt(cursor), Bracket.brackets))) {
            cursor++;
        }
        Token result = new Identifier(string.substring(0, cursor));
        if (Number.isNumber(result.value.charAt(0)) || result.value.charAt(0) == '\''
                || result.value.charAt(0) == '\\') {
            return null;
        } else
            return result;
    }
}
