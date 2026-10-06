package ast;

/**
 * Represents a standalone expression executed as a statement (e.g., function calls).
 */
public class ExpressionStmt extends Stmt {
    private final Expr expression;

    public ExpressionStmt(Expr expression) {
        this.expression = expression;
    }

    public Expr getExpression() {
        return expression;
    }
}
