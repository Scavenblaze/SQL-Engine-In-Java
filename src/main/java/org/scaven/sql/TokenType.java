package org.scaven.sql;

public enum TokenType {
    SELECT,
    FROM,
    WHERE,
    INSERT,
    INTO,
    VALUES,
    CREATE,
    TABLE,
    DELETE,
    ORDER,
    BY,

    IDENTIFIER,
    INTEGER,
    STRING,

    COMMA,
    SEMICOLON,
    LEFT_PAREN,
    RIGHT_PAREN,

    EQUALS,
    GREATER_THAN,
    LESS_THAN,

    STAR

}
