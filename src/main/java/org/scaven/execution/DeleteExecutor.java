package org.scaven.execution;

import org.scaven.models.Row;
import org.scaven.models.Table;
import org.scaven.statements.DeleteStatement;
import org.scaven.storage.StorageEngine;

import java.util.ArrayList;
import java.util.List;

public class DeleteExecutor implements Executor{
    private final StorageEngine storageEngine;
    private final DeleteStatement statement;

    public DeleteExecutor(StorageEngine storageEngine, DeleteStatement statement){
        this.storageEngine = storageEngine;
        this.statement = statement;
    }

    @Override
    public String execute(){
        String tableName = statement.tableName();

        if(!storageEngine.tableExists(tableName)){
            throw new IllegalStateException("Table '" + tableName + "' does not exist");
        }

        Table table = storageEngine.loadTable(tableName);

        List<Integer> toDelete = new ArrayList<>();
        for(int i = 0; i < table.getRows().size(); i++){
            Row row = table.getRows().get(i);
            if(ConditionEvaluator.matches(row, statement.condition(), table)){
                toDelete.add(i);
            }
        }

        for(int i = toDelete.size() - 1; i >= 0; i--){
            table.removeRow(toDelete.get(i));
        }

        storageEngine.saveTable(table);
        return toDelete.size() + (toDelete.size() == 1 ? " row" : "rows") + " deleted from '" + tableName + "'";
    }
}
