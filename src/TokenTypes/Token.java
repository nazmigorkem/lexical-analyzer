package src.TokenTypes;

public abstract class Token {
    public String value;
    public String typeName;

    public Token(String value, String name) {
        this.value = value;
        this.typeName = name;
    }

    @Override
    public String toString() {
        return this.value + " " + this.typeName;
    }

    public abstract Token match(String string);
}
