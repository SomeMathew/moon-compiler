package dev.somemathew.compiler.logging;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

public class LoggingManager {
    private static LoggingManager ourInstance = new LoggingManager();
    private List<CompilationMessage> messages;


    public static LoggingManager getInstance() {
        return ourInstance;
    }

    private LoggingManager() {
        this.messages = new LinkedList<>();
    }

    public void addMessage(CompilationMessage message) {
        this.messages.add(checkNotNull(message));
    }

    public List<CompilationMessage> getOrderedMessages() {
        this.messages.sort(new PositionComparator());
        return this.messages;
    }

    public boolean hasErrors() {
        return messages.stream().anyMatch(msg -> msg instanceof CompilationError);
    }

    private class PositionComparator implements Comparator<CompilationMessage> {
        @Override
        public int compare(CompilationMessage msg1, CompilationMessage msg2) {
            int lineCompared = Integer.compare(msg1.getLocationRow(), msg2.getLocationRow());
            if (lineCompared != 0) {
                return lineCompared;
            } else {
                return Integer.compare(msg1.getLocationColumn(), msg2.getLocationColumn());
            }
        }
    }
}
