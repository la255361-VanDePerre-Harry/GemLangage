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
public class LogicalExpr extends Expr{
    private final Expr left;
    // representation of && or ||
    private final Token operator;
    private final Expr right;

    public LogicalExpr(Expr left, Token operator, Expr right) {
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
