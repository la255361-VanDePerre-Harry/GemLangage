package parser;
import java.util.ArrayList;
import java.util.List;

import ast.*;
import lexer.Token;
import lexer.TokenType;

/**
 * Performs syntactic analysis (parsing) on a stream of tokens produced by the {@link lexer.Lexer}.
 * <p>
 * The {@code Parser} follows a recursive descent parsing strategy to construct an Abstract Syntax
 * Tree (AST) composed of {@link Stmt} and {@link Expr} nodes according to the GEM language grammar.
 * </p>
 *
 * @author Van De Perre Harry
 * @version 1.0
 */
public class Parser {
    /** The linear list of tokens to parse. */
    private final List<Token> tokens;
    /** The index pointing to the token currently being inspected. */
    private int current = 0;

    public Parser(List<Token> tokens) { this.tokens = tokens; }

    /**
     * Parses the full sequence of tokens into a list of AST statement nodes.
     * <p>
     * Iterates until the {@link TokenType#EOF} token is reached, attempting to parse top-level
     * declarations sequentially.
     * </p>

     * @return A {@link List} of {@link Stmt} nodes representing the complete AST of the program.
     * @throws RuntimeException If a syntax error is encountered during parsing.
     */
    public List<Stmt> parse() {
        List<Stmt> statements = new ArrayList<>();

        // Main parsing loop: process declarations sequentially until the end-of-file marker is reached.
        while (!isAtEnd()) {
            statements.add(declaration());
        }

        return statements;
    }

    /**
     * Parses top-level declaration statements (variable declarations or standard statements).
     * <p>
     * <b>Grammar rule:</b> {@code declaration -> varDecl | statement}
     * </p>
     *
     * @return The constructed {@link Stmt} AST node.
     */
    private Stmt declaration() {
        // Dispatch to variable declaration parsing if the 'const' or 'mut' keyword is matched.
        if (match(TokenType.CONST)) return varDeclaration(true);

        if (match(TokenType.MUT)) return varDeclaration(false);

        return statement();
    }

    /**
     * Parses variable and constant declarations with mandatory type annotations and initializers.
     * <p>
     * <b>Grammar rule:</b> {@code varDecl -> ('const' | 'mut') IDENTIFIER ':' TYPE '=' expression}
     * </p>
     *
     * @param isConstant {@code true} if declared via 'const', {@code false} if declared via 'mut'.
     * @return A {@link VarDeclStmt} node encapsulating identifier name, declared type, initializer, and mutability.
     * @throws RuntimeException If any syntax token in the declaration sequence is missing.
     */
    private Stmt varDeclaration(boolean isConstant) {
        // Step 1: Ensure the binding is given a valid identifier name.
        Token name = consume(TokenType.IDENTIFIER, "Nom de variable attendu.");

        // Step 2: Enforce explicit typing syntax using colon separator (e.g., 'name : type').
        consume(TokenType.COLON, "':' attendu après le nom de la variable.");

        // Step 3: Advance over the type specifier token (e.g., int, string, bool).
        Token typeToken = advance();

        // Step 4: Require explicit initialization on declaration using the assignment operator.
        consume(TokenType.ASSIGN, "'=' attendu avant l'expression d'initialisation.");

        // Step 5: Parse the initialization expression.
        Expr initializer = expression();

        return new VarDeclStmt(name, typeToken, initializer, isConstant);
    }

    /**
     * Parses imperative control statements, output instructions, and variable reassignments.
     * <p>
     * <b>Grammar rule:</b> {@code statement -> printStmt | assignStmt}
     * </p>
     *
     * @return The parsed {@link Stmt} node.
     * @throws RuntimeException If the current token sequence does not match any recognized statement syntax.
     */
    private Stmt statement() {
        // Blocs statement
        if (match(TokenType.LBRACE)) {
            new BlockStmt(block());
        }

        // Print statement ('print expression')
        if (match(TokenType.PRINT)) {
            Expr value = expression();
            return new PrintStmt(value);
        }

        // Variable reassignment ('identifier = expression')
        // Uses lookahead via check() to distinguish reassignment from other identifier references.
        if (check(TokenType.IDENTIFIER)) {
            Token name = advance();
            consume(TokenType.ASSIGN, "'=' attendu après le nom de la variable.");
            Expr value = expression();
            return new AssignStmt(name, value);
        }

        throw new RuntimeException("Ligne " + peek().getLine() + " : Instruction non reconnue '" + peek().getLexeme() + "'");
    }

    /**
     * Top-level entry point for parsing expressions.
     * <p>
     * Delegates to {@link #equality()} to begin recursive descent expression evaluation
     * at the lowest operator precedence level.
     * </p>
     *
     * @return The parsed {@link Expr} AST node.
     */
    private Expr expression() {
        return equality();
    }

    /**
     * Parses equality comparison operations ({@code ==}).
     * <p>
     * <b>Grammar rule:</b> {@code equality -> comparison ( '==' comparison )*}
     * </p>
     *
     * @return An {@link Expr} node representing an equality comparison subtree.
     */
    private Expr equality() {
        Expr expr = comparison();

        while (match(TokenType.EQUAL)) {
            Token operator = previous();
            Expr right = comparison();
            expr = new BinaryExpr(expr, operator, right);
        }

        return expr;
    }

    /**
     * Parses relational comparison operations ({@code >} and {@code <}).
     * <p>
     * <b>Grammar rule:</b> {@code comparison -> term ( ('>' | '<') term )*}
     * </p>
     *
     * @return An {@link Expr} node representing a relational comparison subtree.
     */
    private Expr comparison() {
        Expr expr = term();
        while (match(TokenType.GREATER, TokenType.LESS)) {
            Token operator = previous();
            Expr right = term();
            expr = new BinaryExpr(expr, operator, right);
        }

        return expr;
    }

    /**
     * Parses addition ('+') and subtraction ('-') operations.
     * <p>
     * <b>Grammar rule:</b> {@code term -> factor ( ('+' | '-') factor )*}
     * </p>
     */
    private Expr term() {
        Expr expr = factor();

        while (match(TokenType.PLUS, TokenType.MINUS)) {
            Token operator = previous();
            Expr right = factor();
            expr = new BinaryExpr(expr, operator, right);
        }

        return expr;
    }

    /**
     * Parses multiplication ('*') and division ('/') operations.
     * <p>
     * <b>Grammar rule:</b> {@code factor -> primary ( ('*' | '/') primary )*}
     * </p>
     */
    private Expr factor() {
        Expr expr = primary();

        while (match(TokenType.STAR, TokenType.SLASH)) {
            Token operator = previous();
            Expr right = primary();
            expr = new BinaryExpr(expr, operator, right);
        }

        return expr;
    }

    /**
     * Parses primary terminal nodes (literals and identifier variable references).
     * <p>
     * <b>Grammar rule:</b> {@code primary -> NUMBER | STRING | BOOLEAN | IDENTIFIER}
     * </p>
     *
     * @return A {@link LiteralExpr} or {@link VariableExpr} leaf node.
     * @throws RuntimeException If the token stream does not match any valid expression literal or identifier.
     */
    private Expr primary() {
        // Primitive value literals (Number, String, Boolean).
        if (match(TokenType.NUMBER, TokenType.STRING, TokenType.BOOLEAN)) return new LiteralExpr(previous().getLexeme());

        // Variable identifier lookups in expressions.
        if (match(TokenType.IDENTIFIER)) return new VariableExpr(previous());

        // Grouping expression
        if (match(TokenType.LPAREN)) {
            Expr expr = expression();
            consume(TokenType.RPAREN, "Parenthèse fermante ')' attendue après l'expression.");
            return expr;
        }
        throw new RuntimeException("Ligne " + peek().getLine() + " : Expression attendue près de '" + peek().getLexeme() + "'");
    }

    /**
     * Inspects the current lookahead token without consuming it.
     *
     * @return The {@link Token} at the current cursor index.
     */
    private Token peek() { return this.tokens.get(this.current); }

    /**
     * Returns the most recently consumed token.
     *
     * @return The previous {@link Token} in the stream.
     */
    private Token previous() { return this.tokens.get(this.current - 1); }

    /**
     * Determines whether the parser has reached the end of the token stream.
     *
     * @return {@code true} if the current token is {@link TokenType#EOF}; {@code false} otherwise.
     */
    private boolean isAtEnd() {
        return peek().getType() == TokenType.EOF;
    }

    /**
     * Consumes the current token and moves the cursor forward by one position.
     *
     * @return The consumed {@link Token}.
     */
    private Token advance() {
        if (!isAtEnd()) current++;

        return previous();
    }

    /**
     * Non-consuming lookahead check. Determines if the current token matches a given type.
     *
     * @param type The {@link TokenType} to check for.
     * @return {@code true} if matched and not at EOF; {@code false} otherwise.
     */
    private boolean check(TokenType type) {
        if (isAtEnd()) return false;

        return peek().getType() == type;
    }

    /**
     * Checks if the current token matches any of the supplied candidate types.
     * <p>
     * Automatically consumes the token via {@link #advance()} on the first matching type.
     * </p>
     *
     * @param types Variadic candidate list of {@link TokenType} options.
     * @return {@code true} if a candidate matched and was consumed; {@code false} otherwise.
     */
    private boolean match(TokenType... types) {
        for (TokenType type : types) {
            if (check(type)) {
                advance();
                return true;
            }
        }
        return false;
    }

    /**
     * Enforces token matching at critical grammar junction points.
     * <p>
     * Consumes the current token if it matches the expected {@link TokenType}.
     * Throws a descriptive syntax error if the assertion fails.
     * </p>
     *
     * @param type    The expected {@link TokenType}.
     * @param message The error detail message to display if the match fails.
     * @return The consumed {@link Token}.
     * @throws RuntimeException If the current token does not match the expected type.
     */
    private Token consume(TokenType type, String message) {
        if (check(type)) return advance();

        throw new RuntimeException("Ligne " + peek().getLine() + " : " + message);
    }

    private List<Stmt> block() {
        List<Stmt> statements = new ArrayList<>();.
        while (!check(TokenType.RBRACE) && !isAtEnd()) statements.add(declaration());

        consume(TokenType.RBRACE, "Accolade fermante '}' attendue après le bloc.");

        return statements;
    }

}
