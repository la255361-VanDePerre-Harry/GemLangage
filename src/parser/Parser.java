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
 * Tree (AST) composed of {@link Stmt} and {@link Expr} nodes according to the GEM language grammar specifications.
 * </p>
 *
 * @author Van De Perre Harry
 * @version 1.0
 */
public class Parser {
    /** The linear sequence of tokens produced by the lexer to be parsed. */
    private final List<Token> tokens;

    /** The index cursor pointing to the token currently under inspection. */
    private int current = 0;

    /**
     * Constructs a new {@code Parser} instance with a given token stream.
     *
     * @param tokens The list of {@link Token} objects to parse.
     */
    public Parser(List<Token> tokens) {
        this.tokens = tokens;
    }

    /**
     * Parses the complete sequence of tokens into a sequential list of AST statement nodes.
     * <p>
     * Iterates through top-level declarations until the {@link TokenType#EOF} marker is reached.
     * </p>
     *
     * @return A {@link List} of {@link Stmt} nodes representing the root Abstract Syntax Tree.
     * @throws RuntimeException If a syntax error is encountered during parsing.
     */
    public List<Stmt> parse() {
        List<Stmt> statements = new ArrayList<>();

        // Main parsing loop: process declarations sequentially until reaching end-of-file.
        while (!isAtEnd()) {
            statements.add(declaration());
        }

        return statements;
    }

    /**
     * Parses top-level declaration statements (variable declarations, function declarations, or standard statements).
     * <p>
     * <b>Grammar rule:</b> {@code declaration -> varDecl | functionDecl | statement}
     * </p>
     *
     * @return The constructed {@link Stmt} AST node.
     */
    private Stmt declaration() {
        // Dispatch to constant or mutable variable declaration parsing
        if (match(TokenType.CONST)) return varDeclaration(true);
        if (match(TokenType.MUT)) return varDeclaration(false);

        // Dispatch to function declaration parsing
        if (match(TokenType.FN)) return functionDeclaration();

        return statement();
    }

    /**
     * Parses variable and constant declarations with mandatory type annotations and initializers.
     * <p>
     * <b>Grammar rule:</b> {@code varDecl -> ('const' | 'mut') IDENTIFIER ':' TYPE '=' expression}
     * </p>
     *
     * @param isConstant {@code true} if declared via 'const', {@code false} if declared via 'mut'.
     * @return A {@link VarDeclStmt} node encapsulating identifier name, declared type token, initializer, and mutability flag.
     * @throws RuntimeException If any mandatory token in the declaration sequence is missing.
     */
    private Stmt varDeclaration(boolean isConstant) {
        // Step 1: Ensure the declaration provides a valid identifier name.
        Token name = consume(TokenType.IDENTIFIER, "Expected variable name.");

        // Step 2: Enforce explicit typing syntax using a colon separator (e.g., 'name : type').
        consume(TokenType.COLON, "Expected ':' after variable name.");

        // Step 3: Advance over the declared explicit type specifier token (e.g., int, string, bool).
        Token typeToken = advance();

        // Step 4: Require explicit initialization upon declaration using the assignment operator.
        consume(TokenType.ASSIGN, "Expected '=' before initialization expression.");

        // Step 5: Parse the initialization expression subtree.
        Expr initializer = expression();

        return new VarDeclStmt(name, typeToken, initializer, isConstant);
    }

    /**
     * Parses imperative control statements, output instructions, variable reassignments, and expression statements.
     * <p>
     * <b>Grammar rule:</b> {@code statement -> returnStmt | forStmt | whileStmt | ifStmt | blockStmt | printStmt | assignStmt | exprStmt}
     * </p>
     *
     * @return The parsed {@link Stmt} AST node.
     * @throws RuntimeException If the current token sequence does not match any recognized statement syntax.
     */
    private Stmt statement() {
        return switch (peek().getType()) {
            case BREAK -> {
                Token keyword = advance(); // Consomme 'break'
                yield new BreakStmt(keyword);
            }

            case CONTINUE -> {
                Token keyword = advance(); // Consomme 'continue'
                yield new ContinueStmt(keyword);
            }

            case RETURN -> {
                advance();
                yield returnStatement();
            }

            case FOR -> {
                advance();
                yield forStatement();
            }

            case WHILE -> {
                advance();
                yield whileStatement();
            }

            case IF -> {
                advance();
                yield ifStatement();
            }

            case LBRACE -> {
                advance();
                yield new BlockStmt(block());
            }

            case PRINT -> {
                advance();
                Expr value = expression();
                yield new PrintStmt(value);
            }

            case IDENTIFIER -> {
                Token name = peek();

                // Incrementation
                if (checkNext(TokenType.PLUS_PLUS)) {
                    advance(); // Consommation de l'id
                    advance(); // Consommation de l'increment

                    Token plusOp = new Token(TokenType.PLUS, "+", name.getLine());
                    Expr one = new LiteralExpr(1);
                    Expr addExpr = new BinaryExpr(new VariableExpr(name), plusOp, one);
                    yield new AssignStmt(name, addExpr);
                }

                // Decrementation
                if (checkNext(TokenType.MINUS_MINUS)) {
                    advance(); // Consomation de l'id
                    advance(); // Consommation de l'increment

                    Token minusOp = new Token(TokenType.MINUS, "-", name.getLine());
                    Expr one = new LiteralExpr(1);
                    Expr subExpr = new BinaryExpr(new VariableExpr(name), minusOp, one);
                    yield new AssignStmt(name, subExpr);
                }

                // Composed affectations
                if (checkNext(TokenType.PLUS_EQUAL) || checkNext(TokenType.MINUS_EQUAL) || checkNext(TokenType.STAR_EQUAL) || checkNext(TokenType.SLASH_EQUAL)) {
                    advance(); // consommation de l'id

                    Token compoundOp = advance(); // Consommation -> += / -= / *= / /=

                    TokenType binaryType = getBinaryTypeFromCompound(compoundOp.getType());
                    Token binaryOp = new Token(binaryType, getOperatorLexeme(binaryType), compoundOp.getLine());

                    Expr rightValue = expression();
                    Expr binaryExpr = new BinaryExpr(new VariableExpr(name), binaryOp, rightValue);
                    yield new AssignStmt(name, binaryExpr);
                }

                // Basic affectation
                if (checkNext(TokenType.ASSIGN)) {
                    advance(); // consommation de l'ID

                    consume(TokenType.ASSIGN, "Expected '=' after variable name.");
                    Expr value = expression();
                    yield new AssignStmt(name, value);
                }

                // Fallback to auto expressions
                yield new ExpressionStmt(expression());
            }

            default ->
                    throw new RuntimeException("Line " + peek().getLine() + " : Unrecognized statement near '" + peek().getLexeme() + "'");
        };
    }

    /**
     * Entry point for expression parsing.
     * <p>
     * Begins recursive descent evaluation starting at the lowest precedence level (logical OR).
     * </p>
     *
     * @return The parsed {@link Expr} AST node.
     */
    private Expr expression() {
        return logicOr();
    }

    /**
     * Parses logical 'OR' binary expressions ({@code ||}).
     * <p>
     * <b>Grammar rule:</b> {@code logicOr -> logicAnd ( '||' logicAnd )*}
     * </p>
     *
     * @return An {@link Expr} AST node representing a logical 'OR' subtree, or a higher precedence expression node.
     */
    private Expr logicOr() {
        Expr expr = logicAnd();
        while (match(TokenType.OR_OR)) {
            Token operator = previous();
            Expr right = logicAnd();
            expr = new LogicalExpr(expr, operator, right);
        }
        return expr;
    }

    /**
     * Parses logical 'AND' binary expressions ({@code &&}).
     * <p>
     * <b>Grammar rule:</b> {@code logicAnd -> equality ( '&&' equality )*}
     * </p>
     *
     * @return An {@link Expr} AST node representing a logical 'AND' subtree, or a higher precedence expression node.
     */
    private Expr logicAnd() {
        Expr expr = equality();
        while (match(TokenType.AND_AND)) {
            Token operator = previous();
            Expr right = equality();
            expr = new LogicalExpr(expr, operator, right);
        }
        return expr;
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
     * Parses relational comparison operations ({@code <}, {@code <=}, {@code >}, {@code >=}).
     * <p>
     * <b>Grammar rule:</b> {@code comparison -> term ( ( '>' | '>=' | '<' | '<=' ) term )*}
     * </p>
     *
     * @return An {@link Expr} node representing a comparison subtree.
     */
    private Expr comparison() {
        Expr expr = term();

        while (match(TokenType.GREATER, TokenType.GREATER_EQUAL, TokenType.LESS, TokenType.LESS_EQUAL)) {
            Token operator = previous();
            Expr right = term();
            expr = new BinaryExpr(expr, operator, right);
        }

        return expr;
    }

    /**
     * Parses addition ({@code +}) and subtraction ({@code -}) arithmetic operations.
     * <p>
     * <b>Grammar rule:</b> {@code term -> factor ( ('+' | '-') factor )*}
     * </p>
     *
     * @return An {@link Expr} node representing an additive binary operation.
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
     * Parses multiplication ({@code *}) and division ({@code /}) arithmetic operations.
     * <p>
     * <b>Grammar rule:</b> {@code factor -> primary ( ('*' | '/') primary )*}
     * </p>
     *
     * @return An {@link Expr} node representing a multiplicative binary operation.
     */
    private Expr factor() {
        Expr expr = unary();

        while (match(TokenType.STAR, TokenType.SLASH)) {
            Token operator = previous();
            Expr right = unary();
            expr = new BinaryExpr(expr, operator, right);
        }

        return expr;
    }

    /**
     * Parses unary operations ({@code !} for logical NOT, {@code -} for numeric negation).
     * <p>
     * <b>Grammar rule:</b> {@code unary -> ( '!' | '-' ) unary | primary}
     * </p>
     *
     * @return An {@link Expr} node representing a unary subtree or a primary expression.
     */
    private Expr unary() {
        if (match(TokenType.BANG, TokenType.MINUS)) {
            Token operator = previous();
            Expr right = unary();
            return new UnaryExpr(operator, right);
        }
        return primary();
    }

    /**
     * Parses primary terminal nodes (literals, variable references, parenthesized expressions, or function call invocations).
     * <p>
     * <b>Grammar rule:</b> {@code primary -> NUMBER | STRING | BOOLEAN | IDENTIFIER ( '(' arguments? ')' )? | '(' expression ')'}
     * </p>
     *
     * @return A {@link LiteralExpr}, {@link VariableExpr}, or {@link CallExpr} leaf node.
     * @throws RuntimeException If the token stream does not match any valid expression element.
     */
    private Expr primary() {
        // Parse primitive value literals (Number, String, Boolean)
        if (match(TokenType.NUMBER, TokenType.STRING, TokenType.BOOLEAN)) {
            return new LiteralExpr(previous().getLexeme());
        }

        // Parse variable access or function call invocation
        if (match(TokenType.IDENTIFIER)) {
            Token name = previous();

            // Function call signature: identifier followed immediately by '('
            if (match(TokenType.LPAREN)) {
                List<Expr> arguments = new ArrayList<>();
                if (!check(TokenType.RPAREN)) {
                    do {
                        arguments.add(expression());
                    } while (match(TokenType.COMMA));
                }
                consume(TokenType.RPAREN, "Expected ')' after function argument list.");
                return new CallExpr(name, arguments);
            }

            // Simple variable evaluation leaf node
            return new VariableExpr(name);
        }

        // Parenthesized grouping expression
        if (match(TokenType.LPAREN)) {
            Expr expr = expression();
            consume(TokenType.RPAREN, "Expected closing ')' after grouping expression.");
            return expr;
        }

        throw new RuntimeException("Line " + peek().getLine() + " : Expected expression near '" + peek().getLexeme() + "'");
    }

    /**
     * Inspects the current lookahead token without consuming it.
     *
     * @return The {@link Token} at the current cursor index.
     */
    private Token peek() {
        return this.tokens.get(this.current);
    }

    /**
     * Retrieves the most recently consumed token from the stream.
     *
     * @return The previous {@link Token}.
     */
    private Token previous() {
        return this.tokens.get(this.current - 1);
    }

    /**
     * Determines whether the parser has reached the end of the token stream.
     *
     * @return {@code true} if the current token is {@link TokenType#EOF}; {@code false} otherwise.
     */
    private boolean isAtEnd() {
        return peek().getType() == TokenType.EOF;
    }

    /**
     * Consumes the current lookahead token and advances the cursor index forward.
     *
     * @return The consumed {@link Token}.
     */
    private Token advance() {
        if (!isAtEnd()) current++;
        return previous();
    }

    /**
     * Checks whether the current lookahead token matches a specified type without consuming it.
     *
     * @param type The {@link TokenType} to test against.
     * @return {@code true} if matched and not at EOF; {@code false} otherwise.
     */
    private boolean check(TokenType type) {
        if (isAtEnd()) return false;
        return peek().getType() == type;
    }

    /**
     * Checks if the current token matches any of the supplied candidate types.
     * <p>
     * Automatically consumes the matched token via {@link #advance()} on the first hit.
     * </p>
     *
     * @param types Variadic list of candidate {@link TokenType} instances.
     * @return {@code true} if a candidate type matched and was consumed; {@code false} otherwise.
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
     * Enforces token matching at structural grammar junctions.
     * <p>
     * Consumes the token if it matches {@code type}; throws a syntax error otherwise.
     * </p>
     *
     * @param type    The expected {@link TokenType}.
     * @param message The detailed error message to include if matching fails.
     * @return The consumed {@link Token}.
     * @throws RuntimeException If the lookahead token fails to match the expected type.
     */
    private Token consume(TokenType type, String message) {
        if (check(type)) return advance();
        throw new RuntimeException("Line " + peek().getLine() + " : " + message);
    }

    /**
     * Parses a scoped block of statements enclosed within curly braces ({@code { ... }}).
     *
     * @return A {@link List} of {@link Stmt} nodes parsed inside the block scope.
     */
    private List<Stmt> block() {
        List<Stmt> statements = new ArrayList<>();

        // Sequentially parse statements inside the block until encountering '}' or EOF
        while (!check(TokenType.RBRACE) && !isAtEnd()) {
            statements.add(declaration());
        }

        consume(TokenType.RBRACE, "Expected closing '}' after block statement.");
        return statements;
    }

    /**
     * Parses a conditional 'if' statement with an optional 'else' branch.
     * <p>
     * <b>Grammar rule:</b> {@code ifStmt -> 'if' expression statement ( 'else' statement )?}
     * </p>
     *
     * @return An {@link IfStmt} AST node encapsulating condition, then-branch, and optional else-branch.
     */
    private Stmt ifStatement() {
        Expr condition = expression();
        Stmt thenBranch = statement();

        Stmt elseBranch = null;
        if (match(TokenType.ELSE)) {
            elseBranch = statement();
        }

        return new IfStmt(condition, thenBranch, elseBranch);
    }

    /**
     * Parses an iterative 'while' loop statement.
     * <p>
     * <b>Grammar rule:</b> {@code whileStmt -> 'while' expression statement}
     * </p>
     *
     * @return A {@link WhileStmt} AST node encapsulating condition expression and body.
     */
    private Stmt whileStatement() {
        Expr condition = expression();
        Stmt body = statement();

        return new WhileStmt(condition, body);
    }

    /**
     * Parses a 'for' loop statement and desugars it into an AST composed of local block scopes and a 'while' loop.
     * <p>
     * <b>Grammar rule:</b> {@code forStmt -> 'for' '(' ( varDecl | statement | ';' ) expression? ';' statement? ')' statement}
     * </p>
     *
     * @return A {@link Stmt} AST node representing the desugared 'for' loop structure.
     */
    private Stmt forStatement() {
        consume(TokenType.LPAREN, "Expected '(' after 'for'.");

        // 1. Parse loop initializer clause
        Stmt initializer;
        if (match(TokenType.SEMICOLON)) {
            initializer = null;
        } else if (match(TokenType.CONST)) {
            initializer = varDeclaration(true);
            consume(TokenType.SEMICOLON, "Expected ';' after 'for' loop initialization.");
        } else if (match(TokenType.MUT)) {
            initializer = varDeclaration(false);
            consume(TokenType.SEMICOLON, "Expected ';' after 'for' loop initialization.");
        } else {
            initializer = statement();
            consume(TokenType.SEMICOLON, "Expected ';' after 'for' loop initialization.");
        }

        // 2. Parse loop condition clause
        Expr condition = null;
        if (!check(TokenType.SEMICOLON)) {
            condition = expression();
        }
        consume(TokenType.SEMICOLON, "Expected ';' after 'for' loop condition.");

        // 3. Parse loop increment clause
        Stmt increment = null;
        if (!check(TokenType.RPAREN)) {
            Token name = consume(TokenType.IDENTIFIER, "Expected variable name for loop increment.");
            consume(TokenType.ASSIGN, "Expected '=' after increment variable name.");
            Expr value = expression();
            increment = new AssignStmt(name, value);
        }

        consume(TokenType.RPAREN, "Expected ')' after 'for' loop clauses.");

        // 4. Parse loop body statement
        Stmt body = statement();

        // --- AST SYNTACTIC DESUGARING ---

        // Append increment statement to the bottom of the loop body
        if (increment != null) {
            body = new BlockStmt(List.of(body, increment));
        }

        // Default condition to 'true' if omitted (e.g., for(;;))
        if (condition == null) {
            condition = new LiteralExpr(true);
        }

        // Desugar into equivalent 'while' loop structure
        body = new WhileStmt(condition, body);

        // Enclose initializer and while loop inside a new block scope
        if (initializer != null) {
            body = new BlockStmt(List.of(initializer, body));
        }

        return body;
    }

    /**
     * Parses a function declaration statement.
     * <p>
     * <b>Grammar rule:</b> {@code function -> 'fn' IDENTIFIER '(' parameters? ')' ( ':' type )? '{' block '}'}
     * </p>
     *
     * @return A {@link FunctionStmt} AST node representing the declared function.
     */
    private FunctionStmt functionDeclaration() {
        Token name = consume(TokenType.IDENTIFIER, "Expected function name.");

        consume(TokenType.LPAREN, "Expected '(' after function name.");
        List<Token> parameters = new ArrayList<>();

        if (!check(TokenType.RPAREN)) {
            do {
                Token param = consume(TokenType.IDENTIFIER, "Expected parameter name.");
                parameters.add(param);

                // Handle optional explicit parameter type annotation (e.g., param : int)
                if (match(TokenType.COLON)) {
                    advance();
                }
            } while (match(TokenType.COMMA));
        }
        consume(TokenType.RPAREN, "Expected ')' after parameter list.");

        // Handle optional explicit return type annotation (e.g., : int)
        if (match(TokenType.COLON)) {
            advance();
        }

        consume(TokenType.LBRACE, "Expected '{' before function body.");
        List<Stmt> body = block();

        return new FunctionStmt(name, parameters, body);
    }

    /**
     * Parses a return statement.
     * <p>
     * <b>Grammar rule:</b> {@code returnStmt -> 'return' expression?}
     * </p>
     *
     * @return A {@link ReturnStmt} AST node representing the return instruction.
     */
    private Stmt returnStatement() {
        Token keyword = previous();
        Expr value = null;

        if (!check(TokenType.RBRACE) && !check(TokenType.EOF)) {
            value = expression();
        }

        return new ReturnStmt(keyword, value);
    }

    /**
     * Checks if the token immediately following the current lookahead token matches a given type.
     *
     * @param type The {@link TokenType} to inspect.
     * @return {@code true} if the next token matches; {@code false} otherwise.
     */
    private boolean checkNext(TokenType type) {
        if (current + 1 >= tokens.size()) return false;
        return tokens.get(current + 1).getType() == type;
    }

    /**
     * Maps a compound assignment token type to its binary arithmetic operator equivalent.
     */
    private TokenType getBinaryTypeFromCompound(TokenType compoundType) {
        return switch (compoundType) {
            case PLUS_EQUAL -> TokenType.PLUS;
            case MINUS_EQUAL -> TokenType.MINUS;
            case STAR_EQUAL -> TokenType.STAR;
            case SLASH_EQUAL -> TokenType.SLASH;
            default -> throw new IllegalArgumentException("Unexpected compound operator type: " + compoundType);
        };
    }

    /**
     * Retrieves the textual lexeme for a given binary operator token type.
     */
    private String getOperatorLexeme(TokenType type) {
        return switch (type) {
            case PLUS -> "+";
            case MINUS -> "-";
            case STAR -> "*";
            case SLASH -> "/";
            default -> "";
        };
    }
}