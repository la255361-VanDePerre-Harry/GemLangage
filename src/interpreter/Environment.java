package interpreter;

import lexer.Token;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Manages runtime memory bindings and symbol tables for the GEM language interpreter.
 * <p>
 * The {@code Environment} class stores variable values and tracks immutability rules
 * for constants. It provides mechanisms for symbol definition, variable evaluation,
 * and runtime reassignment checking.
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
     * <b>Preconditions:</b> The symbol name must exist within {@code values}.<br>
     * <b>Postconditions:</b> Returns the bound object value associated with the identifier.
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

        throw new RuntimeException("Ligne " + name.getLine() + " : Variable non définie '" + varName + "'.");

    }

    /**
     * Updates the bound value of an existing mutable variable.
     * <p>
     * <b>Preconditions:</b> The symbol must exist in {@code values} and MUST NOT exist in {@code constants}.<br>
     * <b>Postconditions:</b> The entry in {@code values} is updated with the new object value.
     * </p>
     *
     * @param name  The identifier {@link Token} target of the assignment (must not be {@code null}).
     * @param value The new evaluated object value to store.
     * @throws RuntimeException If the variable is undeclared OR if the target symbol is an immutable constant.
     */
    public void assign(Token name, Object value) {
        String varName = name.getLexeme();

        // Ensure variable exists before attempting mutation.
        if (!this.values.containsKey(varName)) throw new RuntimeException("Ligne " + name.getLine() + " : Variable non définie '" + varName + "'.");

        // Protect immutable bindings from mutation attempts.
        if (this.constants.contains(varName)) throw new RuntimeException("Ligne " + name.getLine() + " : Impossible de modifier la constante '" + varName + "'.");

        // Perform in-place value update in memory map.
        this.values.put(varName, value);
    }
}
