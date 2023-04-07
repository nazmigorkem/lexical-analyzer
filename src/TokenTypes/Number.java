package src.TokenTypes;

import src.Tokenizer;

public class Number extends Token {
    public Number(String string) {
        super(string, "NUMBER");
    }

    static boolean isNumber(char character) {
        return character - 48 >= 0 && character - 48 <= 9;
    }

    @Override
    public Number match(String string) {
        int cursor = 0;
        String number = null;
        if (Number.isNumber(string.charAt(0))) {
            number = "";
            while (Tokenizer.hasNextToken(cursor, string) && Number.isNumber(string.charAt(cursor))) {
                number += string.charAt(cursor);
                cursor++;
            }
            return new Number(number);
        }
        return null;
    }
}
