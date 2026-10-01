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

    public VarDeclStmt(Token name, Token typeToken, Expr initializer, boolean isConstant) {
        this.name = name;
        this.typeToken = typeToken;
        this.initializer = initializer;
        this.isConstant = isConstant;
    }

    public Token getName() { return name; }
    public Token getTypeToken() { return typeToken; }
    public Expr getInitializer() { return initializer; }
    public boolean isConstant() { return isConstant; }
}