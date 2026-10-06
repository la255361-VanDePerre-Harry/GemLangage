package ast;

import lexer.Token;

/**
 * Represents a variable or constant declaration statement node in the GEM Abstract Syntax Tree (AST).
 * <p>
 * Handles both mutable and immutable declarations, following the syntax:
 * {@code mut identifier : type = expression} or {@code const identifier : type = expression}.
 * </p>
 *
 * @author Van De Perre Harry
 * @version 1.0
 */
public class VarDeclStmt extends Stmt {
    /** The identifier token representing the variable or constant name. */
    private final Token name;

    /** The token specifying the declared data type (e.g., int, string, bool). */
    private final Token typeToken;

    /** The expression evaluated to initialize the variable upon declaration. */
    private final Expr initializer;

    /** Flag indicating whether the declaration is an immutable constant ({@code true}) or mutable ({@code false}). */
    private final boolean isConstant;

    /**
     * Constructs a new {@code VarDeclStmt} node.
     *
     * @param name        The identifier {@link Token} specifying the name.
     * @param typeToken   The type specifier {@link Token} (e.g., {@code int}, {@code string}, {@code bool}).
     * @param initializer The {@link Expr} node evaluated at declaration time.
     * @param isConstant  {@code true} if declared with 'const'; {@code false} if declared with 'mut'.
     */
    public VarDeclStmt(Token name, Token typeToken, Expr initializer, boolean isConstant) {
        this.name = name;
        this.typeToken = typeToken;
        this.initializer = initializer;
        this.isConstant = isConstant;
    }

    /**
     * Retrieves the identifier token of the declared variable or constant.
     *
     * @return The identifier {@link Token}.
     */
    public Token getName() {
        return name;
    }

    /**
     * Retrieves the declared explicit type token.
     *
     * @return The type specifier {@link Token}.
     */
    public Token getTypeToken() {
        return typeToken;
    }

    /**
     * Retrieves the initialization expression AST node.
     *
     * @return The initializer {@link Expr}.
     */
    public Expr getInitializer() {
        return initializer;
    }

    /**
     * Checks whether this node represents an immutable constant declaration.
     *
     * @return {@code true} if declared via 'const'; {@code false} if declared via 'mut'.
     */
    public boolean isConstant() {
        return isConstant;
    }
}