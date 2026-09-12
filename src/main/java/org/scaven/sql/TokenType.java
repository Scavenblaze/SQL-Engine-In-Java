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
    AND,
    OR,

    INT,
    STRING,
    DOUBLE,
    BOOLEAN,

    IDENTIFIER,
    INTEGER,
    DOUBLE_LITERAL,

    COMMA,
    SEMICOLON,
    LEFT_PAREN,
    RIGHT_PAREN,

    EQUALS,
    GREATER_THAN,
    LESS_THAN,

    STAR

}
