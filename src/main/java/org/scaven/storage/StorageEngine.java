package org.scaven.storage;

import org.scaven.models.Table;

public interface StorageEngine{
    void saveTable(Table table);

    Table loadTable(String tableName);

    boolean tableExists(String tableName);

    void deleteTable(String tableName);
}
