package dev.somemathew.compiler.logging;

import dev.somemathew.compiler.lexer.Token;

public class CompilationWarning extends CompilationMessage {
    private static final String TYPE = "WARNING";

    public CompilationWarning() {
        super();
    }

    public CompilationWarning(Token token, String message) {
        super(token, message);
    }

    public CompilationWarning(int locationColumn, int locationRow, String message) {
        super(locationColumn, locationRow, message);
    }

    @Override
    public String type() {
        return TYPE;
    }
}
