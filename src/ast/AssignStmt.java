package ast;

import lexer.Token;

/**
 * Represents a variable assignment statement node in the GEM Abstract Syntax Tree (AST).
 * <p>
 * An assignment statement mutates the value bound to an existing variable name,
 * following the syntax: {@code identifier = expression}.
 * </p>
 *
 * @author Van De Perre Harry
 * @version 1.0
 */
public class AssignStmt extends Stmt {
    /** The identifier token representing the variable target being modified. */
    private final Token name;

    /** The expression evaluating to the new value to assign. */
    private final Expr value;

    /**
     * Constructs a new {@code AssignStmt} node.
     *
     * @param name  The identifier {@link Token} specifying the target variable.
     * @param value The {@link Expr} node evaluated to yield the assigned value.
     */
    public AssignStmt(Token name, Expr value) {
        this.name = name;
        this.value = value;
    }

    /**
     * Retrieves the target variable identifier token.
     *
     * @return The target variable {@link Token}.
     */
    public Token getName() {
        return name;
    }

    /**
     * Retrieves the expression being assigned to the variable.
     *
     * @return The assigned value {@link Expr} node.
     */
    public Expr getValue() {
        return value;
    }
}