package src.TokenTypes;

import src.Tokenizer;
import src.Util;

public class Identifier extends Token {

    char signs[] = { '+', '-', '.' };
    char canStartWith[] = { '!', '*', '/', ':', '<', '=', '>', '?' };

    public Identifier(String value) {
        super(value, "IDENTIFIER");
    }

    @Override
    public Token match(String string) {
        int cursor = 0;
        if (!Util.contains(string.charAt(cursor), canStartWith) && !Util.contains(string.charAt(cursor), signs)
                && !Tokenizer.isLetter(string.charAt(cursor))) {
            return null;
        }
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
