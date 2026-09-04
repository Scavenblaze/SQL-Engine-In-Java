package org.scaven.statements;

import java.util.List;

public record SelectStatement(
        List<String> columns,
        String tableName,
        Condition condition,
        String orderBy
) implements Statement {}
