package src;

import src.TokenTypes.Token;

public class Main {

    public static void main(String[] args) {
        for (Token token : new Parser().parse("42 \"asd\" DEFINE []\n\t\n\t~asdfasdfasd\n(asdfasd")) {
            System.out.println(token);
        }
    }
}