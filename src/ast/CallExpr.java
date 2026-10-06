package ast;

import lexer.Token;

import java.util.List;

/**
 * Represents a function call expression node in the GEM Abstract Syntax Tree (AST).
 * <p>
 * Evaluates the argument expressions and invokes the target function identified by {@code callee}.
 * </p>
 *
 * @author Van De Perre Harry
 * @version 1.0
 */
public class CallExpr extends Expr {
    /** The token identifying the name of the function being called. */
    private final Token callee;

    /** The list of evaluated argument expressions passed to the function call. */
    private final List<Expr> arguments;

    /**
     * Constructs a new {@code CallExpr} node.
     *
     * @param callee    The identifier {@link Token} specifying the target function name.
     * @param arguments The {@link List} of {@link Expr} argument nodes.
     */
    public CallExpr(Token callee, List<Expr> arguments) {
        this.callee = callee;
        this.arguments = arguments;
    }

    /**
     * Retrieves the function identifier token.
     *
     * @return The callee {@link Token}.
     */
    public Token getCallee() {
        return callee;
    }

    /**
     * Retrieves the list of argument expressions.
     *
     * @return A {@link List} of argument {@link Expr} nodes.
     */
    public List<Expr> getArguments() {
        return arguments;
    }
}