package src.TokenTypes;

public class EoF extends Token {
    public EoF() {
        super("", "EoF");
    }

    @Override
    public Token match(String string) {
        return null;
    }
}
