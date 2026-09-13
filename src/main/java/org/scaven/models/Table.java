package org.scaven.models;


import java.util.ArrayList;
import java.util.List;

public class Table{
    private List<Column> columns =  new ArrayList<>();
    private List<Row> rows = new ArrayList<>();
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

    //Returns index of the column with the given name
    public int getColumnIndex(String columnName) {
        for (int i = 0; i < columns.size(); i++) {
            if (columns.get(i).getColumnName().equalsIgnoreCase(columnName)) {
                return i;
            }
        }
        return -1;
    }

    public void removeRow(int index) {
        rows.remove(index);
    }

    @Override
    public String toString() {
        return "Table {name = '" + tableName + "', Columns = '" + columns + "', Rows = '"  + rows + "'}";
    }
}
