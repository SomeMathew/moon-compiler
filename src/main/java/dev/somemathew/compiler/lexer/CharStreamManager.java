package dev.somemathew.compiler.lexer;

import com.google.common.base.CharMatcher;

import java.util.NoSuchElementException;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

class CharStreamManager {
    private static final CharMatcher BLANK_MATCHER = CharMatcher.whitespace();

    private final CharSequence text;

    private int tailLine;
    private int tailColumn;
    private int tail;

    private int headLine;
    private int headColumn;
    private int head;

    public CharStreamManager(CharSequence text) {
        this.text = checkNotNull(text);
        reset();
    }

    /**
     * Peek the next character but does not advance the head.
     * 
     * @return the character at the head.
     * @throws NoSuchElementException when stream is complete
     */
    public char peekChar() {
        if (hasNext()) {
            return text.charAt(head);
        } else {
            throw new NoSuchElementException("EOF");
        }
    }

    /**
     * Advance the head. The character should be checked with peekChar() first if
     * necessary.
     */
    public void consumeChar() {
        if (hasNext()) {
            incrementHead();
        }
        // Never consume the EOF
    }

    /**
     * Skip any blanks from the current head position AND reset the tail to the head
     * position to prepare for the next token.
     * 
     * This should be used only to advance to the next token once the previous one
     * has been consumed.
     * 
     */
    public void skipBlanks() {
        while (true) {
            if (!hasNext()) {
                break;
            } else if (BLANK_MATCHER.matches(text.charAt(head))) {
                incrementHead();
            } else {
                break;
            }
        }
        tail = head;
        tailLine = headLine;
        tailColumn = headColumn;
    }

    /**
     * Skip n characters at the head indiscriminately.
     * 
     * @param n the number of characters skip.
     */
    public void forward(int n) {
        if (head + n >= text.length()) {
            head = text.length();
        } else {
            head += n;
        }
    }

    /**
     * Consumes the current token. This will move the tail to the head position.
     * 
     * The token should be retrieved first if necessary with peekToken().
     */
    void consumeToken() {
        tail = head;
        tailLine = headLine;
        tailColumn = headColumn;
    }

    /**
     * Retrieve the current token in [tail, end).
     * 
     * This will return an empty Optional if we're at the EOF.
     * 
     * @return The current token delimited by [tail, end). (end exclusive).
     */
    Optional<String> peekToken() {
        if (tail >= text.length()) {
            return Optional.empty();
        } else {
            return Optional.of(text.subSequence(tail, head).toString());
        }
    }

    int getTokenLine() {
        return tailLine;
    }

    int getTokenColumn() {
        return tailColumn;
    }

    boolean hasNext() {
        return head < text.length();
    }

    /**
     * Reset the stream to the beginning.
     */
    private void reset() {
        this.headLine = 0;
        this.headColumn = -1;
        this.head = 0;

        updateHeadLineColumn();
        tail = head;
        tailLine = headLine;
        tailColumn = headColumn;
    }

    private void incrementHead() {
        head++;
        updateHeadLineColumn();
    }

    private void updateHeadLineColumn() {
        if (!hasNext()) {
            headLine = -1;
            headColumn = -1;
            return;
        }

        headColumn++;
        if (head > 0) {
            if (text.charAt(head - 1) == '\n') {
                headLine++;
                headColumn = 0;
            } else if (text.charAt(head - 1) == '\r' && text.charAt(head) != '\n') {
                headLine++;
                headColumn = 0;
            }
        }
    }
}
