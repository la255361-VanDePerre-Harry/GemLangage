package ast;

public class ForStmt extends Stmt {
    private final Stmt initializer;
    private final Expr condition;
    private final Stmt increment;
    private final Stmt body;

    public ForStmt(Stmt initializer, Expr condition, Stmt increment, Stmt body) {
        this.initializer = initializer;
        this.condition = condition;
        this.increment = increment;
        this.body = body;
    }

    public Stmt getInitializer() { return initializer; }
    public Expr getCondition() { return condition; }
    public Stmt getIncrement() { return increment; }
    public Stmt getBody() { return body; }
}