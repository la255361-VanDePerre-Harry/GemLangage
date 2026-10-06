package ast;

import lexer.Token;

/**
 * Represents a return statement node ({@code return}) in the GEM Abstract Syntax Tree (AST).
 * <p>
 * Signals the termination of function execution and carries an optional evaluated
 * return expression back to the call site.
 * </p>
 *
 * @author Van De Perre Harry
 * @version 1.0
 */
public class ReturnStmt extends Stmt {
    /** The 'return' keyword token, retained for source position tracking. */
    private final Token keyword;

    /** The expression evaluated and returned by the function, or {@code null} if returning void. */
    private final Expr value;

    /**
     * Constructs a new {@code ReturnStmt} node.
     *
     * @param keyword The 'return' keyword {@link Token}.
     * @param value   The {@link Expr} node to return, or {@code null} if returning without a value.
     */
    public ReturnStmt(Token keyword, Expr value) {
        this.keyword = keyword;
        this.value = value;
    }

    /**
     * Retrieves the 'return' keyword token.
     *
     * @return The keyword {@link Token}.
     */
    public Token getKeyword() {
        return keyword;
    }

    /**
     * Retrieves the return expression value node.
     *
     * @return The {@link Expr} node to return, or {@code null} if no expression was provided.
     */
    public Expr getValue() {
        return value;
    }
}