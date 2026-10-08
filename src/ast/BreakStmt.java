package ast;

import lexer.Token;

public class BreakStmt extends Stmt {
    private final Token keyword;

    public BreakStmt(Token keyword) {
        this.keyword = keyword;
    }

    public Token getKeyword() {
        return keyword;
    }
}
