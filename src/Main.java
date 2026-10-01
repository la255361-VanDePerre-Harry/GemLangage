import lexer.Lexer;
import lexer.Token;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        try {
            String codeGEM = Files.readString(Path.of("test.gem"));


            // Lecture du gem
            Lexer lexer = new Lexer(codeGEM);
            List<Token> tokens = lexer.scanTokens();

            for (Token token : tokens) {
                System.out.println(token);
            }
        } catch (IOException e) {
            System.err.println("Impossible de lire le fichier .gem : " + e.getMessage());
        }
    }
}