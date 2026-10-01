package parser;
import java.util.ArrayList;
import java.util.List;

import ast.*;
import lexer.Token;
import lexer.TokenType;

public class Parser {
    private final List<Token> tokens;
    private int current = 0;

    public Parser(List<Token> tokens) { this.tokens = tokens; }

    public List<Stmt> parse() {
        List<Stmt> statements = new ArrayList<>();
        while (!isAtEnd()) {
            statements.add(declaration());
        }
        return statements;
    }

    private Stmt declaration() {
        if (match(TokenType.CONST)) return varDeclaration(true);
        if (match(TokenType.MUT)) return varDeclaration(false);
        return statement();
    }

    private Stmt varDeclaration(boolean isConstant) {
        Token name = consume(TokenType.IDENTIFIER, "Nom de variable attendu.");
        consume(TokenType.COLON, "':' attendu après le nom de la variable.");

        Token typeToken = advance();

        consume(TokenType.ASSIGN, "'=' attendu avant l'expression d'initialisation.");
        Expr initializer = expression();

        return new VarDeclStmt(name, typeToken, initializer, isConstant);
    }

    private Stmt statement() {
        if (match(TokenType.PRINT)) {
            Expr value = expression();
            return new PrintStmt(value);
        }
        throw new RuntimeException("Ligne " + peek().getLine() + " : Instruction non reconnue '" + peek().getLexeme() + "'");
    }

    private Expr expression() {
        return primary();
    }

    private Expr primary() {
        if (match(TokenType.NUMBER, TokenType.STRING, TokenType.BOOLEAN)) {
            return new LiteralExpr(previous().getLexeme());
        }

        if (match(TokenType.IDENTIFIER)) {
            return new VariableExpr(previous());
        }

        throw new RuntimeException("Ligne " + peek().getLine() + " : Expression attendue près de '" + peek().getLexeme() + "'");
    }

    private Token peek() { return this.tokens.get(this.current); }

    private Token previous() { return this.tokens.get(this.current - 1); }

    private boolean isAtEnd() {
        return peek().getType() == TokenType.EOF;
    }

    private Token advance() {
        if (!isAtEnd()) current++;

        return previous();
    }

    private boolean check(TokenType type) {
        if (isAtEnd()) return false;
        return peek().getType() == type;
    }

    private boolean match(TokenType... types) {
        for (TokenType type : types) {
            if (check(type)) {
                advance();
                return true;
            }
        }
        return false;
    }

    private Token consume(TokenType type, String message) {
        if (check(type)) {
            return advance();
        }
        throw new RuntimeException("Ligne " + peek().getLine() + " : " + message);
    }
}
