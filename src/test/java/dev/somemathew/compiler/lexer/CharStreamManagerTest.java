package dev.somemathew.compiler.lexer;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


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
    void consumeChar() {
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