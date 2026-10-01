package ast;

/**
 * Represents a print statement node in the GEM Abstract Syntax Tree (AST).
 * <p>
 * A print statement evaluates an inner expression and outputs the resulting value
 * to the standard output console, following the syntax: {@code print expression}.
 * </p>
 *
 * @author Van De Perre Harry
 * @version 1.0
 */
public class PrintStmt extends Stmt {
    /** The expression whose evaluated result will be printed to the standard console. */
    private final Expr expression;

    public PrintStmt(Expr expression) {
        this.expression = expression;
    }

    public Expr getExpression() { return expression; }
}