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
public class WhileStmt extends Stmt{
    private final Expr condition;
    private final Stmt body;

    public WhileStmt(Expr condition, Stmt body) {
        this.condition = condition;
        this.body = body;
    }

    public Expr getCondition() {
        return condition;
    }

    public Stmt getBody() {
        return body;
    }
}
