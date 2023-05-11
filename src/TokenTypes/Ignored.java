package src.TokenTypes;

import src.Tokenizer;

public class Ignored extends Token {

    public static char[] ignoredCharacters = {
            '\s', '\t', '\n'
    };

    public Ignored(String string) {
        super(string, "IGNORED");
    }

    @Override
    public Token match(String string) {
        int cursor = 0;
        boolean isComment = string.charAt(0) == '~';

        if (isComment) {
            while (true) {
                if (!Tokenizer.hasNextToken(cursor, string)) {
                    break;
                }
                if (string.charAt(cursor) != '\n') {
                    cursor++;
                } else {
                    break;
                }
            }
        } else {
            for (char c : ignoredCharacters) {
                while (true) {
                    if (!Tokenizer.hasNextToken(cursor, string)) {
                        break;
                    }

                    if (string.charAt(cursor) != c)
                        break;
                    else
                        cursor++;
                }
            }
        }
        if (cursor != 0)
            return new Ignored(string.substring(0, cursor));
        else
            return null;
    }
}
