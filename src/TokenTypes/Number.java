package src.TokenTypes;

import src.Tokenizer;
import src.Util;

public class Number extends Token {
    char hexNumbers[] = {
            'A', 'B', 'C', 'D', 'E', 'F'
    };

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
        char firstCharacter = string.charAt(0);
        boolean isSigned = firstCharacter == '+' || firstCharacter == '-';

        if (Number.isNumber(firstCharacter) || ((isSigned)
                && Tokenizer.hasNextToken(cursor, string) && Number.isNumber(string.charAt(1)))) {
            number = "";

            if (firstCharacter == '0' && Tokenizer.hasNextToken(1, string)) {
                char secondCharacter = string.charAt(1);
                if (secondCharacter == 'x') {
                    number += firstCharacter + "" + secondCharacter;
                    cursor += 2;
                    number = this.hex(number, string, cursor);
                    cursor += number.length() - 2;
                } else if (secondCharacter == 'b') {
                    number += firstCharacter + "" + secondCharacter;
                    cursor += 2;
                    number = this.binary(number, string, cursor);
                    cursor += number.length() - 2;
                }
            }

            if (number == "") {
                if (isSigned) {
                    number += firstCharacter;
                    cursor++;
                }

                number = this.decimal(number, string, cursor);

                cursor += number.length() - (isSigned ? 1 : 0);
            }

            if (!Tokenizer.hasNextToken(cursor, string)
                    || Util.contains(string.charAt(cursor), Ignored.ignoredCharacters)
                    || Util.contains(string.charAt(cursor), Bracket.brackets))
                return new Number(number);

        }
        return null;
    }

    public String binary(String number, String string, int cursor) {
        while (Tokenizer.hasNextToken(cursor, string)
                && (string.charAt(cursor) == '0' || string.charAt(cursor) == '1')) {
            number += string.charAt(cursor);
            cursor++;
        }
        return number;
    }

    public String hex(String number, String string, int cursor) {
        while (Tokenizer.hasNextToken(cursor, string)
                && (Number.isNumber(string.charAt(cursor)) || Util.contains(string.charAt(cursor), hexNumbers))) {
            number += string.charAt(cursor);
            cursor++;
        }
        return number;
    }

    public String decimal(String number, String string, int cursor) {
        while (Tokenizer.hasNextToken(cursor, string) && Number.isNumber(string.charAt(cursor))) {
            number += string.charAt(cursor);
            cursor++;
        }
        number = floatingPoint(number, string, cursor);
        return number;
    }

    public String floatingPoint(String number, String string, int cursor) {
        if (string.charAt(cursor) == '.' && Tokenizer.hasNextToken(cursor + 1, string)
                && Number.isNumber(string.charAt(cursor + 1))) {
            number += string.charAt(cursor);
            cursor++;
            while (Tokenizer.hasNextToken(cursor, string) && Number.isNumber(string.charAt(cursor))) {
                number += string.charAt(cursor);
                cursor++;
            }
        }
        return number;
    }
}
