package org.scaven.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Table implements Serializable {
    private List<Column> columns =  new ArrayList<Column>();
    private List<Row> rows = new ArrayList<Row>();
    private String tableName;

    public Table(String tableName) {
        this.tableName = tableName;
    }

    public List<Column> getColumns() {
        return columns;
    }
    public List<Row> getRows() {
        return rows;
    }
    public String getTableName() {
        return tableName;
    }

    public void addColumn(Column column) {
        columns.add(column);
    }
    public void addRow(Row row) {
        rows.add(row);
    }

    @Override
    public String toString() {
        return "Table {name = '" + tableName + "', Columns = '" + columns + "', Rows = '"  + rows + "'}";
    }
}
