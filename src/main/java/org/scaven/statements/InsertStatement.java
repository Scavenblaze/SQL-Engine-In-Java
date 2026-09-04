package org.scaven.statements;

import java.util.List;

public record InsertStatement(
        String tableName,
        List<String> values
) implements Statement{
}
