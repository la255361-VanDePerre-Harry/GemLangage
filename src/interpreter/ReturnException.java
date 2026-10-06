package interpreter;

public class ReturnException extends RuntimeException {
    // value return by the function
    private final Object value;

    public ReturnException (Object value) {
        super(null, null, false, false); // disable stack capture JAVA (optimisation)

        this.value = value;
    }

    public Object getValue() {
        return value;
    }
}
