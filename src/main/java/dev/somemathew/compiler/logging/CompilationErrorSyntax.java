package dev.somemathew.compiler.logging;

import dev.somemathew.compiler.lexer.Token;

public class CompilationErrorSyntax extends CompilationError {
    private static final String TYPE = "ERROR_SYNTAX";

    public CompilationErrorSyntax() {
        super(-1, -1, null);
    }

    public CompilationErrorSyntax(Token token, String message) {
        super(token, message);
    }

    public CompilationErrorSyntax(int locationColumn, int locationRow, String message) {
        super(locationColumn, locationRow, message);
    }

    @Override
    public String type() {
        return TYPE;
    }
}
