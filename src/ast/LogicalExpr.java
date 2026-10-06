package ast;

import lexer.Token;

/**
 * Represents a logical binary expression node ({@code &&} or {@code ||}) in the AST.
 * <p>
 * Evaluates short-circuit logical operation rules between two boolean operand expressions.
 * </p>
 *
 * @author Van De Perre Harry
 * @version 1.0
 */
public class LogicalExpr extends Expr {
    /** The left-hand operand expression. */
    private final Expr left;

    /** The logical operator token ({@code TokenType.AND_AND} or {@code TokenType.OR_OR}). */
    private final Token operator;

    /** The right-hand operand expression. */
    private final Expr right;

    /**
     * Constructs a new {@code LogicalExpr} node.
     *
     * @param left     The left-hand {@link Expr} operand.
     * @param operator The logical operator {@link Token}.
     * @param right    The right-hand {@link Expr} operand.
     */
    public LogicalExpr(Expr left, Token operator, Expr right) {
        this.left = left;
        this.operator = operator;
        this.right = right;
    }

    /**
     * Retrieves the left-hand operand expression.
     *
     * @return The left {@link Expr} node.
     */
    public Expr getLeft() {
        return left;
    }

    /**
     * Retrieves the logical operator token.
     *
     * @return The operator {@link Token}.
     */
    public Token getOperator() {
        return operator;
    }

    /**
     * Retrieves the right-hand operand expression.
     *
     * @return The right {@link Expr} node.
     */
    public Expr getRight() {
        return right;
    }
}