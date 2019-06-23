package dev.somemathew.compiler.lexer;

import com.google.common.collect.ImmutableTable;
import dev.somemathew.compiler.logging.CompilationErrorLexical;
import dev.somemathew.compiler.logging.LoggingManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

import static com.google.common.base.Verify.verify;

public class Lexer {
    private static final LoggingManager LOGGING_MANAGER = LoggingManager.getInstance();
    private final List<Token> tokenList;

    private final ImmutableTable<State, Character, State> dfaTransitions;
    private final ImmutableTable<State, ControlSymbol, State> dfaControlTransitions;
    private final Map<String, TokenType> reservedWords;

    private final State startState;
    private State currentState;

    private final CharStreamManager sourceStream;

    private boolean redirectError;
    private Consumer<Token> redirectErrorOutput;

    private boolean pipeError;
    private Consumer<Token> pipeErrorOutput;

    private boolean skipComments;


    public Lexer(ILexerSpecification spec, CharSequence source) {
        dfaTransitions = spec.getDfaTransitions();
        dfaControlTransitions = spec.getDfaControlTransitions();
        reservedWords = spec.getReservedWords();

        startState = spec.getStartState();
        currentState = startState;

        sourceStream = new CharStreamManager(source);

        redirectError(false, null);
        skipComments = true;

        pipeError(true, this::logLexicalErrors);
        tokenList = new ArrayList<>();
    }

    private void logLexicalErrors(Token errToken) {
        LOGGING_MANAGER.addMessage(new CompilationErrorLexical(errToken, "Lexeme: " + errToken.getLexeme() + ", " +
                "Details: " + errToken.getErrorMessage()));
    }


    /**
     * Redirect the error token to the given Consumer function.
     * <p>
     * The error tokens will no longer be returned by nextToken().
     *
     * @param enable Enables or disables redirection error
     * @param out The consumer of redirection error.
     */
    public void redirectError(boolean enable, Consumer<Token> out) {
        if (enable) {
            redirectError = true;
            redirectErrorOutput = out;
        } else {
            redirectError = false;
            redirectErrorOutput = null;
        }
    }

    /**
     * Send all the errors to the given Consumer function as well as an output of
     * the nextToken() function.
     *
     * @param enable Enables or Disables piping errors.
     * @param out The consumer of the errors.
     */
    public void pipeError(boolean enable, Consumer<Token> out) {
        if (enable) {
            pipeError = true;
            pipeErrorOutput = out;
        } else {
            pipeError = false;
            pipeErrorOutput = null;
        }
    }

    /**
     * Set the lexer to only skip comments and not return them as token in
     * nextToken. This should be only used for testing.
     *
     * @param enable Enable or disables skipping comments from the token returned by the lexer.
     */
    public void skipComments(boolean enable) {
        skipComments = enable;
    }

    /**
     * Retrieve the next token from the source.
     *
     * @return the next token.
     */
    public Token nextToken() {
        resetDFAToStart();
        sourceStream.skipBlanks();

        Token token = null;
        do {
            if (sourceStream.hasNext()) {
                char c = sourceStream.peekChar();
                transition(c);
            } else {
                transition(ControlSymbol.EOF);
            }

            if (currentState.isFinal()) {
                if (currentState.getTokenType() == TokenType.COMMENT && skipComments) {
                    skipComment();
                } else if (currentState.getTokenType() == TokenType.ERROR && redirectError) {
                    redirectErrorToken();
                } else {
                    token = handleToken();
                }
            } else {
                sourceStream.consumeChar();
            }
        } while (token == null);

        tokenList.add(token);
        if (token.getType() == TokenType.ERROR && pipeError && pipeErrorOutput != null) {
            pipeErrorOutput.accept(token);
        }
        return token;
    }

    private void skipComment() {
        if (currentState.getBacktrackCount() == 0) { // Currently only support 1 lookahead
            sourceStream.consumeChar();
        }
        sourceStream.consumeToken();
        resetDFAToStart();
        sourceStream.skipBlanks();
    }

    private void redirectErrorToken() {
        Token errToken = handleToken();
        resetDFAToStart();
        sourceStream.skipBlanks();
        if (redirectErrorOutput != null) {
            redirectErrorOutput.accept(errToken);
        } else if (pipeErrorOutput != null) {
            pipeErrorOutput.accept(errToken);
        }
    }

    private Token handleToken() {
        Token token;
        if (currentState.getBacktrackCount() == 0) { // Currently only support 1 lookahead
            sourceStream.consumeChar();
        }

        TokenType type = currentState.getTokenType();
        Optional<String> lexeme = sourceStream.peekToken();
        int line = sourceStream.getTokenLine();
        int col = sourceStream.getTokenColumn();

        sourceStream.consumeToken();

        if (lexeme.isPresent()) {
            // Handle Reserved Words
            if (type == TokenType.ID && reservedWords.containsKey(lexeme.get())) {
                type = reservedWords.get(lexeme.get());
            }
            token = new Token(type, lexeme.get(), line, col, currentState.getDescription());
        } else {
            // EOF
            verify(type == TokenType.EOF, "ERROR: We should be at EOF if lexeme is null. type: %s, line: %s, col: %s",
                    type, line, col);
            token = new Token(type, null, line, col);
        }
        return token;
    }

    private void resetDFAToStart() {
        currentState = startState;
    }

    /**
     * Transition the dfa on the given character.
     *
     * @param c The transition character.
     */
    private void transition(char c) {
        if (dfaTransitions.contains(currentState, c)) {
            currentState = dfaTransitions.get(currentState, c);
        } else {
            verify(dfaControlTransitions.contains(currentState, ControlSymbol.OTHER),
                    "ERROR: The DFA is not complete. No state for %s, %s", currentState, c);
            currentState = dfaControlTransitions.get(currentState, ControlSymbol.OTHER);
        }
    }

    /**
     * Transition the dfa on the given control character.
     * <p>
     * This is used for EOF and character not explicitly found in the character
     * transition table.
     *
     * @param ctrl The transition control symbol.
     */
    @SuppressWarnings("SameParameterValue")
    private void transition(ControlSymbol ctrl) {
        if (dfaControlTransitions.contains(currentState, ctrl)) {
            currentState = dfaControlTransitions.get(currentState, ctrl);
        } else {
            verify(dfaControlTransitions.contains(currentState, ControlSymbol.OTHER),
                    "ERROR: The DFA is not complete. No state for %s, %s", currentState, ctrl);
            currentState = dfaControlTransitions.get(currentState, ControlSymbol.OTHER);
        }
    }

    public List<Token> getTokenList() {
        return tokenList;
    }
}
