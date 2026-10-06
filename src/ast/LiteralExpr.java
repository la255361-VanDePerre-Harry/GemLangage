package ast;

/**
 * Represents a literal value expression node in the GEM Abstract Syntax Tree (AST).
 * <p>
 * A literal expression holds a raw, constant value directly embedded in the source code,
 * such as numbers (e.g., {@code 42}), strings (e.g., {@code "Hello"}), or booleans (e.g., {@code true}).
 * </p>
 *
 * @author Van De Perre Harry
 * @version 1.0
 */
public class LiteralExpr extends Expr {
    /** The runtime value represented by this literal node. */
    private final Object value;

    /**
     * Constructs a new {@code LiteralExpr} node.
     *
     * @param value The primitive or constant object value (e.g., {@link String}, {@link Integer}, or {@link Boolean}).
     */
    public LiteralExpr(Object value) {
        this.value = value;
    }

    /**
     * Retrieves the raw value object stored within this literal expression.
     *
     * @return The runtime {@link Object} representation.
     */
    public Object getValue() {
        return value;
    }
}