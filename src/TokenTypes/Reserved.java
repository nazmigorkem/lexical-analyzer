package src.TokenTypes;

public class Reserved extends Token {
    public static String ReservedTokens[] = {
            "DEFINE ",
            "LET ",
            "COND ",
            "IF ",
            "BEGIN "
    };

    public Reserved(String value) {
        super(value, "RESERVED_KEYWORD");
    }

    @Override
    public Token match(String string) {
        for (String _string : ReservedTokens) {
            if (_string.length() <= string.length() && string.substring(0, _string.length()).compareTo(_string) == 0) {
                return new Reserved(_string);
            }
        }
        return null;
    }
}
