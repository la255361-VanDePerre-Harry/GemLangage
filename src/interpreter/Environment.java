package interpreter;

import lexer.Token;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Stores variable and constant bindings for a specific lexical scope.
 * <p>
 * Supports nested lexical scoping through an optional link to an enclosing parent
 * {@code Environment}. Variable lookups and reassignments automatically traverse up the
 * scope hierarchy until the binding is resolved or a runtime exception is raised.
 * </p>
 *
 * @author Van De Perre Harry
 * @version 1.0
 */
public class Environment {
    /** Map binding variable names (identifiers) to their stored runtime values. */
    private final Map<String, Object> values = new HashMap<>();
    /** Set tracking variable names declared as immutable constants ('const'). */
    private final Set<String> constants = new HashSet<>();
    /** Reference to the outer enclosing scope environment, or {@code null} if this is the global scope. */
    private final Environment enclosing;

    public Environment(){
        this.enclosing = null;
    }

    public Environment(Environment enclosing) {
        this.enclosing = enclosing;
    }


    /**
     * Binds a new symbol name to an initial value in the current environment scope.
     * <p>
     * <b>Preconditions:</b> The variable name must not already exist in the scope.<br>
     * <b>Postconditions:</b> The name is bound in {@code values}, and registered in {@code constants} if specified.
     * </p>

     * @param name       The identifier string of the variable or constant (must not be {@code null}).
     * @param value      The initial runtime value evaluated from the initializer expression.
     * @param isConstant {@code true} if declared with 'const'; {@code false} if declared with 'mut'.
     * @throws RuntimeException If a variable with the same identifier name is already defined in this environment.
     */
    public void define(String name, Object value, boolean isConstant) {
        // Enforce single-definition rule within the same scope to prevent redeclaration bugs.
        if(this.values.containsKey(name)) throw new RuntimeException("La variable '" + name + "' est déjà définie dans ce contexte.");

        // Bind symbol name to its evaluated runtime object value.
        this.values.put(name, value);

        // Track constant immutability metadata if declared with 'const'.
        if(isConstant) constants.add(name);

    }

    /**
     * Resolves and retrieves the bound runtime value of a given variable token.
     * <p>
     * Searches the local scope first. If not found, delegates the lookup recursively to the
     * enclosing parent environment.
     * </p>
     *
     * @param name The identifier {@link Token} containing the variable name and line number (must not be {@code null}).
     * @return The runtime {@link Object} bound to the identifier.
     * @throws RuntimeException If the variable has not been defined prior to access.
     */
    public Object get(Token name) {
        String varName = name.getLexeme();

        // Perform memory lookup; return bound runtime object if found.
        if (this.values.containsKey(varName)) return this.values.get(varName);

        // Delegate lookup to enclosing outer scope if variable is not in local scope.
        if (this.enclosing != null) return this.enclosing.get(name);


        throw new RuntimeException("Ligne " + name.getLine() + " : Variable non définie '" + varName + "'.");

    }

    /**
     * Updates the bound value of an existing mutable variable.
     * <p>
     * <b>Preconditions:</b> The symbol must exist in {@code values} (or outer scopes) and MUST NOT exist in {@code constants}.<br>
     * <b>Postconditions:</b> The entry in {@code values} is updated with the new object value.
     * </p>
     *
     * @param name  The identifier {@link Token} target of the assignment (must not be {@code null}).
     * @param value The new evaluated object value to store.
     * @throws RuntimeException If the variable is undeclared OR if the target symbol is an immutable constant.
     */
    public void assign(Token name, Object value) {
        String varName = name.getLexeme();

        // If variable exists in current local scope
        if (this.values.containsKey(varName)) {
            // Protect immutable bindings from mutation attempts.
            if (this.constants.contains(varName)) {
                throw new RuntimeException("Ligne " + name.getLine() + " : Impossible de modifier la constante '" + varName + "'.");
            }
            // Perform in-place value update in memory map.
            this.values.put(varName, value);
            return;
        }

        // Delegate reassignment to outer enclosing scope if not found locally.
        if (this.enclosing != null) {
            this.enclosing.assign(name, value);
            return;
        }

        // Ensure variable exists before attempting mutation.
        throw new RuntimeException("Ligne " + name.getLine() + " : Variable non définie '" + varName + "'.");
    }
}
