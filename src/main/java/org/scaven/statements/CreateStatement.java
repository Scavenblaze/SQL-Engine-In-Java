package org.scaven.statements;

import org.scaven.models.Datatype;

import java.util.List;

public record CreateStatement(
        String tableName,
        List<String> columnNames,
        List<Datatype> columnTypes
) implements Statement {}