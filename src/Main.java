import ast.Stmt;
import interpreter.Interpreter;
import lexer.Lexer;
import lexer.Token;
import parser.Parser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        try {
            // 1. Lecture du code source GEM
            String codeGEM = Files.readString(Path.of("test.gem"));

            // 2. Lexing : Analyse lexicale
            Lexer lexer = new Lexer(codeGEM);
            List<Token> tokens = lexer.scanTokens();

            // 3. Parsing : Analyse syntaxique (construction de l'AST)
            Parser parser = new Parser(tokens);
            List<Stmt> ast = parser.parse();

            // 4. Interprétation : Exécution dynamique du programme GEM
            System.out.println("=== Exécution GEM ===");
            Interpreter interpreter = new Interpreter();
            interpreter.interpret(ast);

        } catch (IOException e) {
            System.err.println("Erreur de lecture du fichier : " + e.getMessage());
        } catch (RuntimeException e) {
            System.err.println("Erreur d'exécution : " + e.getMessage());
        }
    }
}