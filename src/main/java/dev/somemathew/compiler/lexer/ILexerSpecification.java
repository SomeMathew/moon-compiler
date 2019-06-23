package dev.somemathew.compiler.lexer;

import com.google.common.collect.ImmutableTable;

import java.util.Map;

public interface ILexerSpecification {

    ImmutableTable<State, Character, State> getDfaTransitions();

    ImmutableTable<State, ControlSymbol, State> getDfaControlTransitions();

    Map<String, TokenType> getReservedWords();

    State getStartState();

}