package dev.somemathew.compiler.logging;

import dev.somemathew.compiler.lexer.Token;

public class CompilationError extends CompilationMessage {
    private static final String TYPE = "ERROR";

    public CompilationError() {
        super(-1, -1, null);
    }

    public CompilationError(Token token, String message) {
        super(token, message);
    }

    public CompilationError(int locationColumn, int locationRow, String message) {
        super(locationColumn, locationRow, message);
    }

    @Override
    public String type() {
        return TYPE;
    }
}
