package dev.somemathew.compiler.lexer;

public enum TokenType {
    /* Lexical Elements */
    ID, NUM_INTEGER, NUM_FLOAT,

    /* Operators */
    OP_ASSIGN, OP_EQ, OP_NEQ, OP_LT, OP_GT, OP_LTE, //
    OP_GTE, OP_PLUS, OP_MINUS, OP_MULT, OP_DIV, OP_AND, //
    OP_OR, OP_NOT, OP_SR,

    /* Punctuation */
    LPAREN, RPAREN, LCURL, RCURL, LBRACE, RBRACE, //
    LCOMMENT, RCOMMENT, COMMENT, ERROR, EOF, COLON, //
    SEMICOL, COMMA, DOT,

    /* Reserved Words */
    RW_IF, RW_THEN, RW_ELSE, RW_FOR, RW_CLASS, RW_INTEGER, //
    RW_FLOAT, RW_READ, RW_WRITE, RW_RETURN, RW_MAIN, RW_VOID
}
