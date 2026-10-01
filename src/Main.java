import ast.*;
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
            String codeGEM = Files.readString(Path.of("test.gem"));

            Lexer lexer = new Lexer(codeGEM);
            List<Token> tokens = lexer.scanTokens();

            Parser parser = new Parser(tokens);
            List<Stmt> ast = parser.parse();

            System.out.println("=== AST Généré avec succès ! ===");
            for (Stmt stmt : ast) {
                if (stmt instanceof VarDeclStmt decl) {
                    System.out.println("• Déclaration " + (decl.isConstant() ? "[CONST]" : "[MUT]")
                            + " -> Name: " + decl.getName().getLexeme()
                            + " | Type: " + decl.getTypeToken().getLexeme());
                } else if (stmt instanceof PrintStmt print) {
                    System.out.println("• Instruction PRINT");
                }
            }

        } catch (IOException e) {
            System.err.println("Erreur de lecture du fichier : " + e.getMessage());
        } catch (RuntimeException e) {
            System.err.println("Erreur de compilation : " + e.getMessage());
        }
    }
}