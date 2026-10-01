package lexer;

public enum TokenType {
    // KeyWords
    CONST,
    MUT,
    IF,
    ELSE,
    WHILE,
    PRINT,

    // Data Types
    TYPE_INT,
    TYPE_STRING,
    TYPE_BOOL,

    // Literals and Identifiers
    NUMBER,
    STRING,
    BOOLEAN,
    IDENTIFIER,

    // Arithmetic and Logical Operators
    ASSIGN,
    PLUS,
    MINUS,
    STAR,
    SLASH,
    LESS,
    GREATER,
    EQUAL,

    // Symbols and Separators
    COLON,
    LPAREN,
    RPAREN,
    LBRACE,
    RBRACE,

    // FLUX CONTROL
    EOF
}