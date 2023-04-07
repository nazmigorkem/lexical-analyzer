package src;

import src.TokenTypes.Token;
import src.TokenTypes._String;
import src.TokenTypes.EoF;
import src.TokenTypes.Ignored;
import src.TokenTypes.Number;
import src.TokenTypes.Reserved;

public class Tokenizer {

    private String string;
    private int cursor;
    private Token tokenTypes[] = {
            new Number(null),
            new _String(null),
            new Reserved(null),
            new Ignored(null),
    };

    Tokenizer() {
        this.cursor = 0;
    }

    void initialize(String string) {
        this.string = string;
    }

    public static boolean hasNextToken(int cursor, String string) {
        return cursor < string.length();
    }

    boolean isNumber(char character) {
        return character - 48 >= 0 && character - 48 <= 9;
    }

    Token getNextToken() {

        if (!Tokenizer.hasNextToken(this.cursor, this.string)) {
            return new EoF();
        }

        for (Token token : tokenTypes) {
            Token match = token.match(this.string.substring(cursor));
            if (match != null) {
                this.cursor += match.value.length();
                return match;
            }
        }
        System.out.println(this.string.substring(cursor));
        throw new Error("Unknown token.");
    }
}