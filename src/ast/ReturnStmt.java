package ast;

import lexer.Token;

public class ReturnStmt extends Stmt {
    // Return key
    private final Token keyword;
    // expression valued by return
    private final Expr value;

    public ReturnStmt(Token keyword, Expr value) {
        this.keyword = keyword;
        this.value = value;
    }

    public Token getKeyword() {
        return keyword;
    }

    public Expr getValue() {
        return value;
    }
}
