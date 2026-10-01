package ast;

import lexer.Token;

public class AssignStmt extends Stmt{
    private final Token name;
    private final Expr value;

    public AssignStmt(Token name, Expr value) {
        this.name = name;
        this.value = value;
    }

    public Token getName() {
        return name;
    }

    public Expr getValue() {
        return value;
    }
}
