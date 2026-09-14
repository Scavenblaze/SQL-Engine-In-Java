package org.scaven.execution;

import org.scaven.models.Column;
import org.scaven.models.Table;
import org.scaven.statements.CreateStatement;
import org.scaven.storage.StorageEngine;

public class CreateExecutor implements Executor{
    private final StorageEngine storageEngine;
    private final CreateStatement statement;

    public CreateExecutor(StorageEngine storageEngine, CreateStatement statement){
        this.storageEngine = storageEngine;
        this.statement = statement;
    }

    @Override
    public String execute(){
        String tableName = statement.tableName();

        if(storageEngine.tableExists(tableName)){
            throw new IllegalStateException("Table '" + tableName + "' exists");
        }

        Table table = new Table(tableName);

        for(int i = 0; i < statement.columnNames().size(); i++){

            Column column = new Column(
                    statement.columnNames().get(i),
                    statement.columnTypes().get(i)
            );

            table.addColumn(column);
        }

        storageEngine.saveTable(table);
        return "Table '" + tableName + "' created successfully with " + table.getColumns().size() + " columns";
    }
}
