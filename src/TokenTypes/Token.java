package src.TokenTypes;

public abstract class Token {
    public String value;
    public String typeName;
    public int location[] = { 0, 0 };

    public Token(String value, String name) {
        this.value = value;
        this.typeName = name;
    }

    @Override
    public String toString() {
        return this.typeName + " " + location[0] + ":" + location[1];
    }

    public abstract Token match(String string);
}
