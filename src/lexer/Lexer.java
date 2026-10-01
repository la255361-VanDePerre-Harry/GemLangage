package lexer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class Lexer {
    private String input;
    private List<Token> tokens;
    private int start;
    private int current;
    private int line = 1;

    private static final Map<String, TokenType> keywords;
    static {
        keywords = new HashMap<>();
        keywords.put("const", TokenType.CONST);
        keywords.put("mut", TokenType.MUT);
        keywords.put("if", TokenType.IF);
        keywords.put("else", TokenType.ELSE);
        keywords.put("while", TokenType.WHILE);
        keywords.put("print", TokenType.PRINT);
        keywords.put("int", TokenType.TYPE_INT);
        keywords.put("string", TokenType.TYPE_STRING);
        keywords.put("bool", TokenType.TYPE_BOOL);
        keywords.put("true", TokenType.BOOLEAN);
        keywords.put("false", TokenType.BOOLEAN);
    }


    public Lexer(String input) {
        this.input = input;
        this.tokens = new ArrayList<>();
    }

    public List<Token> scanTokens() {
        while(!isAtEnd()) {
            start = current;
            scanToken();
        }
        tokens.add(new Token(TokenType.EOF, "", this.line));

        return tokens;
    }

    private void scanToken() {
        char caract = advance();
        switch (caract) {
            case '(':
                addToken(TokenType.LPAREN);
                break;
            case ')':
                addToken(TokenType.RPAREN);
                break;
            case '{':
                addToken(TokenType.LBRACE);
                break;
            case '}':
                addToken(TokenType.RBRACE);
                break;
            case ':':
                addToken(TokenType.COLON);
                break;
            case '+':
                addToken(TokenType.PLUS);
                break;
            case '-':
                addToken(TokenType.MINUS);
                break;
            case '*':
                addToken(TokenType.STAR);
                break;
            case '/':
                addToken(TokenType.SLASH);
                break;
            case '=':
                addToken(match('=') ? TokenType.EQUAL : TokenType.ASSIGN);
                break;

            case ' ':
            case '\r':
            case '\t':
                break;

            case '\n':
                line++;
                break;
            case '"':
                string();
                break;


            default:
                if (Character.isDigit(caract)) {
                    number();
                } else if (Character.isLetter(caract) || caract == '_') {
                    identifier();
                } else {
                    System.err.println("Ligne " + line + " : Caractère inattendu '" + caract + "'");
                }
                break;

        }
    }


    private boolean match(char expected) {
        if (isAtEnd()) return false;
        if (input.charAt(current) != expected) return false;

        current++;
        return true;
    }

    private boolean isAtEnd() {
        return this.current >= this.input.length();
    }

    private char advance() {
        char caract = this.input.charAt(this.current);
        this.current++;

        return caract;
    }

    private void addToken(TokenType type) {
        String text = input.substring(start, current);

        addToken(type, text);
    }

    private void addToken(TokenType type, String lexeme) {
        Token token = new Token(type,lexeme, this.line);

        tokens.add(token);
    }

    private char peek() {
        if (isAtEnd()) return '\0';

        return this.input.charAt(this.current);
    }

    private void identifier() {
        while (Character.isLetterOrDigit(peek()) || peek() == '_') {
            advance();
        }
        String text = input.substring(start, current);

        TokenType type = keywords.get(text);

        if (type == null) {
            type = TokenType.IDENTIFIER;
        }

        addToken(type);
    }

    private void number() {
        while (Character.isDigit(peek())) {
            advance();
        }

        addToken(TokenType.NUMBER);
    }

    private void string() {
        while (peek() != '"' && !isAtEnd()) {
            if (peek() == '\n') line++;
            advance();
        }

        if (isAtEnd()) {
            System.err.println("Ligne " + line + " : Chaîne non terminée.");
            return;
        }

        advance();

        String value = this.input.substring(start + 1, current - 1);

        addToken(TokenType.STRING, value);
    }
}
