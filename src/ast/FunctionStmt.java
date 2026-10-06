package ast;

import lexer.Token;

import java.util.List;

/**
 * Represents a function declaration statement node ({@code fn}) in the GEM Abstract Syntax Tree (AST).
 * <p>
 * Encapsulates the function's identifier name, formal parameter list, and execution body block.
 * </p>
 *
 * @author Van De Perre Harry
 * @version 1.0
 */
public class FunctionStmt extends Stmt {
    /** The identifier token representing the function name. */
    private final Token name;

    /** The list of parameter identifier tokens accepted by this function. */
    private final List<Token> parameters;

    /** The sequence of statements comprising the function body block. */
    private final List<Stmt> body;

    /**
     * Constructs a new {@code FunctionStmt} node.
     *
     * @param name       The identifier {@link Token} specifying the function name.
     * @param parameters The {@link List} of parameter identifier tokens.
     * @param body       The {@link List} of {@link Stmt} nodes in the function body.
     */
    public FunctionStmt(Token name, List<Token> parameters, List<Stmt> body) {
        this.name = name;
        this.parameters = parameters;
        this.body = body;
    }

    /**
     * Retrieves the function name identifier token.
     *
     * @return The function name {@link Token}.
     */
    public Token getName() {
        return name;
    }

    /**
     * Retrieves the list of declared formal parameter tokens.
     *
     * @return A {@link List} of parameter {@link Token} instances.
     */
    public List<Token> getParameters() {
        return parameters;
    }

    /**
     * Retrieves the list of statements comprising the function body.
     *
     * @return A {@link List} of {@link Stmt} nodes inside the function.
     */
    public List<Stmt> getBody() {
        return body;
    }
}