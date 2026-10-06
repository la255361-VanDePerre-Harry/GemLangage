package interpreter;

import ast.FunctionStmt;

import java.util.List;

/**
 * Represents a user-defined function object at runtime in GEM.
 * <p>
 * Manages invocation logic by constructing a dedicated local lexical environment,
 * binding argument values to parameters, and executing the function body.
 * </p>
 *
 * @author Van De Perre Harry
 * @version 1.0
 */
public class GemFunction {
    /** The AST statement node defining this function signature and body. */
    private final FunctionStmt declaration;

    public GemFunction(FunctionStmt declaration) {
        this.declaration = declaration;
    }

    /**
     * Executes the function body within a new local scoped environment.
     *
     * @param interpreter The active {@link Interpreter} instance.
     * @param arguments   The list of evaluated argument objects passed to the call.
     * @return The evaluated return value {@link Object}, or {@code null} if no explicit return occurred.
     */
    public Object call(Interpreter interpreter, List<Object> arguments) {
        Environment environment = new Environment(interpreter.getGlobals());
    }

    /**
     * Returns the expected number of arguments for this function.
     *
     * @return The arity count integer.
     */
    public int arity() {
        return declaration.getParameters().size();
    }
}
