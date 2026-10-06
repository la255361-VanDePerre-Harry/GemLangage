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
public class IfStmt extends Stmt {
    /** The boolean condition expression controlling branch execution. */
    private final Expr condition;

    /** The statement executed when the condition evaluates to {@code true}. */
    private final Stmt thenBranch;

    /** The optional statement executed when the condition evaluates to {@code false} (may be {@code null}). */
    private final Stmt elseBranch;

    /**
     * Constructs a new {@code IfStmt} node.
     *
     * @param condition  The {@link Expr} condition controlling execution flow.
     * @param thenBranch The {@link Stmt} executed if condition evaluates to {@code true}.
     * @param elseBranch The optional {@link Stmt} executed if condition evaluates to {@code false} (or {@code null}).
     */
    public IfStmt(Expr condition, Stmt thenBranch, Stmt elseBranch) {
        this.condition = condition;
        this.thenBranch = thenBranch;
        this.elseBranch = elseBranch;
    }

    /**
     * Retrieves the condition expression.
     *
     * @return The condition {@link Expr} node.
     */
    public Expr getCondition() {
        return condition;
    }

    /**
     * Retrieves the statement for the 'then' execution branch.
     *
     * @return The then-branch {@link Stmt} node.
     */
    public Stmt getThenBranch() {
        return thenBranch;
    }

    /**
     * Retrieves the optional statement for the 'else' execution branch.
     *
     * @return The else-branch {@link Stmt} node, or {@code null} if no else branch was declared.
     */
    public Stmt getElseBranch() {
        return elseBranch;
    }
}