package dev.somemathew.compiler.lexer;
import static com.google.common.base.Preconditions.checkNotNull;

public final class Token {
    private final TokenType type;
    private final String lexeme;
    private final String errorMessage;
    private final int line;
    private final int column;

    public Token(TokenType type, String lexeme, int line, int column) {
        this(type, lexeme, line, column, null);
    }

    public Token(TokenType type, String lexeme, int line, int column, String errorMessage) {
        this.type = checkNotNull(type);
        this.lexeme = lexeme;
        this.line = line;
        this.column = column;
        this.errorMessage = errorMessage;
    }

    public TokenType getType() {
        return type;
    }

    public String getLexeme() {
        return lexeme;
    }

    public int getLine() {
        return line;
    }

    public int getColumn() {
        return column;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public boolean isError() {
        return type == TokenType.ERROR;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Token [type:");
        builder.append(type);
        if (isError()) {
            builder.append(", msg: ");
            builder.append(errorMessage);
        }
        builder.append(", lexeme: ");
        builder.append(lexeme);
        builder.append(", line:");
        builder.append(line);
        builder.append(", column:");
        builder.append(column);
        builder.append("]");
        return builder.toString();
    }

}
