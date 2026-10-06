package interpreter;

/**
 * Control flow exception used to interrupt function execution and return a value.
 * <p>
 * This exception bypasses normal control flow during execution to return a value
 * from a GEM function back to the caller. JVM stack trace generation is disabled
 * for performance optimization.
 * </p>
 *
 * @author Van De Perre Harry
 * @version 1.0
 */
public class ReturnException extends RuntimeException {
    /** The runtime object value returned by the 'return' statement. */
    private final Object value;

    public ReturnException (Object value) {
        super(null, null, false, false); // disable stack capture JAVA (optimisation)

        this.value = value;
    }

    public Object getValue() {
        return value;
    }
}
