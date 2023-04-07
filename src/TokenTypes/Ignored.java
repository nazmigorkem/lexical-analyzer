package src.TokenTypes;

public class Ignored extends Token {

    char ignoredCharacters[] = {
            ' ', '\t', '\n'
    };

    public Ignored(String string) {
        super(string, "IGNORED");
    }

    @Override
    public Token match(String string) {
        int cursor = 0;
        for (char c : ignoredCharacters) {
            while (true) {
                if (string.charAt(cursor) != c)
                    break;
                else
                    cursor++;
            }
        }
        if (cursor != 0)
            return new Ignored(string.substring(0, cursor));
        else
            return null;
    }
}
