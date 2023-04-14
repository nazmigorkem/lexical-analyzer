package src.TokenTypes;

import src.Tokenizer;

public class _String extends Token {
    public _String(String value) {
        super(value, "STRING");
    }

    @Override
    public Token match(String string) {
        int cursor = 0;
        if (string.charAt(cursor) == '"') {
            String _string = "";

            while (true) {
                _string += string.charAt(cursor);
                cursor++;
                if (Tokenizer.hasNextToken(cursor, string)) {
                    if (string.charAt(cursor) != '"') {
                        continue;
                    } else {
                        _string += string.charAt(cursor);

                        if (string.charAt(cursor - 1) == '\\' && string.charAt(cursor) == '"') {
                            continue;
                        }
                        if (string.charAt(cursor - 1) == '\\' && string.charAt(cursor - 2) != '\\') {
                            throw new Error("Unclosed quotes after " + string);
                        }

                        return new _String(_string);
                    }

                } else {
                    throw new Error("Unclosed quotes after " + string);
                }
            }

        }
        return null;
    }
}
