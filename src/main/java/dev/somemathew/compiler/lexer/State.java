package dev.somemathew.compiler.lexer;

import java.util.HashSet;

import static com.google.common.base.Preconditions.checkArgument;

public class State {
    private static final HashSet<Integer> idDb = new HashSet<>();

    private final int id;

    private final int backTrackCount;
    private final boolean isFinalState;
    private final TokenType tokenType;
    private final String description;

    private State(int id, int backTrackCount, boolean isFinalState, TokenType type, String description) {
        if (idDb.contains(id)) {
            throw new IllegalArgumentException("Ids of states must be Unique.");
        } else {
            idDb.add(id);
        }
        this.id = id;

        checkArgument(backTrackCount >= 0, "Backtrack count must be non-negative. Given: %s", backTrackCount);
        this.backTrackCount = backTrackCount;
        this.isFinalState = isFinalState;
        this.tokenType = type;
        this.description = description;
    }

    public boolean isFinal() {
        return isFinalState;
    }

    public TokenType getTokenType() {
        return tokenType;
    }

    public int getBacktrackCount() {
        return backTrackCount;
    }

    private int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static State nonFinal(int id) {
        return new Builder().id(id);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + getId();
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;

        int otherId;
        if (obj instanceof State) {
            State other = (State) obj;
            otherId = other.id;
        } else if (obj instanceof SearchStub) {
            SearchStub other = (SearchStub) obj;
            otherId = other.id;
        } else {
            return false;
        }

        return id == otherId;
    }

    @Override
    public String toString() {
        return "State [id=" +
                id +
                ", backTrackCount=" +
                backTrackCount +
                ", isFinalState=" +
                isFinalState +
                ", tokenType=" +
                tokenType +
                ", description=" +
                description +
                "]";
    }

    public static class Builder {
        private int backTrackCount;
        private boolean isFinalState;
        private TokenType tokenType;
        private String description;

        private Builder() {
            this.backTrackCount = 0;
            this.isFinalState = false;
            this.tokenType = null;
            this.description = null;
        }

        public Builder setFinal() {
            this.isFinalState = true;
            return this;
        }

        public Builder ofType(TokenType type) {
            this.tokenType = type;
            return this;
        }

        public Builder withDescription(String desc) {
            this.description = desc;
            return this;
        }

        public Builder withBacktrack() {
            this.backTrackCount = 1;
            return this;
        }

        public State id(int id) {
            return new State(id, backTrackCount, isFinalState, tokenType, description);
        }
    }

    static final class SearchStub {
        private final int id;

        public SearchStub(int id) {
            this.id = id;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + id;
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;

            int otherId;
            if (obj instanceof State) {
                State other = (State) obj;
                otherId = other.id;
            } else if (obj instanceof SearchStub) {
                SearchStub other = (SearchStub) obj;
                otherId = other.id;
            } else {
                return false;
            }

            return id == otherId;
        }

    }
}
