package ast;

public class PrintStmt extends Stmt {
    private final Expr expression;

    public PrintStmt(Expr expression) {
        this.expression = expression;
    }

    public Expr getExpression() { return expression; }
}