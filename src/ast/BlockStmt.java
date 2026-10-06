package ast;

import java.util.List;

/**
 * Represents a block statement node containing a sequential list of scoped statements.
 * <p>
 * A {@code BlockStmt} defines an explicit lexical scope delimited by curly braces ({@code { ... }}).
 * Statements executed within this block share a child {@link interpreter.Environment} that inherits
 * from the outer enclosing scope.
 * </p>
 *
 * @author Van De Perre Harry
 * @version 1.0
 */
public class BlockStmt extends Stmt {
    /** The sequence of statements contained within this block scope. */
    private final List<Stmt> statements;

    /**
     * Constructs a new {@code BlockStmt} node.
     *
     * @param statements The {@link List} of {@link Stmt} nodes inside the block.
     */
    public BlockStmt(List<Stmt> statements) {
        this.statements = statements;
    }

    /**
     * Retrieves the list of statements contained within the block.
     *
     * @return A {@link List} of inner {@link Stmt} AST nodes.
     */
    public List<Stmt> getStatements() {
        return statements;
    }
}