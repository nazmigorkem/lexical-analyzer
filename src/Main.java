package src;

import src.Exceptions.SyntaxException;
import src.Grammer.Program;
import src.ParseTree.Tree;
import src.TokenTypes.Token;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws IOException {
        StringBuilder program = new StringBuilder();
        try {
            File myObj = new File("./input.txt");
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

        SyntaxException syntaxException = null;
        try {
            new Program().synthesize();
        } catch (SyntaxException syntaxException_) {
            syntaxException_.printStackTrace();
            syntaxException = syntaxException_;
        }

        PrintWriter out = new PrintWriter("output.txt");
        out.write(Tree.printTree());
        if (syntaxException != null)
            out.write(syntaxException.toString());
        out.close();


    }
}