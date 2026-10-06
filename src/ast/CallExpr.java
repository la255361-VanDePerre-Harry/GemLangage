package ast;

import lexer.Token;

import java.util.List;

public class CallExpr extends Expr {
    // name of called function
    private final Token callee;
    // parameters list into ()
    private final List<Expr> arguments;

    public CallExpr(Token callee, List<Expr> arguments) {
        this.callee = callee;
        this.arguments = arguments;
    }

    public Token getCallee() {
        return callee;
    }

    public List<Expr> getArguments() {
        return arguments;
    }
}
