package org.scaven.statements;

public record Condition(
        String column,
        Operator operator,
        String value
) {}