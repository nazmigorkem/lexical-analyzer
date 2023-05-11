package src.TokenTypes;

public class Reserved extends Token {
    public static String[] ReservedTokens = {
            "define",
            "let",
            "cond",
            "if",
            "begin"
    };

    public Reserved(String value, String name) {
        super(value, name);
    }

    @Override
    public Token match(String string) {
        for (String _string : ReservedTokens) {
            if (_string.length() <= string.length()
                    && string.substring(0, _string.length()).compareTo(_string) == 0
                    && string.charAt(_string.length()) == '\s') {
                return new Reserved(_string, _string.toUpperCase());
            }
        }
        return null;
    }
}
