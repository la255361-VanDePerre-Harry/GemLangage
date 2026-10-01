package interpreter;

import ast.*;
import lexer.Token;
import lexer.TokenType;

import java.util.List;

/**
 * Executes GEM Abstract Syntax Tree (AST) statement nodes and evaluates expressions.
 * <p>
 * The {@code Interpreter} drives the runtime execution of a GEM program. It traverses AST
 * statements sequentially, maintaining program state via an internal {@link Environment}
 * and performing expression evaluations.
 * </p>
 *
 * @author Van De Perre Harry
 * @version 1.0
 */
public class Interpreter {
    /** Global runtime memory environment storing variable and constant bindings. */
    private final Environment environment = new Environment();

    /**
     * Executes a complete program represented as a sequential list of AST statements.
     * <p>
     * <b>Preconditions:</b> The statement list must not be {@code null}.<br>
     * <b>Postconditions:</b> Statements are executed sequentially in order of appearance.
     * </p>
     *
     * @param statements The list of {@link Stmt} nodes to execute.
     */
    public void interpret(List<Stmt> statements) {
        for (Stmt statement : statements) {
            execute(statement);
        }
    }

    /**
     * Executes a single statement node using pattern matching on the statement type.
     * <p>
     * Handles variable declarations, print output operations, and variable reassignments.
     * </p>
     *
     * @param stmt The {@link Stmt} node to execute.
     * @throws RuntimeException If an unsupported statement type or null reference is encountered.
     */
    private void execute(Stmt stmt) {
        switch (stmt) {
            // Variable / Constant Declaration
            case VarDeclStmt decl -> {
                Object value = evaluate(decl.getInitializer());
                String name = decl.getName().getLexeme();

                environment.define(name, value, decl.isConstant());
            }

            // Output Print Statement
            case PrintStmt printStmt -> {
                Object value = evaluate(printStmt.getExpression());
                System.out.println(value);
            }

            // Variable Reassignment
            case AssignStmt assign -> {
                Object value = evaluate(assign.getValue());
                environment.assign(assign.getName(), value);
            }

            // Unsupported or invalid AST statement node
            case null, default -> throw new RuntimeException("Type d'instruction non supporté à l'exécution.");
        }
    }

    /**
     * Evaluates an AST expression node and returns its resulting runtime value object.
     * <p>
     * <b>Preconditions:</b> {@code expr} must be a valid expression node.<br>
     * <b>Postconditions:</b> Returns a primitive value (e.g., String, Integer, Boolean) or variable look-up result.
     * </p>
     *
     * @param expr The {@link Expr} node to evaluate (must not be {@code null}).
     * @return The evaluated runtime value {@link Object}.
     * @throws RuntimeException If the expression node type is not supported by the evaluator.
     */
    private Object evaluate(Expr expr) {
        // Literal values
        if (expr instanceof LiteralExpr literal) return literal.getValue();

        // Variable identifier references
        if (expr instanceof VariableExpr variable) return environment.get(variable.getName());

        // Evaluation of Binary Operations
        if (expr instanceof BinaryExpr binary) {
            Object left = evaluate(binary.getLeft());
            Object right = evaluate(binary.getRight());

            return evaluateBinary(binary.getOperator(), left, right);
        }

        throw new RuntimeException("Expression non supportée à l'exécution : " + expr.getClass().getSimpleName());
    }

    private Object parseLiteralValue(Object rawValue) {
        if (rawValue instanceof  String str) {
            try {
                return Integer.parseInt(str);
            } catch (NumberFormatException ignored) {
                // not an integer, keep it string
            }
        }
        return rawValue;
    }

    /**
    * Executes binary mathematical and string concatenation operations.}
    */
    private Object evaluateBinary(Token operator, Object left, Object right) {
        // String concatenation rule
        if (operator.getType() == TokenType.PLUS && (left instanceof String || right instanceof String)) return String.valueOf(left) + String.valueOf(right);

        // Numeric evaluation rules
        if (left instanceof Integer lInt && right instanceof Integer rInt) {
            return switch (operator.getType()) {
                case PLUS -> lInt + rInt;
                case MINUS -> lInt - rInt;
                case STAR -> lInt * rInt;
                case SLASH -> {
                    if (rInt == 0) throw new RuntimeException("Ligne " + operator.getLine() + " : Division par zéro.");

                    yield lInt / rInt;
                }

                default -> throw new RuntimeException("Ligne " + operator.getLine() + " : Opérateur binaire non supporté.");

            };
        }

        throw new RuntimeException("Ligne " + operator.getLine() + " : Opérandes incompatibles pour l'opération '" + operator.getLexeme() + "'.");
    }

}
