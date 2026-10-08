package ast;

import lexer.Token;

public class ContinueStmt extends Stmt {
    private final Token keyword;

    public ContinueStmt(Token keyword) {
        this.keyword = keyword;
    }

    public Token getKeyword() {
        return keyword;
    }
}
