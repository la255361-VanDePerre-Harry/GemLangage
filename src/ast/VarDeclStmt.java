package ast;

import lexer.Token;

public class VarDeclStmt extends Stmt {
    private final Token name;
    private final Token typeToken;
    private final Expr initializer;
    private final boolean isConstant;

    public VarDeclStmt(Token name, Token typeToken, Expr initializer, boolean isConstant) {
        this.name = name;
        this.typeToken = typeToken;
        this.initializer = initializer;
        this.isConstant = isConstant;
    }

    public Token getName() { return name; }
    public Token getTypeToken() { return typeToken; }
    public Expr getInitializer() { return initializer; }
    public boolean isConstant() { return isConstant; }
}