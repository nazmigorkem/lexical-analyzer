package src;

import src.TokenTypes.Token;
import src.TokenTypes._String;
import src.TokenTypes.Boolean;
import src.TokenTypes.Bracket;
import src.TokenTypes.EoF;
import src.TokenTypes.Identifier;
import src.TokenTypes.Ignored;
import src.TokenTypes.Number;
import src.TokenTypes.Reserved;

public class Tokenizer {

    private String string;
    private int cursor;
    private int cursor_of_line;
    private int line;
    private Token tokenTypes[] = {
            new Number(null),
            new _String(null),
            new Reserved(null, null),
            new Boolean(null),
            new Ignored(null),
            new Bracket(null, null),
            new Identifier(null),
    };

    Tokenizer() {
        this.line = 1;
        this.cursor_of_line = 1;
        this.cursor = 0;
    }

    void initialize(String string) {
        this.string = string;
    }

    public static boolean hasNextToken(int cursor, String string) {
        return cursor < string.length();
    }

    Token getNextToken() {

        if (!Tokenizer.hasNextToken(this.cursor, this.string)) {
            return new EoF();
        }

        for (Token token : tokenTypes) {
            Token match = token.match(this.string.substring(this.cursor));
            if (match != null) {
                match.location[0] = line;
                match.location[1] = cursor_of_line;
                this.cursor += match.value.length();
                this.cursor_of_line += match.value.length();
                if (match.value.startsWith("\n")) {
                    this.cursor_of_line = 1;
                    this.line += match.value.toCharArray().length;
                }
                return match;
            }
        }
        throw new Error(
                "Unknown token at" + " [" + this.line + ":" + this.cursor_of_line + "] " + string.substring(cursor));
    }
}