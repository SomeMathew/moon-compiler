package dev.somemathew.compiler.logging;

import dev.somemathew.compiler.lexer.Token;

public class CompilationErrorSemantic extends CompilationError {
    private static final String TYPE = "ERROR_SEMANTIC";

    public CompilationErrorSemantic() {
        super(-1, -1, null);
    }

    public CompilationErrorSemantic(Token token, String message) {
        super(token, message);
    }

    public CompilationErrorSemantic(int locationColumn, int locationRow, String message) {
        super(locationColumn, locationRow, message);
    }

    @Override
    public String type() {
        return TYPE;
    }
}
