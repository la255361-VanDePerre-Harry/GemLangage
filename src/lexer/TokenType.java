package lexer;

/**
 * Represents the complete set of token categories recognized by the GEM language Lexer.
 * <p>
 * The enumeration constant defines a specific lexical unit used by the Parser
 * during syntactic analysis and Abstract Syntax Tree (AST) construction.
 * </p>
 *
 * @author Van De Perre Harry
 * @version 1.0
 */
public enum TokenType {
    // =========================================================================
    // Keywords
    // =========================================================================
    CONST,
    MUT,
    IF,
    ELSE,
    WHILE,
    PRINT,
    FOR,

    // =========================================================================
    // Native Data Types
    // =========================================================================
    TYPE_INT,
    TYPE_STRING,
    TYPE_BOOL,

    // =========================================================================
    // Literals and Identifiers
    // =========================================================================
    NUMBER,
    STRING,
    BOOLEAN,
    IDENTIFIER,

    // =========================================================================
    // Arithmetic, Logical, and Assignment Operators
    // =========================================================================
    ASSIGN,
    PLUS,
    MINUS,
    STAR,
    SLASH,
    LESS,
    GREATER,
    EQUAL,
    AND_AND,
    OR_OR,
    BANG,


    // =========================================================================
    // Symbols and Delimiters
    // =========================================================================
    COLON,
    SEMICOLON,
    LPAREN,
    RPAREN,
    LBRACE,
    RBRACE,

    // =========================================================================
    // Flux Control
    // =========================================================================
    EOF
}