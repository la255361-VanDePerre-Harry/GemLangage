package interpreter;

/**
 * Control flow exception used to interrupt function execution and return a value.
 * <p>
 * This exception bypasses normal control flow during AST evaluation to return a value
 * from a GEM function back to its caller. JVM stack trace generation and suppression
 * are disabled for performance optimization, as this exception is strictly used for
 * runtime control flow rather than error handling.
 * </p>
 *
 * @author Van De Perre Harry
 * @version 1.0
 */
public class ReturnException extends RuntimeException {
    /** The runtime object value returned by the 'return' statement. */
    private final Object value;

    /**
     * Constructs a new {@code ReturnException} encapsulating the return value.
     * <p>
     * Disables JVM stack trace generation to eliminate memory overhead during
     * frequent function return interruptions.
     * </p>
     *
     * @param value The evaluated runtime object returned by the function.
     */
    public ReturnException(Object value) {
        super(null, null, false, false); // Disable JVM stack capture and suppression for performance
        this.value = value;
    }

    /**
     * Retrieves the return value carried by this control flow exception.
     *
     * @return The evaluated runtime {@link Object}.
     */
    public Object getValue() {
        return value;
    }
}