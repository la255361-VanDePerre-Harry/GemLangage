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
public class BinaryExpr extends Expr{
    /** The left-hand side operand expression. */
    private final Expr left;
    /** The operator token (e.g., +, -, *, /). */
    private final Token operator;
    /** The right-hand side operand expression. */
    private final Expr right;

    public BinaryExpr(Expr left, Token operator, Expr right) {
        this.left = left;
        this.operator = operator;
        this.right = right;
    }

    public Expr getLeft() {
        return left;
    }

    public Token getOperator() {
        return operator;
    }

    public Expr getRight() {
        return right;
    }
}
