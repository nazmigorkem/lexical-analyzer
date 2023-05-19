package src.TokenTypes;

public class Bracket extends Token {
    public static char[] brackets = {
            '[', '{', '(', ']', '}', ')'
    };

    public String[] names = {
            "LEFTSQUAREB", "LEFTCURLYB", "LEFTPAR", "RIGHTSQUAREB", "RIGHTCURLYB", "RIGHTPAR",
    };

    public Bracket(String value, String type) {
        super(value, type);
    }

    @Override
    public Token match(String string) {
        for (int i = 0; i < brackets.length; i++) {
            if (brackets[i] == string.charAt(0))
                return new Bracket(String.valueOf(brackets[i]), names[i]);
        }

        return null;
    }
}
