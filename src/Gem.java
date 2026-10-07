import ast.Stmt;
import interpreter.Interpreter;
import lexer.Lexer;
import lexer.Token;
import parser.Parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * Main entry point for the GEM programming language executable.
 * <p>
 * Manages both batch execution of .gem files and the line-by-line interactive REPL.
 * </p>
 *
 * @author Van De Perre Harry
 * @version 1.0
 */
public class Gem {
    private static final Interpreter interpreter = new Interpreter();
    private static boolean hadError = false;

    public static void main(String[] args) throws IOException {
        if (args.length > 1) {
            System.out.println("Usage: gem [script.gem]");
            System.exit(64);
        } else if (args.length == 1) {
            runFile(args[0]);
        } else {
            runPrompt();
        }
    }

    // File mode
    private static void runFile(String path) throws IOException {
        byte[] bytes = Files.readAllBytes(Paths.get(path));
        String source = new String(bytes, StandardCharsets.UTF_8);

        run(source);

        if (hadError) System.exit(65);
    }

    // REPL mode
    private static void runPrompt() throws IOException {
        InputStreamReader input = new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(input);

        System.out.println("GEM REPL");
        System.out.println("Type 'exit' to quit.\n");

        for (;;) {
            System.out.println("> ");
            String line = reader.readLine();

            if (line == null || line.equalsIgnoreCase("exit")) break;
            if (line.trim().isEmpty()) continue;

            run(line);
            hadError = false;

        }
    }

    private static void run(String source) {
        try {
            Lexer lexer = new Lexer(source);
            List<Token> tokens = lexer.scanTokens();

            Parser parser = new Parser(tokens);
            List<Stmt> statements = parser.parse();

            if (hadError) return;

            interpreter.interpret(statements);
        } catch (RuntimeException error) {
            System.err.println("[Error] " + error.getMessage());
            hadError = true;
        }
    }

}
