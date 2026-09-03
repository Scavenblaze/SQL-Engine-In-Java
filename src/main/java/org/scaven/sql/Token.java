package org.scaven.sql;

public record Token(String value, TokenType type) {

    @Override
    public String toString() {
        return "Token{" +
                "value='" + value + '\'' +
                ", type=" + type +
                '}';
    }
}
