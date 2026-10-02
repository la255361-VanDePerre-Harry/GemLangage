package ast;

/**
 * Represents a conditional branching statement node ({@code if / else}) in the AST.
 * <p>
 * Evaluates a condition expression. If the condition resolves to {@code true},
 * the {@code thenBranch} statement is executed. Otherwise, if present,
 * the optional {@code elseBranch} statement is executed.
 * </p>
 *
 * @author Van De Perre Harry
 * @version 1.0
 */
public class IfStmt extends Stmt{
    private final Expr condition;
    private final Stmt thenBranch;
    private final Stmt elseBranch;

    public IfStmt(Expr condition, Stmt thenBranch, Stmt elseBranch) {
        this.condition = condition;
        this.thenBranch = thenBranch;
        this.elseBranch = elseBranch;
    }

    public Expr getCondition() {
        return condition;
    }

    public Stmt getThenBranch() {
        return thenBranch;
    }

    public Stmt getElseBranch() {
        return elseBranch;
    }
}
