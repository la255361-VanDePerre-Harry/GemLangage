package ast;

import lexer.Token;

/**
 * Represents a unary expression node (e.g., {@code !} or unary {@code -}) in the AST.
 * <p>
 * Applies a single operator to a right-hand operand expression.
 * </p>

 * @author Van De Perre Harry
 * @version 1.0
 */
public class UnaryExpr extends Expr {
    /** The operator token (e.g., {@code TokenType.BANG} or {@code TokenType.MINUS}). */
    private final Token operator;
    /** The operand expression. */
    private final Expr right;


    public UnaryExpr(Token operator, Expr right) {
        this.operator = operator;
        this.right = right;
    }


    public Token getOperator() {
        return operator;
    }


    public Expr getRight() {
        return right;
    }
}