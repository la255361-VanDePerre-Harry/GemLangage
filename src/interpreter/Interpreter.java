package interpreter;

import ast.*;
import lexer.Token;
import lexer.TokenType;

import java.util.List;
import java.util.function.UnaryOperator;

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
    private Environment environment = new Environment(null);

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

            // Expression Statement (ex: appel de fonction seul 'greet(...)')
            case ExpressionStmt exprStmt -> evaluate(exprStmt.getExpression());

            // Function Declaration Statement
            case FunctionStmt function -> {
                GemFunction gemFunction = new GemFunction(function);
                environment.define(function.getName().getLexeme(), gemFunction);
            }

            // Return Statement
            case ReturnStmt returnStmt -> {
                Object value = null;
                if (returnStmt.getValue() != null) {
                    value = evaluate(returnStmt.getValue());
                }
                throw new ReturnException(value);
            }

            // WHILE Statement
            case WhileStmt whileStmt -> {
                while (isTruthy(evaluate(whileStmt.getCondition()))) {
                    execute(whileStmt.getBody());
                }
            }

            // IF Statement
            case IfStmt ifStmt -> {
                Object conditionValue = evaluate(ifStmt.getCondition());

                if (isTruthy(conditionValue)) execute(ifStmt.getThenBranch());

                else if (ifStmt.getElseBranch() != null) execute(ifStmt.getElseBranch());


            }

            // Block Statement ({ ... })
            case BlockStmt blockStmt -> executeBlock(blockStmt.getStatements(), new Environment(this.environment));
            
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
        return switch (expr) {
            case LiteralExpr literal -> parseLiteralValue(literal.getValue());

            case VariableExpr variable -> environment.get(variable.getName());

            case UnaryExpr unary -> {
                Object right = evaluate(unary.getRight());
                if (unary.getOperator().getType() == TokenType.BANG) yield !isTruthy(right);

                if (unary.getOperator().getType() == TokenType.MINUS) yield -(int) right;

                throw new RuntimeException("Ligne " + unary.getOperator().getLine() +
                        " : Opérateur unaire non supporté '" + unary.getOperator().getLexeme() + "'");
            }

            case LogicalExpr logical -> {
                Object left = evaluate(logical.getLeft());

                // Evaluation en court-circuit (Short-circuit evaluation)
                if (logical.getOperator().getType() == TokenType.OR_OR) {
                    if (isTruthy(left)) yield true;
                } else {
                    if (!isTruthy(left)) yield false;
                }

                yield isTruthy(evaluate(logical.getRight()));
            }

            case BinaryExpr binary -> {
                Object left = evaluate(binary.getLeft());
                Object right = evaluate(binary.getRight());
                yield evaluateBinary(binary.getOperator(), left, right);
            }

            case CallExpr call -> {
                Object callee = environment.get(call.getCallee());

                if (!(callee instanceof GemFunction function)) {
                    throw new RuntimeException("Ligne " + call.getCallee().getLine() +
                            " : L'identifiant '" + call.getCallee().getLexeme() + "' n'est pas une fonction.");
                }

                List<Object> arguments = new java.util.ArrayList<>();
                for (Expr argument : call.getArguments()) {
                    arguments.add(evaluate(argument));
                }

                if (arguments.size() != function.arity()) {
                    throw new RuntimeException("Ligne " + call.getCallee().getLine() +
                            " : La fonction '" + call.getCallee().getLexeme() + "' attend " +
                            function.arity() + " arguments mais en a reçu " + arguments.size() + ".");
                }

                yield function.call(this, arguments);
            }

            default -> throw new RuntimeException("Expression non supportée à l'exécution : " + expr.getClass().getSimpleName());
        };
    }

    /**
     * Converts raw literal string representations from the Lexer into typed runtime Java objects.
     * <p>
     * Translates boolean string values ("true", "false") to {@link Boolean} instances and
     * numeric string values to {@link Integer} instances, preserving raw string values otherwise.
     * </p>
     *
     * @param rawValue The raw literal value object to parse (typically a {@link String} from the Lexer).
     * @return The parsed runtime object ({@link Boolean}, {@link Integer}, or {@link String}).
     */
    private Object parseLiteralValue(Object rawValue) {
        if (rawValue instanceof  String str) {

            // Parse boolean literals
            if (str.equals("true")) return Boolean.TRUE;
            if (str.equals("false")) return Boolean.FALSE;

            // Parse integer numeric literals
            try {
                return Integer.parseInt(str);
            } catch (NumberFormatException ignored) {
                // not an integer, keep it string
            }
        }
        return rawValue;
    }

    /**
     * Executes binary operations including arithmetic, string concatenation, equality, and relational comparisons.
     * <p>
     * Evaluates operand types and applies the appropriate operation according to the GEM language specifications.
     * </p>
     *
     * @param operator The {@link Token} representing the binary operator.
     * @param left     The evaluated runtime value of the left-hand operand.
     * @param right    The evaluated runtime value of the right-hand operand.
     * @return The resulting evaluated runtime object ({@link Integer}, {@link Boolean}, or {@link String}).
     * @throws RuntimeException If an operator is executed on incompatible operand types or if division by zero occurs.
     */
    private Object evaluateBinary(Token operator, Object left, Object right) {
        // String concatenation rule
        if (operator.getType() == TokenType.PLUS && (left instanceof String || right instanceof String)) return String.valueOf(left) + String.valueOf(right);

        // Equality comparison
        if (operator.getType() == TokenType.EQUAL) {
            return java.util.Objects.equals(left, right);
        }

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

                case GREATER -> lInt > rInt;
                case GREATER_EQUAL -> lInt >= rInt;
                case LESS -> lInt < rInt;
                case LESS_EQUAL -> lInt <= rInt;

                default -> throw new RuntimeException("Ligne " + operator.getLine() + " : Opérateur binaire non supporté.");

            };
        }

        throw new RuntimeException("Ligne " + operator.getLine() + " : Opérandes incompatibles pour l'opération '" + operator.getLexeme() + "'.");
    }

    /**
     * Executes a list of statements within a new scoped environment.
     * <p>
     * <b>Preconditions:</b> {@code statements} and {@code environment} must not be {@code null}.<br>
     * <b>Postconditions:</b> Restores the previous enclosing environment upon completion or exception.
     * </p>
     *
     * @param statements  The {@link List} of {@link Stmt} nodes to execute inside the block.
     * @param environment The local {@link Environment} scoped specifically for this block.
     */
    public void executeBlock(List<Stmt> statements, Environment environment) {
        Environment previous = this.environment;

        try {
            this.environment = environment;
            for (Stmt statement : statements) {
                execute(statement);
            }
        } finally {
            this.environment = previous;
        }
    }

    /**
     * Asserts that a runtime value evaluated as a condition is strictly a {@link Boolean}.
     *
     * @param object The evaluated runtime value to validate.
     * @return The primitive {@code boolean} value.
     * @throws RuntimeException If the condition value is not a {@link Boolean}.
     */
    private boolean isTruthy(Object object) {
        if (object instanceof Boolean b) return  b;

        throw new RuntimeException("La condition d'une instruction 'if' doit être de type booléen.");
    }

    /**
     * Resolves and returns the root global runtime environment.
     * <p>
     * Traverses up the scope hierarchy chain until reaching an environment without an enclosing parent.
     * </p>
     *
     * @return The root global {@link Environment} instance.
     */
    public Environment getGlobals() {
        Environment current = this.environment;
        while (current.getParent() != null) {
            current = current.getParent();
        }
        return current;
    }
}
