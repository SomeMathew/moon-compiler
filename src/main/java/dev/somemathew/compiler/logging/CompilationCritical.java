package dev.somemathew.compiler.logging;

import dev.somemathew.compiler.lexer.Token;

public class CompilationCritical extends CompilationMessage {
    private static final String TYPE = "CRITICAL";
    private Exception exception;

    public CompilationCritical() {
        super(-1, -1, null);
    }

    public CompilationCritical(Token token, String message) {
        super(token, message);
    }

    public CompilationCritical(int locationColumn, int locationRow, String message) {
        super(locationColumn, locationRow, message);
    }

    @Override
    public String type() {
        return TYPE;
    }

    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }
}
