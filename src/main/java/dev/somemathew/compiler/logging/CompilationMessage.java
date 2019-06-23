package dev.somemathew.compiler.logging;

import dev.somemathew.compiler.lexer.Token;

public abstract class CompilationMessage {
    private int locationColumn;
    private int locationRow;
    private String message;

    CompilationMessage() {
        this(-1, -1, null);
    }

    CompilationMessage(Token token, String message) {
        this(token.getColumn(), token.getLine(), message);
    }

    CompilationMessage(int locationColumn, int locationRow, String message) {
        this.locationColumn = locationColumn;
        this.locationRow = locationRow;
        if (message == null) {
            this.message = "";
        } else {
            this.message = message;
        }
    }

    public int getLocationColumn() {
        return locationColumn;
    }

    public void setLocationColumn(int locationColumn) {
        this.locationColumn = locationColumn;
    }

    public int getLocationRow() {
        return locationRow;
    }

    public void setLocationRow(int locationRow) {
        this.locationRow = locationRow;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public abstract String type();

    public String toString() {
        return type() + ":"
                + " Location: "
                + "line[" + (locationRow + 1) + "], "
                + "column[" + (locationColumn + 1) + "]"
                + "\n\t\t"
                + getMessage();


    }
}
