package org.scaven.execution;

import org.scaven.models.Column;
import org.scaven.models.Table;
import org.scaven.statements.CreateStatement;
import org.scaven.storage.StorageEngine;

public class CreateExecutor implements Executor{
    private final StorageEngine storageEngine;

    public CreateExecutor(StorageEngine storageEngine) {
        this.storageEngine = storageEngine;
    }

    public void execute(CreateStatement statement) {

        Table table = new Table(statement.tableName());

        for (int i = 0; i < statement.columnNames().size(); i++) {

            Column column = new Column(
                    statement.columnNames().get(i),
                    statement.columnTypes().get(i)
            );

            table.addColumn(column);
        }

        storageEngine.saveTable(table);
    }
}
