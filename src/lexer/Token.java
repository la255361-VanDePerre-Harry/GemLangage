package lexer;
/**
 * Represents an immutable lexical token produced by the {@link Lexer}.
 * <p>
 * A {@code Token} encapsulates a token type (from {@link TokenType}), the exact source code
 * textual substring (lexeme), and its corresponding line number in the source file for error reporting.
 * </p>
 *
 * @author Van De Perre Harry
 * @version 1.0
 */
public class Token {
    /** The category/type of this token. */
    private final TokenType type;
    /** The exact string representation from the source code. */
    private final String lexeme;
    /** The line number where this token appeared in the source file (1-indexed). */
    private final int line;

    /**
     * Constructs a new {@code Token} instance.
     *
     * @param type   The category of the token (must not be {@code null}).
     * @param lexeme The raw text extracted from the source file (must not be {@code null}).
     * @param line   The line number in the source file where the token starts (must be &gt;= 1).
     */
    public Token(TokenType type, String lexeme, int line) {
        this.type = type;
        this.lexeme = lexeme;
        this.line = line;
    }

    public TokenType getType() {
        return type;
    }

    public String getLexeme() {
        return lexeme;
    }

    public int getLine() {
        return line;
    }

    @Override
    public String toString() {
        return String.format("Ligne %d | Token(%s, '%s')", line, type, lexeme);
    }
}