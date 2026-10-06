package interpreter;

import ast.FunctionStmt;
import lexer.Token;

import java.util.List;

/**
 * Represents a user-defined callable function object at runtime in GEM.
 * <p>
 * Manages invocation logic by constructing a dedicated local lexical environment,
 * binding argument values to formal parameters, and executing the statement block body.
 * Exceptions of type {@link ReturnException} are intercepted to handle explicit return values.
 * </p>
 *
 * @author Van De Perre Harry
 * @version 1.0
 */
public class GemFunction {
    /** The AST statement node defining this function's signature and body block. */
    private final FunctionStmt declaration;

    /**
     * Constructs a runtime representation of a declared GEM function.
     *
     * @param declaration The {@link FunctionStmt} AST node encapsulating name, parameters, and body.
     */
    public GemFunction(FunctionStmt declaration) {
        this.declaration = declaration;
    }

    /**
     * Invokes the function by creating a local scope and executing its body statements.
     * <p>
     * Binds each evaluated argument to its corresponding parameter identifier in a new local
     * {@link Environment}. Intercepts {@link ReturnException} to retrieve control-flow return values.
     * </p>
     *
     * @param interpreter The active {@link Interpreter} runtime context executing the program.
     * @param arguments   The list of evaluated argument {@link Object} values passed to the function call.
     * @return The evaluated return value {@link Object}, or {@code null} if no explicit return statement was executed.
     */
    public Object call(Interpreter interpreter, List<Object> arguments) {
        Environment environment = new Environment(interpreter.getGlobals());

        // Bind arguments to parameter names in local environment scope
        for (int i = 0; i < declaration.getParameters().size(); i++) {
            Token param = declaration.getParameters().get(i);
            Object value = arguments.get(i);
            environment.define(param.getLexeme(), value);
        }

        try {
            interpreter.executeBlock(declaration.getBody(), environment);
        } catch (ReturnException returnValue) {
            return returnValue.getValue();
        }

        return null;
    }

    /**
     * Returns the expected number of arguments (arity) declared for this function.
     *
     * @return The parameter count integer.
     */
    public int arity() {
        return declaration.getParameters().size();
    }
}