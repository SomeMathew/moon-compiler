package dev.somemathew.compiler.lexer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;


class CharStreamManagerTest {
    private static String TEST_STRING = "Test String";
    private static String TEST_STRING_WITH_END_BLANKS = "TestString    ";
    private static String EMPTY_STRING = "";

    private CharStreamManager charStreamManagerTestString;
    private CharStreamManager charStreamManagerEmptyString;
    private CharStreamManager charStreamManagerEndBlanks;
    @BeforeEach
    void setUp() {
        charStreamManagerTestString = new CharStreamManager(TEST_STRING);
        charStreamManagerEmptyString = new CharStreamManager(EMPTY_STRING);
        charStreamManagerEndBlanks = new CharStreamManager(TEST_STRING_WITH_END_BLANKS);
    }

    @Test
    void whenNotForwardedPeekCharReturnsTheSameChar() {
        char firstChar = charStreamManagerTestString.peekChar();
        char secondChar = charStreamManagerTestString.peekChar();
        assertEquals(firstChar, secondChar);
    }

    @Test
    void whenConsumedPeekCharReturnsTheNextChar() {
        char firstChar = charStreamManagerTestString.peekChar();
        charStreamManagerTestString.consumeChar();
        char secondChar = charStreamManagerTestString.peekChar();

        assertEquals(TEST_STRING.charAt(0), firstChar);
        assertEquals(TEST_STRING.charAt(1), secondChar);
    }

    @Test
    void whenForwardedPeekCharReturnsTheNextChar() {
        char firstChar = charStreamManagerTestString.peekChar();
        charStreamManagerTestString.forward(1);
        char secondChar = charStreamManagerTestString.peekChar();

        assertEquals(TEST_STRING.charAt(0), firstChar);
        assertEquals(TEST_STRING.charAt(1), secondChar);
    }

    @Test
    void whenStreamIsCompletePeekCharThrowsAnException() {
        charStreamManagerTestString.forward(TEST_STRING.length());
        assertThrows(NoSuchElementException.class, charStreamManagerTestString::peekChar);
    }

    @Test
    void givenAnEmptyStringPeekCharThrowsAnException() {
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
    void givenBlanksAfterHeadSkipBlanksAdvancesTheHeadToANonBlank() {
        int blankIndex = TEST_STRING.indexOf(' ');
        charStreamManagerTestString.forward(blankIndex);

        charStreamManagerTestString.skipBlanks();
        assertEquals(TEST_STRING.charAt(blankIndex + 1), charStreamManagerTestString.peekChar());
    }

    @Test
    void givenBlanksToTheEndSkipsBlanksAdvancesToEOF() {
        int blankIndex = TEST_STRING_WITH_END_BLANKS.indexOf(' ');
        charStreamManagerEndBlanks.forward(blankIndex);

        charStreamManagerEndBlanks.skipBlanks();
        assertFalse(charStreamManagerEndBlanks.peekToken().isPresent());
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