import lexer.Lexer;
import lexer.Token;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        String codeGEM = "const MAX: int\nmut count: bool = true";

        Lexer lexer = new Lexer(codeGEM);
        List<Token> tokens = lexer.scanTokens();

        for (Token token : tokens) {
            System.out.println(token);
        }
    }
}