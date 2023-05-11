package src.TokenTypes;

import src.Util;

public class Boolean extends Token {
    String[] tokens = {
            "true", "false"
    };

    public Boolean(String value) {
        super(value, "BOOLEAN");
    }

    @Override
    public Token match(String string) {
        for (String token : tokens) {
            if (string.startsWith(token)) {
                if (!(Util.contains(string.charAt(token.length()), Ignored.ignoredCharacters)
                        || Util.contains(string.charAt(token.length()), Bracket.brackets))) {
                    return null;
                }
                return new Boolean(token);
            }
        }
        return null;
    }
}
