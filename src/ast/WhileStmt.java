package ast;

/**
 * Represents an iterative loop statement node ({@code while}) in the AST.
 * <p>
 * Repeatedly evaluates a condition expression and executes the contained
 * {@code body} statement or block as long as the condition evaluates to {@code true}.
 * </p>
 *
 * @author Van De Perre Harry
 * @version 1.0
 */
public class WhileStmt extends Stmt {
    /** The condition expression evaluated before each iteration. */
    private final Expr condition;

    /** The body statement or block executed when the condition evaluates to {@code true}. */
    private final Stmt body;

    /**
     * Constructs a new {@code WhileStmt} node.
     *
     * @param condition The {@link Expr} condition controlling the loop execution.
     * @param body      The {@link Stmt} body executed on each true iteration.
     */
    public WhileStmt(Expr condition, Stmt body) {
        this.condition = condition;
        this.body = body;
    }

    /**
     * Retrieves the loop condition expression.
     *
     * @return The condition {@link Expr} node.
     */
    public Expr getCondition() {
        return condition;
    }

    /**
     * Retrieves the loop body statement.
     *
     * @return The body {@link Stmt} node.
     */
    public Stmt getBody() {
        return body;
    }
}