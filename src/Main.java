package src;

import src.Exceptions.SyntaxException;
import src.Grammer.Program;
import src.TokenTypes.Token;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        StringBuilder program = new StringBuilder();
        try {
            File myObj = new File("./src/input.txt");
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                program.append(data).append("\n");
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        for (Token token : Parser.getInstance().parse(program.toString())) {
            System.out.println(token);
        }

        try {
            new Program().program();
        } catch (SyntaxException syntaxException) {
            syntaxException.printStackTrace();
            System.out.println(syntaxException.toString());
        }


    }
}