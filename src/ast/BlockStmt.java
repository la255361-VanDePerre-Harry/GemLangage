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
public class BlockStmt extends Stmt{
    private final List<Stmt> statements;

    public BlockStmt(List<Stmt> statements) {
        this.statements = statements;
    }

    public List<Stmt> getStatements() {
        return statements;
    }
}
