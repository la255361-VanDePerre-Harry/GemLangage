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
public class AssignStmt extends Stmt{
    /** The identifier token representing the variable target being modified. */
    private final Token name;
    /** The expression evaluating to the new value to assign. */
    private final Expr value;

    public AssignStmt(Token name, Expr value) {
        this.name = name;
        this.value = value;
    }

    public Token getName() {
        return name;
    }

    public Expr getValue() {
        return value;
    }
}
