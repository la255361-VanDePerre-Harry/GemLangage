package ast;

/**
 * Represents a standalone expression executed as a statement in the GEM AST (e.g., standalone function calls).
 * <p>
 * Evaluates an inner expression solely for its runtime side effects (such as procedure execution)
 * rather than yielding a usable return value.
 * </p>
 *
 * @author Van De Perre Harry
 * @version 1.0
 */
public class ExpressionStmt extends Stmt {
    /** The inner expression evaluated by this statement. */
    private final Expr expression;

    /**
     * Constructs a new {@code ExpressionStmt} node.
     *
     * @param expression The {@link Expr} node to evaluate as a statement.
     */
    public ExpressionStmt(Expr expression) {
        this.expression = expression;
    }

    /**
     * Retrieves the inner expression AST node.
     *
     * @return The enclosed {@link Expr} node.
     */
    public Expr getExpression() {
        return expression;
    }
}