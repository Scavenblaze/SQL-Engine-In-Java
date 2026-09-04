package org.scaven.statements;

import org.scaven.database.proto.DataType;

import java.util.List;

public record CreateStatement(
        String tableName,
        List<String> columnNames,
        List<DataType> columnTypes
) implements Statement {}