package dev.somemathew.compiler.logging;

import dev.somemathew.compiler.lexer.Token;

public class CompilationErrorLexical extends CompilationError {
    private static final String TYPE = "ERROR_LEXICAL";

    public CompilationErrorLexical() {
        super(-1, -1, null);
    }

    public CompilationErrorLexical(Token token, String message) {
        super(token, message);
    }

    public CompilationErrorLexical(int locationColumn, int locationRow, String message) {
        super(locationColumn, locationRow, message);
    }

    @Override
    public String type() {
        return TYPE;
    }
}
