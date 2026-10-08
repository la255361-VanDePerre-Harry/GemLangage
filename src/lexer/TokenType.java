package lexer;

/**
 * Represents the complete set of token categories recognized by the GEM language Lexer.
 * <p>
 * Each enumeration constant defines a specific lexical unit used by the Parser
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
    /** Constant declaration keyword ('const'). */
    CONST,
    /** Mutable variable declaration keyword ('mut'). */
    MUT,
    /** Conditional execution branch keyword ('if'). */
    IF,
    /** Alternative execution branch keyword ('else'). */
    ELSE,
    /** Conditional loop keyword ('while'). */
    WHILE,
    /** Standard output printing keyword ('print'). */
    PRINT,
    /** Iterative loop keyword ('for'). */
    FOR,
    /** Flows controls */
    BREAK,
    CONTINUE,

    // =========================================================================
    // Native Data Types
    // =========================================================================
    /** Primitive integer type specifier ('int'). */
    TYPE_INT,
    /** Primitive string type specifier ('string'). */
    TYPE_STRING,
    /** Primitive boolean type specifier ('bool'). */
    TYPE_BOOL,

    // =========================================================================
    // Literals and Identifiers
    // =========================================================================
    /** Numeric literal value (e.g., '42', '100'). */
    NUMBER,
    /** String literal value (e.g., '"Hello World"'). */
    STRING,
    /** Boolean literal value ('true' or 'false'). */
    BOOLEAN,
    /** User-defined symbol or variable identifier (e.g., 'counter', 'add'). */
    IDENTIFIER,

    // =========================================================================
    // Arithmetic, Logical, and Assignment Operators
    // =========================================================================
    /** Assignment operator ('='). */
    ASSIGN,
    /** Addition operator ('+'). */
    PLUS,
    /** Subtraction operator ('-'). */
    MINUS,
    /** Multiplication operator ('*'). */
    STAR,
    /** Division operator ('/'). */
    SLASH,
    /** Relational 'less than' operator ('<'). */
    LESS,
    /** Relational 'greater than' operator ('>'). */
    GREATER,
    /** Equality comparison operator ('=='). */
    EQUAL,
    /** Logical short-circuit 'AND' operator ('&&'). */
    AND_AND,
    /** Logical short-circuit 'OR' operator ('||'). */
    OR_OR,
    /** Logical negation operator ('!'). */
    BANG,
    /** Relational 'greater than or equal' operator ('>='). */
    GREATER_EQUAL,
    /** Relational 'less than or equal' operator ('<='). */
    LESS_EQUAL,

    // =========================================================================
    // Increment Operators
    // =========================================================================
    /* Increment var=var+1 ('++') */
    PLUS_PLUS,
    /* Increment var=var-1 ('--') */
    MINUS_MINUS,
    /* ('+=) */
    PLUS_EQUAL,
    /* ('-=') */
    MINUS_EQUAL,
    /* ('*=') */
    STAR_EQUAL,
    /* ('/=') */
    SLASH_EQUAL,


    // =========================================================================
    // Symbols and Delimiters
    // =========================================================================
    /** Argument and parameter separator (','). */
    COMMA,
    /** Type annotation separator (':'). */
    COLON,
    /** Statement terminator (';'). */
    SEMICOLON,
    /** Left opening parenthesis ('('). */
    LPAREN,
    /** Right closing parenthesis (')'). */
    RPAREN,
    /** Left opening brace ('{'). */
    LBRACE,
    /** Right closing brace ('}'). */
    RBRACE,

    // =========================================================================
    // Functions
    // =========================================================================
    /** Function declaration keyword ('fn'). */
    FN,
    /** Function return keyword ('return'). */
    RETURN,

    // =========================================================================
    // Control Flow Markers
    // =========================================================================
    /** End-of-file sentinel marker indicating complete scanning of input source. */
    EOF
}