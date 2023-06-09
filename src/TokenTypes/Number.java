package src.TokenTypes;

import src.Tokenizer;
import src.Util;

public class Number extends Token {
    char[] hexNumbers = {
            'A', 'B', 'C', 'D', 'E', 'F', 'a', 'b', 'c', 'd', 'e', 'f'
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
        String number;
        char firstCharacter = string.charAt(0);
        boolean isSigned = firstCharacter == '+' || firstCharacter == '-';

        if (Number.isNumber(firstCharacter) || ((isSigned) || firstCharacter == '.'
                && Tokenizer.hasNextToken(cursor, string) && Number.isNumber(string.charAt(1)))) {
            number = "";

            if (firstCharacter == '0' && Tokenizer.hasNextToken(1, string)) {
                char secondCharacter = string.charAt(1);
                if (secondCharacter == 'x') {
                    number += firstCharacter + String.valueOf(secondCharacter);
                    cursor += 2;
                    number = this.hex(number, string, cursor);
                    cursor += number.length() - 2;
                } else if (secondCharacter == 'b') {
                    number += firstCharacter + String.valueOf(secondCharacter);
                    cursor += 2;
                    number = this.binary(number, string, cursor);
                    cursor += number.length() - 2;
                }
                if (!Tokenizer.hasNextToken(cursor, string)
                        || Util.contains(string.charAt(cursor), Ignored.ignoredCharacters)
                        || Util.contains(string.charAt(cursor), Bracket.brackets))
                    return new Number(number);
            }
            Number result = null;
            if (number.equals("")) {
                if (isSigned) {
                    number += firstCharacter;
                    cursor++;
                }

                String currentWord = Tokenizer.getNextWord(cursor, string);

                if ((currentWord.contains("e") || currentWord.contains("E")) && !currentWord.contains(".")) {
                    result = this.onlyScientific(number, string, cursor);
                } else if (currentWord.contains(".")) {
                    result = this.mixed(number, string, cursor);
                } else {
                    result = this.decimal(number, string, cursor);
                }

            }
            if ((result != null ? result.value.length() : 0) == 1 && (result.value.compareTo("+") == 0 || result.value.compareTo("-") == 0
                    || result.value.compareTo(".") == 0)) {
                return null;
            }
            return result;

        }
        return null;
    }

    public String binary(String number, String string, int cursor) {
        StringBuilder numberBuilder = new StringBuilder(number);
        while (Tokenizer.hasNextToken(cursor, string)
                && (string.charAt(cursor) == '0' || string.charAt(cursor) == '1')) {
            numberBuilder.append(string.charAt(cursor));
            cursor++;
        }
        number = numberBuilder.toString();
        return number;
    }

    public String hex(String number, String string, int cursor) {
        StringBuilder numberBuilder = new StringBuilder(number);
        while (Tokenizer.hasNextToken(cursor, string)
                && (Number.isNumber(string.charAt(cursor)) || Util.contains(string.charAt(cursor), hexNumbers))) {
            numberBuilder.append(string.charAt(cursor));
            cursor++;
        }
        number = numberBuilder.toString();
        return number;
    }

    public Number decimal(String number, String string, int cursor) {
        StringBuilder numberBuilder = new StringBuilder(number);
        while (Tokenizer.hasNextToken(cursor, string) && Number.isNumber(string.charAt(cursor))) {
            numberBuilder.append(string.charAt(cursor));
            cursor++;
        }
        number = numberBuilder.toString();
        if (!Tokenizer.hasNextToken(cursor, string)
                || Util.contains(string.charAt(cursor), Ignored.ignoredCharacters)
                || Util.contains(string.charAt(cursor), Bracket.brackets))
            return new Number(number);
        else
            return null;
    }

    public Number onlyScientific(String number, String string, int cursor) {
        boolean isEFound = false;
        StringBuilder numberBuilder = new StringBuilder(number);
        while ((Tokenizer.hasNextToken(cursor, string) && (Number.isNumber(string.charAt(cursor))
                || (string.charAt(cursor) == 'e' || string.charAt(cursor) == 'E')))
                || ((Tokenizer.hasNextToken(cursor, string)
                        && (string.charAt(cursor) == '+' || string.charAt(cursor) == '-')
                        && (string.charAt(cursor - 1) == 'e' || string.charAt(cursor - 1) == 'E')))) {
            if ((string.charAt(cursor) == 'e' || string.charAt(cursor) == 'E') && !isEFound) {
                isEFound = true;
                numberBuilder.append(string.charAt(cursor));
                cursor++;
            } else if ((string.charAt(cursor) == 'e' || string.charAt(cursor) == 'E') && isEFound) {
                return null;
            }
            numberBuilder.append(string.charAt(cursor));
            cursor++;
        }
        number = numberBuilder.toString();
        if (!Tokenizer.hasNextToken(cursor, string)
                || Util.contains(string.charAt(cursor), Ignored.ignoredCharacters)
                || Util.contains(string.charAt(cursor), Bracket.brackets))
            return new Number(number);
        else
            return null;
    }

    public Number mixed(String number, String string, int cursor) {
        boolean isDotFound = false;
        StringBuilder numberBuilder = new StringBuilder(number);
        while (Tokenizer.hasNextToken(cursor, string)
                && (Number.isNumber(string.charAt(cursor)) || (string.charAt(cursor) == '.' && !isDotFound))) {
            if (string.charAt(cursor) == '.') {
                isDotFound = true;
            }
            numberBuilder.append(string.charAt(cursor));
            cursor++;
        }
        number = numberBuilder.toString();

        if (string.charAt(cursor - 1) != '.' && Tokenizer.hasNextToken(cursor, string)
                && (string.charAt(cursor) == 'e' || string.charAt(cursor) == 'E')
                && Tokenizer.hasNextToken(cursor + 1, string)
                && (Number.isNumber(string.charAt(cursor + 1)) || string.charAt(cursor + 1) == '+'
                        || string.charAt(cursor + 1) == '-')) {
            number += string.charAt(cursor);
            cursor++;
            if (Tokenizer.hasNextToken(cursor, string)
                    && (string.charAt(cursor) == '+' || string.charAt(cursor) == '-')) {
                number += string.charAt(cursor);
                cursor++;
            }
            StringBuilder numberBuilder1 = new StringBuilder(number);
            while (Tokenizer.hasNextToken(cursor, string) && Number.isNumber(string.charAt(cursor))) {
                numberBuilder1.append(string.charAt(cursor));
                cursor++;
            }
            number = numberBuilder1.toString();
        }

        if (!Tokenizer.hasNextToken(cursor, string)
                || Util.contains(string.charAt(cursor), Ignored.ignoredCharacters)
                || Util.contains(string.charAt(cursor), Bracket.brackets))
            return new Number(number);
        else
            return null;
    }
}
