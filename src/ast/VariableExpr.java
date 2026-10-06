package ast;

import lexer.Token;

/**
 * Represents a variable reference expression node in the GEM Abstract Syntax Tree (AST).
 * <p>
 * A variable expression resolves a bound identifier name to its associated runtime value
 * within the current execution scope or environment (e.g., accessing {@code message} in {@code print message}).
 * </p>
 *
 * @author Van De Perre Harry
 * @version 1.0
 */
public class VariableExpr extends Expr {
    /** The identifier token representing the variable being accessed. */
    private final Token name;

    /**
     * Constructs a new {@code VariableExpr} node.
     *
     * @param name The identifier {@link Token} specifying the variable name.
     */
    public VariableExpr(Token name) {
        this.name = name;
    }

    /**
     * Retrieves the identifier token associated with this variable reference.
     *
     * @return The {@link Token} object representing the variable name.
     */
    public Token getName() {
        return name;
    }
}