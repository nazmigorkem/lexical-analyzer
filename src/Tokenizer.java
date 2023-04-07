package src;

public class Tokenizer {

    private String string;
    private int cursor;

    Tokenizer() {
        this.cursor = 0;
    }

    void initialize(String string) {
        this.string = string;
    }

    boolean hasNextToken() {
        return cursor < this.string.length();
    }

    boolean isNumber(char character) {
        return character - 48 >= 0 && character - 48 <= 9;
    }

    Token getNextToken() {
        if (!this.hasNextToken()) {
            return new Token("EOF", "End Of File");
        }

        if (this.isNumber(this.string.charAt(cursor))) {
            String number = "";
            while (this.hasNextToken() && this.isNumber(this.string.charAt(this.cursor))) {
                number += this.string.charAt(this.cursor);
                this.cursor++;
            }

            return new Token(number, "NUMBER");
        }

        if (this.string.charAt(cursor) == '"') {
            String string = "";

            while (true) {
                string += this.string.charAt(this.cursor);
                this.cursor++;

                if (this.hasNextToken()) {
                    if (this.string.charAt(this.cursor) != '"') {
                        continue;
                    } else {
                        string += this.string.charAt(this.cursor);
                        this.cursor++;
                        return new Token(string, "STRING");
                    }

                } else {
                    throw new Error("Unclosed quotes.");
                }

            }

        }
        return null;
    }
}