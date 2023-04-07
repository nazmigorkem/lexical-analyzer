package src;

public class Token {
    String value;
    String typeName;

    Token(String value, String name) {
        this.value = value;
        this.typeName = name;
    }

    @Override
    public String toString() {
        return this.value + " " + this.typeName;
    }
}
