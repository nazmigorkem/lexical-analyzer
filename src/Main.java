package src;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import src.TokenTypes.Token;

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
        for (Token token : new Parser().parse(program.toString())) {
            System.out.println(token);
        }
    }
}