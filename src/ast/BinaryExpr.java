package ast;

import lexer.Token;

/**
 * Represents a binary expression node in the GEM Abstract Syntax Tree (AST).
 * <p>
 * A binary expression evaluates an operator combining two operand expressions,
 * following the structure: {@code leftOperand operator rightOperand} (e.g., {@code 10 + 5} or {@code x * 2}).
 * </p>
 *
 * @author Van De Perre Harry
 * @version 1.0
 */
public class BinaryExpr extends Expr {
    /** The left-hand side operand expression. */
    private final Expr left;

    /** The operator token (e.g., +, -, *, /). */
    private final Token operator;

    /** The right-hand side operand expression. */
    private final Expr right;

    /**
     * Constructs a new {@code BinaryExpr} node.
     *
     * @param left     The left-hand {@link Expr} operand.
     * @param operator The binary operator {@link Token}.
     * @param right    The right-hand {@link Expr} operand.
     */
    public BinaryExpr(Expr left, Token operator, Expr right) {
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
     * Retrieves the operator token for this binary expression.
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