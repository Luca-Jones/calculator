package main;

import java.util.Scanner;

import ast_nodes.ASTNode;
import ast_nodes.NumberNode;
import ast_nodes.UnaryOperatorNode;
import lexer.Lexer;
import parser.Parser;
import parser.SyntaxException;

public class Main {

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

    // TODO: implement a nice refresh / scroll (not sure what feels best)
    public static void main(String[] args) {

        if (args.length > 0) {
            switch (args[0]) {
                case "-h":
                case "--help":
                case "help":
                    printHelpMessage();
                default:
                    break;
            }
            return;
        }

        Scanner scanner = new Scanner(System.in);
        Parser parser = new Parser();
        ASTNode ast;

        while(true) {
            
            System.out.println("Enter your expression to evaluate." + ANSI_BLUE + "\t" + UnaryOperatorNode.angleMode.name() + " | " + NumberNode.decimalMode.name() + ANSI_RESET);

            String inputString;
            inputString = scanner.nextLine();
            
            try {

                ast = parser.parse(inputString);
                
                if (ast != null) {
                    Number result = ast.evaluate();
                    if (result != null) {
                        System.out.println("= " + NumberNode.formatNumber(result));
                    } 
                } else {
                    break;
                }

            } catch (SyntaxException se) {
                System.out.println(se.getMessage());
            }
        }
            
        scanner.close();
    }

    private static void printHelpMessage() {
        System.out.println("This is an unhelpful help message!");
    }

    @SuppressWarnings("unused")
    private static void testLexer(String inputString) {
        Lexer lexer = new Lexer(inputString);
        while (lexer.hasMoreTokens())
            System.out.println(lexer.getNextToken());
    }

}
