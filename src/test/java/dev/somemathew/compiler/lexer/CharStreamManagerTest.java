package dev.somemathew.compiler.lexer;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;


class CharStreamManagerTest {
    private static String TEST_STRING = "Test String";
    private static String EMPTY_STRING = "";

    private CharStreamManager charStreamManagerTestString;
    private CharStreamManager charStreamManagerEmptyString;
    @BeforeEach
    void setUp() {
        charStreamManagerTestString = new CharStreamManager(TEST_STRING);
        charStreamManagerEmptyString = new CharStreamManager(EMPTY_STRING);
    }

    @Test
    void peekCharReturnsTheSameCharWithNoForward() {
        char firstChar = charStreamManagerTestString.peekChar();
        char secondChar = charStreamManagerTestString.peekChar();
        assertEquals(firstChar, secondChar);
    }

    @Test
    void peekCharReturnsTheNextCharWhenConsumed() {
        char firstChar = charStreamManagerTestString.peekChar();
        charStreamManagerTestString.consumeChar();
        char secondChar = charStreamManagerTestString.peekChar();

        assertEquals(TEST_STRING.charAt(0), firstChar);
        assertEquals(TEST_STRING.charAt(1), secondChar);
    }

    @Test
    void peekCharReturnsTheNextCharWhenForwarded() {
        char firstChar = charStreamManagerTestString.peekChar();
        charStreamManagerTestString.forward(1);
        char secondChar = charStreamManagerTestString.peekChar();

        assertEquals(TEST_STRING.charAt(0), firstChar);
        assertEquals(TEST_STRING.charAt(1), secondChar);
    }

    @Test
    void peekCharThrowsAnExceptionWhenStreamDone() {
        charStreamManagerTestString.forward(TEST_STRING.length());
        assertThrows(NoSuchElementException.class, charStreamManagerTestString::peekChar);
    }

    @Test
    void peekCharThrowsAnExceptionGivenAnEmptyString() {
        assertThrows(NoSuchElementException.class, charStreamManagerEmptyString::peekChar);
    }

    @Test
    void givenMoreCharAvailableConsumeCharMovesTheHeadForward() {
        boolean allCharsMatched = true;
        int i = 0;
        char peekedChar = '\0';
        char expectedChar = '\0';
        for (; i < TEST_STRING.length(); i++) {
            peekedChar = charStreamManagerTestString.peekChar();
            expectedChar = TEST_STRING.charAt(i);
            if (peekedChar != expectedChar) {
                allCharsMatched = false;
                break;
            }
            charStreamManagerTestString.consumeChar();
        }

        assertTrue(allCharsMatched, String.format("The char %c at pos %d did not match %c.", expectedChar, i, peekedChar));
    }

    @Test
    void givenACompleteStreamConsumeCharKeepsEOF() {
        for (int i = 0; i < 5; i++) {
            charStreamManagerEmptyString.consumeChar();
        }

        assertFalse(charStreamManagerEmptyString.peekToken().isPresent());
    }

    @Test
    void skipBlanks() {
    }

    @Test
    void forward() {
    }

    @Test
    void consumeToken() {
    }

    @Test
    void peekToken() {
    }

    @Test
    void getTokenLine() {
    }

    @Test
    void getTokenColumn() {
    }

    @Test
    void hasNext() {
    }
}