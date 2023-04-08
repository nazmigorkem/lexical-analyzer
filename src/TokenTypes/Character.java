package src.TokenTypes;

import src.Tokenizer;

public class Character extends Token {
    public Character(String value) {
        super(value, "CHARACTER");
    }

    @Override
    public Token match(String string) {
        if (string.length() > 4 && string.charAt(0) == '\'' && string.charAt(1) == '\\' && string.charAt(1) == '\''
                && string.charAt(3) == '\'') {
            return new Character(string.substring(0, 3));
        }
        if (string.length() > 4 && string.charAt(0) == '\'' && string.charAt(1) == '\\' && string.charAt(1) == '\\'
                && string.charAt(3) == '\'') {
            return new Character(string.substring(0, 3));
        }
        if (string.length() > 3 && string.charAt(0) == '\'' && string.charAt(1) != '\'' && string.charAt(1) != '\\'
                && string.charAt(2) == '\'') {
            return new Character(string.substring(0, 2));
        }
        return null;
    }
}
