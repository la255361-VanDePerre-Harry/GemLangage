package ast;

import lexer.Token;

public class VariableExpr extends Expr{
    private final Token name;

    public VariableExpr(Token name) {
        this.name = name;
    }

    public Token getName() {
        return name;
    }
}
