package org.scaven.execution;

import org.scaven.models.Datatype;
import org.scaven.models.Row;
import org.scaven.models.Table;
import org.scaven.statements.InsertStatement;
import org.scaven.storage.ProtoConverter;
import org.scaven.storage.StorageEngine;

import java.util.ArrayList;
import java.util.List;


public class InsertExecutor implements Executor{
    private final StorageEngine storageEngine;
    private final InsertStatement statement;

    public InsertExecutor(StorageEngine storageEngine, InsertStatement statement){
        this.storageEngine = storageEngine;
        this.statement = statement;
    }

    @Override
    public String execute(){
        String tableName = statement.tableName();

        if(!storageEngine.tableExists(tableName)){
            throw new IllegalArgumentException("Table '" + tableName + "' does not exist");
        }

        Table table = storageEngine.loadTable(tableName);
        List<String> rawValues = statement.values();

        //check value count matches column count
        int expectedColumns = table.getColumns().size();
        if(rawValues.size() != expectedColumns){
            throw new IllegalArgumentException("Column count mismatch: table '" + tableName + "' has " + expectedColumns + " columns but " + rawValues.size() + " values were provided");
        }

        List<Object> typedValues = new ArrayList<>();
        for(int i = 0; i < rawValues.size(); i++){
            Datatype type = table.getColumns().get(i).getType();
            try{
                typedValues.add(ProtoConverter.stringToObject(rawValues.get(i), type));
            }catch (NumberFormatException e){
                throw new IllegalArgumentException("Cannot convert value '" + rawValues.get(i) + "' to " + type + " for column '" + table.getColumns().get(i).getColumnName() + "'", e);
            }
        }

        table.addRow(new Row(typedValues));
        storageEngine.saveTable(table);

        return "1 row inserted into '" + tableName + "'";
    }
}
