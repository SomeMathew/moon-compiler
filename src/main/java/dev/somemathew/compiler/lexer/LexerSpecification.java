package dev.somemathew.compiler.lexer;

import com.google.common.collect.ImmutableTable;

import java.util.HashMap;
import java.util.Map;

public class LexerSpecification implements ILexerSpecification {
    private ImmutableTable<State, Character, State> dfaTransitions;
    private ImmutableTable<State, ControlSymbol, State> dfaControlTransitions;
    private ImmutableTable.Builder<State, Character, State> transitionBuilder;
    private ImmutableTable.Builder<State, ControlSymbol, State> controlTransitionBuilder;
    private State startState;

    private HashMap<String, TokenType> reservedWords;

    /*
     * (non-Javadoc)
     * 
     * @see LexerDFA#getDfaTransitions()
     */
    public ImmutableTable<State, Character, State> getDfaTransitions() {
        return dfaTransitions;
    }

    /*
     * (non-Javadoc)
     * 
     * @see LexerDFA#getDfaControlTransitions()
     */
    public ImmutableTable<State, ControlSymbol, State> getDfaControlTransitions() {
        return dfaControlTransitions;
    }

    public State getStartState() {
        return startState;
    }

    public Map<String, TokenType> getReservedWords() {
        return reservedWords;
    }

    public LexerSpecification() {
        buildTableDFA();
        buildReservedWordsMap();
    }

    private void buildReservedWordsMap() {
        reservedWords = new HashMap<>();
        reservedWords.put("if", TokenType.RW_IF);
        reservedWords.put("then", TokenType.RW_THEN);
        reservedWords.put("else", TokenType.RW_ELSE);
        reservedWords.put("for", TokenType.RW_FOR);
        reservedWords.put("class", TokenType.RW_CLASS);
        reservedWords.put("integer", TokenType.RW_INTEGER);
        reservedWords.put("float", TokenType.RW_FLOAT);
        reservedWords.put("read", TokenType.RW_READ);
        reservedWords.put("write", TokenType.RW_WRITE);
        reservedWords.put("return", TokenType.RW_RETURN);
        reservedWords.put("main", TokenType.RW_MAIN);
        reservedWords.put("void", TokenType.RW_VOID);
    }

    private void buildTableDFA() {
        transitionBuilder = ImmutableTable.builder();
        controlTransitionBuilder = ImmutableTable.builder();

        State s0 = State.nonFinal(0);
        this.startState = s0;

        buildTableForId(s0);
        buildTableForNumbers(s0);

        /* Token: SLASH */
        State s15 = State.nonFinal(15);
        of('/', s0, s15);

        State s16 = State.builder().setFinal().withBacktrack().ofType(TokenType.OP_DIV).id(16);
        anyOther(s15, s16);
        /* *************** */

        buildTableForComment(s15);

        buildTableForOperatorsAndPunctuation(s0);

        State s55EOF = State.builder().setFinal().ofType(TokenType.EOF).id(55);
        eof(s0, s55EOF);

        State s56ErrorInvalidChar = State.builder().setFinal().ofType(TokenType.ERROR)
                .withDescription("Invalid Character To Start A token").id(56);
        anyOther(s0, s56ErrorInvalidChar);

        this.dfaTransitions = transitionBuilder.build();
        this.dfaControlTransitions = controlTransitionBuilder.build();

        transitionBuilder = null;
        controlTransitionBuilder = null;
    }

    private void buildTableForId(State s0) {
        State s1 = State.nonFinal(1);

        letter(s0, s1);
        alphanum(s1, s1);

        State s2Id = State.builder().setFinal().withBacktrack().ofType(TokenType.ID).id(2);
        anyOther(s1, s2Id);
    }

    private void buildTableForNumbers(State s0) {
        /* Token: INTEGER */
        State s3 = State.nonFinal(3);
        nonZeroDigit(s0, s3);
        digit(s3, s3);

        State s4Integer = State.builder().setFinal().withBacktrack().ofType(TokenType.NUM_INTEGER).id(4);
        anyOther(s3, s4Integer);

        State s5 = State.nonFinal(5);
        of('0', s0, s5);
        anyOther(s5, s4Integer);

        /* *************** */

        /* Token: FLOAT */

        State s7ErrorFloat = State.builder().setFinal().withBacktrack().ofType(TokenType.ERROR)
                .withDescription("Invalid Float").id(7);

        State s6 = State.nonFinal(6);
        of('.', s3, s6);
        of('.', s5, s6);
        anyOther(s6, s7ErrorFloat);

        State s8 = State.nonFinal(8);
        digit(s6, s8);
        nonZeroDigit(s8, s8);

        State s10FloatBt = State.builder().setFinal().withBacktrack().ofType(TokenType.NUM_FLOAT).id(10);
        anyOther(s8, s10FloatBt);

        State s9 = State.nonFinal(9);
        of('0', s8, s9);
        of('0', s9, s9);
        nonZeroDigit(s9, s8);
        anyOther(s9, s7ErrorFloat);

        /* Float with Exponent */

        State s11 = State.nonFinal(11);
        of('e', s8, s11);
        anyOther(s11, s7ErrorFloat);

        State s12FloatNoBt = State.builder().setFinal().ofType(TokenType.NUM_FLOAT).id(12);
        of('0', s11, s12FloatNoBt);

        State s13 = State.nonFinal(13);
        anyOf("+-", s11, s13);
        of('0', s13, s12FloatNoBt);
        anyOther(s13, s7ErrorFloat);

        State s14 = State.nonFinal(14);
        nonZeroDigit(s11, s14);
        digit(s14, s14);
        anyOther(s14, s10FloatBt);

        nonZeroDigit(s13, s14);

        /* *************** */
    }

    private void buildTableForComment(State s15) {
        /* Token: COMMENT */
        State s17Comment = State.builder().setFinal().ofType(TokenType.COMMENT).id(17);

        /* Single Line Comment */
        State s18 = State.nonFinal(18);
        of('/', s15, s18);
        of('\n', s18, s17Comment);
        eof(s18, s17Comment);
        anyOther(s18, s18);

        /* Multi Line Comment */
        State s19 = State.nonFinal(19);
        of('*', s15, s19);
        eof(s19, s17Comment);
        anyOther(s19, s19);

        State s20 = State.nonFinal(20);
        of('*', s19, s20);
        of('/', s20, s17Comment);
        eof(s20, s17Comment);
        anyOther(s20, s19);
        /* *************** */
    }

    private void buildTableForOperatorsAndPunctuation(State s0) {
        /* ******************* */
        State s21 = State.nonFinal(21);
        of('=', s0, s21);

        /* Token: ASSIGN */
        State s22Assign = State.builder().setFinal().withBacktrack().ofType(TokenType.OP_ASSIGN).id(22);
        anyOther(s21, s22Assign);

        /* Token: EQUAL_OP */
        State s23EqOp = State.builder().setFinal().ofType(TokenType.OP_EQ).id(23);
        of('=', s21, s23EqOp);
        /* ******************* */

        /* ******************* */
        State s24 = State.nonFinal(24);
        of('<', s0, s24);

        /* Token: LT */
        State s25Lt = State.builder().setFinal().withBacktrack().ofType(TokenType.OP_LT).id(25);
        anyOther(s24, s25Lt);

        /* Token: LTE */
        State s26LtE = State.builder().setFinal().ofType(TokenType.OP_LTE).id(26);
        of('=', s24, s26LtE);

        /* Token: LTGT */
        State s27LtGt = State.builder().setFinal().ofType(TokenType.OP_NEQ).id(27);
        of('>', s24, s27LtGt);
        /* ******************* */

        /* ******************* */
        State s28 = State.nonFinal(28);
        of('>', s0, s28);

        /* Token: GT */
        State s29Gt = State.builder().setFinal().withBacktrack().ofType(TokenType.OP_GT).id(29);
        anyOther(s28, s29Gt);

        /* Token: GTE */
        State s30GtE = State.builder().setFinal().ofType(TokenType.OP_GTE).id(30);
        of('=', s28, s30GtE);
        /* ******************* */

        /* ******************* */
        State s31 = State.nonFinal(31);
        of(':', s0, s31);

        /* Token: COLON */
        State s32Colon = State.builder().setFinal().withBacktrack().ofType(TokenType.COLON).id(32);
        anyOther(s31, s32Colon);

        /* Token: DCOLON */
        State s33DColon = State.builder().setFinal().ofType(TokenType.OP_SR).id(33);
        of(':', s31, s33DColon);
        /* ******************* */

        /* ******************* */
        State s34 = State.nonFinal(34);
        of('&', s0, s34);

        /* Token: AND */
        State s35And = State.builder().setFinal().ofType(TokenType.OP_AND).id(35);
        of('&', s34, s35And);

        State s36ErrorAnd = State.builder().setFinal().withBacktrack().ofType(TokenType.ERROR)
                .withDescription("Invalid AND").id(36);
        anyOther(s34, s36ErrorAnd);
        /* ******************* */

        /* ******************* */
        /* Token: OR */
        State s37 = State.nonFinal(37);
        of('|', s0, s37);

        State s38Or = State.builder().setFinal().ofType(TokenType.OP_OR).id(38);
        of('|', s37, s38Or);

        State s39ErrorOr = State.builder().setFinal().withBacktrack().ofType(TokenType.ERROR)
                .withDescription("Invalid OR").id(39);
        anyOther(s37, s39ErrorOr);
        /* ******************* */

        /* ******************* */
        State s40 = State.nonFinal(40);
        of('*', s0, s40);

        /* Token: RCOMMENT */
        State s41CmtEnd = State.builder().setFinal().ofType(TokenType.RCOMMENT).id(41);
        of('/', s40, s41CmtEnd);

        /* Token: STAR */

        State s42Star = State.builder().setFinal().withBacktrack().ofType(TokenType.OP_MULT).id(42);
        anyOther(s40, s42Star);
        /* ******************* */

        /* ******************* */
        /* Token: SEMICOL */
        State s43Semicol = State.builder().setFinal().ofType(TokenType.SEMICOL).id(43);
        of(';', s0, s43Semicol);

        /* Token: COMMA */
        State s44Comma = State.builder().setFinal().ofType(TokenType.COMMA).id(44);
        of(',', s0, s44Comma);

        /* Token: DOT */
        State s45Dot = State.builder().setFinal().ofType(TokenType.DOT).id(45);
        of('.', s0, s45Dot);

        /* Token: PLUS */
        State s46Plus = State.builder().setFinal().ofType(TokenType.OP_PLUS).id(46);
        of('+', s0, s46Plus);

        /* Token: MINUS */
        State s47Minus = State.builder().setFinal().ofType(TokenType.OP_MINUS).id(47);
        of('-', s0, s47Minus);

        /* Token: EXCLPTS */
        State s48ExclPt = State.builder().setFinal().ofType(TokenType.OP_NOT).id(48);
        of('!', s0, s48ExclPt);

        /* Token: LPAREN */
        State s49LParen = State.builder().setFinal().ofType(TokenType.LPAREN).id(49);
        of('(', s0, s49LParen);

        /* Token: RPAREN */
        State s50RParen = State.builder().setFinal().ofType(TokenType.RPAREN).id(50);
        of(')', s0, s50RParen);

        /* Token: LCURL */
        State s51LCurl = State.builder().setFinal().ofType(TokenType.LCURL).id(51);
        of('{', s0, s51LCurl);

        /* Token: RCURL */
        State s52RCurl = State.builder().setFinal().ofType(TokenType.RCURL).id(52);
        of('}', s0, s52RCurl);

        /* Token: LBRACE */
        State s53LBrace = State.builder().setFinal().ofType(TokenType.LBRACE).id(53);
        of('[', s0, s53LBrace);

        /* Token: RBRACE */
        State s54RBrace = State.builder().setFinal().ofType(TokenType.RBRACE).id(54);
        of(']', s0, s54RBrace);
        /* ******************* */
    }

    private void eof(State start, State end) {
        controlTransitionBuilder.put(start, ControlSymbol.EOF, end);
    }

    private void anyOther(State start, State end) {
        controlTransitionBuilder.put(start, ControlSymbol.OTHER, end);
    }

    private void of(char c, State start, State end) {
        transitionBuilder.put(start, c, end);
    }

    private void anyOf(CharSequence s, State start, State end) {
        for (int i = 0; i < s.length(); i++) {
            transitionBuilder.put(start, s.charAt(i), end);
        }
    }

    private void alphanum(State start, State end) {
        letter(start, end);
        digit(start, end);
        transitionBuilder.put(start, '_', end);
    }

    private void letter(State start, State end) {
        for (char c = 'a'; c <= 'z'; c++) {
            transitionBuilder.put(start, c, end);
        }

        for (char c = 'A'; c <= 'Z'; c++) {
            transitionBuilder.put(start, c, end);
        }
    }

    private void digit(State start, State end) {
        for (char c = '0'; c <= '9'; c++) {
            transitionBuilder.put(start, c, end);
        }
    }

    private void nonZeroDigit(State start, State end) {
        for (char c = '1'; c <= '9'; c++) {
            transitionBuilder.put(start, c, end);
        }
    }
}
