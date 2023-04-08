package src;

public class Main {

    public static void main(String[] args) {
        System.out.println(new Parser().parse("42 \"asd\" DEFINE []\n\t\n\t~asdfasdfasd\n(1213asdfasd"));
    }
}