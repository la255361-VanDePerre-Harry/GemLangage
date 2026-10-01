package interpreter;

import ast.*;

import java.util.List;

public class Interpreter {
    private final Environment environment = new Environment();

    public void interpret(List<Stmt> statements) {
        for (Stmt statement : statements) {
            execute(statement);
        }
    }

    private void execute(Stmt stmt) {
        switch (stmt) {
            case VarDeclStmt decl -> {
                Object value = evaluate(decl.getInitializer());
                String name = decl.getName().getLexeme();

                environment.define(name, value, decl.isConstant());
            }
            case PrintStmt printStmt -> {
                Object value = evaluate(printStmt.getExpression());
                System.out.println(value);
            }
            case AssignStmt assign -> {
                Object value = evaluate(assign.getValue());
                environment.assign(assign.getName(), value);
            }
            case null, default -> throw new RuntimeException("Type d'instruction non supporté à l'exécution.");
        }
    }

    private Object evaluate(Expr expr) {
        if (expr instanceof LiteralExpr literal) return literal.getValue();

        if (expr instanceof VariableExpr variable) return environment.get(variable.getName());

        throw new RuntimeException("Expression non supportée à l'exécution : " + expr.getClass().getSimpleName());
    }
}
