package lexer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * Performs lexical analysis (scanning) on GEM source code.
 * <p>
 * The {@code Lexer} processes raw source text character-by-character and converts it
 * into a sequential stream of {@link Token} instances, handling reserved keywords,
 * identifiers, numeric literals, enclosed string literals, and language operators.
 * </p>
 *
 * @author GEM Compiler Team
 * @version 1.0
 */
public class Lexer {
    /** The raw source code input string to be scanned. */
    private String input;
    /** The accumulated list of scanned tokens. */
    private List<Token> tokens;
    /** The character offset pointing to the start of the token currently being scanned. */
    private int start;
    /** The character offset pointing to the character currently being inspected. */
    private int current;
    /** The current line number in the source code (1-indexed), tracked for error reporting. */
    private int line = 1;

    /** Static map associating language keyword strings with their corresponding {@link TokenType}. */
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

    /**
     * Scans the complete source string and converts it into a list of tokens.
     * <p>
     * Appends an {@link TokenType#EOF} token at the end of the list to signal the end
     * of the token stream to the parser.
     * </p>
     *
     * @return A {@link List} of scanned {@link Token} instances.
     */
    public List<Token> scanTokens() {
        while(!isAtEnd()) {
            start = current;
            scanToken();
        }
        tokens.add(new Token(TokenType.EOF, "", this.line));

        return tokens;
    }

    /**
     * Scans a single token starting at the current offset.
     * <p>
     * Consumes the next character and dispatches token generation based on match cases,
     * handling single-character symbols, multi-character operators, whitespace,
     * numeric values, identifiers, and string literals.
     * </p>
     */
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


    /**
     * Conditionally consumes the current character if it matches the expected character.
     *
     * @param expected The character expected at the current scanning position.
     * @return {@code true} if the character matches and was consumed; {@code false} otherwise.
     */
    private boolean match(char expected) {
        if (isAtEnd()) return false;
        if (input.charAt(current) != expected) return false;

        current++;
        return true;
    }

    /**
     * Checks if the scanner has consumed all characters in the source input.
     *
     * @return {@code true} if current index is greater than or equal to input length; {@code false} otherwise.
     */
    private boolean isAtEnd() {
        return this.current >= this.input.length();
    }

    /**
     * Consumes and returns the current character, advancing the offset pointer by one.
     *
     * @return The consumed character.
     */
    private char advance() {
        char caract = this.input.charAt(this.current);
        this.current++;

        return caract;
    }

    /**
     * Helper method that extracts the substring from {@code start} to {@code current}
     * and adds a new token of the specified type.
     *
     * @param type The {@link TokenType} classification to assign.
     */
    private void addToken(TokenType type) {
        String text = input.substring(start, current);

        addToken(type, text);
    }

    /**
     * Constructs and appends a new {@link Token} to the token list.
     *
     * @param type   The {@link TokenType} classification.
     * @param lexeme The raw or processed string value of the token.
     */
    private void addToken(TokenType type, String lexeme) {
        Token token = new Token(type,lexeme, this.line);

        tokens.add(token);
    }

    /**
     * Returns the current character without advancing the offset pointer (lookahead).
     *
     * @return The character at the current position, or {@code '\0'} if at end of input.
     */
    private char peek() {
        if (isAtEnd()) return '\0';

        return this.input.charAt(this.current);
    }

    /**
     * Scans an identifier or reserved keyword sequence.
     * <p>
     * Consumes alphanumeric characters and underscores, extracts the resulting sequence,
     * and checks against the {@code keywords} map to assign either a keyword type
     * or {@link TokenType#IDENTIFIER}.
     * </p>
     */
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

    /**
     * Scans a sequence of numeric digits and emits a {@link TokenType#NUMBER} token.
     */
    private void number() {
        while (Character.isDigit(peek())) {
            advance();
        }

        addToken(TokenType.NUMBER);
    }

    /**
     * Scans a double-quoted string literal.
     * <p>
     * Consumes characters until the closing quote is reached, stripping surrounding double quotes
     * before emitting a {@link TokenType#STRING} token. Supports multi-line strings by incrementing
     * {@code line} on newlines.
     * </p>
     */
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
