package org.scaven.statements;

public record DeleteStatement(
        String tableName,
        Condition condition
) implements Statement{
}
