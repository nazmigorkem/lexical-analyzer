package src.TokenTypes;

public class Boolean extends Token {
    String tokens[] = {
            "true", "false"
    };

    public Boolean(String value) {
        super(value, "BOOLEAN");
    }

    @Override
    public Token match(String string) {
        for (String token : tokens) {
            if (string.startsWith(token + " ")) {
                return new Boolean(token);
            }
        }
        return null;
    }
}
