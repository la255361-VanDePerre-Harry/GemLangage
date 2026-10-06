package ast;

import lexer.Token;

import java.util.List;

public class FunctionStmt extends Stmt {
    // function name
    private final Token name;
    // parameters name
    private final List<Token> parameters;
    // body instructions
    private final List<Stmt> body;

    public FunctionStmt(Token name, List<Token> parameters, List<Stmt> body) {
        this.name = name;
        this.parameters = parameters;
        this.body = body;
    }

    public Token getName() {
        return name;
    }

    public List<Token> getParameters() {
        return parameters;
    }

    public List<Stmt> getBody() {
        return body;
    }
}
