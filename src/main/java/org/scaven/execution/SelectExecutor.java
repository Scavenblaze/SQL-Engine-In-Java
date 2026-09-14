package org.scaven.execution;

import org.scaven.models.Row;
import org.scaven.models.Table;
import org.scaven.statements.SelectStatement;
import org.scaven.storage.StorageEngine;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


public class SelectExecutor implements Executor{
    private final StorageEngine storageEngine;
    private final SelectStatement statement;

    public SelectExecutor(StorageEngine storageEngine, SelectStatement statement){
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

        //determine which column indices to project
        List<Integer> projectedIndices = resolveColumnIndices(table);

        //filter rows by WHERE condition
        List<Row> filteredRows = new ArrayList<>();
        for(Row row : table.getRows()){
            if(ConditionEvaluator.matches(row, statement.condition(), table)){
                filteredRows.add(row);
            }
        }

        //apply ORDER BY sorting
        if(statement.orderBy() != null){
            int orderIndex = table.getColumnIndex(statement.orderBy());
            if(orderIndex < 0){
                throw new IllegalArgumentException("Unknown ORDER BY column: " + statement.orderBy());
            }
            filteredRows.sort(createComparator(orderIndex));
        }

        return formatResults(table, filteredRows, projectedIndices);
    }


    private List<Integer> resolveColumnIndices(Table table){
        List<String> requestedColumns = statement.columns();
        List<Integer> indices = new ArrayList<>();

        if(requestedColumns.size() == 1 && requestedColumns.getFirst().equals("*")){
            for(int i = 0; i < table.getColumns().size(); i++){
                indices.add(i);
            }
        }
        else{
            for(String colName : requestedColumns){
                int idx = table.getColumnIndex(colName);
                if (idx < 0){
                    throw new IllegalArgumentException("Unknown column: " + colName);
                }
                indices.add(idx);
            }
        }

        return indices;
    }

    //resolves column names to their indices. Supports '*' for all columns
    @SuppressWarnings("unchecked")
    private Comparator<Row> createComparator(int columnIndex){
        return(row1, row2) -> {
            Object v1 = row1.getValue(columnIndex);
            Object v2 = row2.getValue(columnIndex);

            if(v1 == null && v2 == null) return 0;
            if(v1 == null) return -1;
            if(v2 == null) return 1;

            if(v1 instanceof Comparable<?> c1 && v2 instanceof Comparable<?>) {
                return((Comparable<Object>) c1).compareTo(v2);
            }

            return v1.toString().compareTo(v2.toString());
        };
    }


    //formats query results as table with headers and aligned columns
    private String formatResults(Table table, List<Row> rows, List<Integer> projectedIndices){
        if(rows.isEmpty()){
            return "(0 rows)";
        }

        //build header names
        List<String> headers = new ArrayList<>();
        for(int idx : projectedIndices){
            headers.add(table.getColumns().get(idx).getColumnName());
        }

        //build data rows
        List<List<String>> dataRows = new ArrayList<>();
        for(Row row : rows){
            List<String> dataRow = new ArrayList<>();
            for(int idx : projectedIndices){
                Object val = row.getValue(idx);
                dataRow.add(val == null ? "NULL" : val.toString());
            }
            dataRows.add(dataRow);
        }

        //column widths
        int[] widths = new int[headers.size()];
        for(int i = 0; i < headers.size(); i++){
            widths[i] = headers.get(i).length();
        }
        for(List<String> dataRow : dataRows){
            for(int i = 0; i < dataRow.size(); i++){
                widths[i] = Math.max(widths[i], dataRow.get(i).length());
            }
        }

        //format output
        var sb = new StringBuilder();

        //header line
        for(int i = 0; i < headers.size(); i++){
            if(i > 0) sb.append(" | ");
            sb.append(padRight(headers.get(i), widths[i]));
        }
        sb.append('\n');

        //separator line
        for(int i = 0; i < headers.size(); i++){
            if(i > 0) sb.append("-+-");
            sb.append("-".repeat(widths[i]));
        }
        sb.append('\n');

        //data lines
        for(List<String> dataRow : dataRows){
            for(int i = 0; i < dataRow.size(); i++){
                if(i > 0) sb.append(" | ");
                sb.append(padRight(dataRow.get(i), widths[i]));
            }
            sb.append('\n');
        }

        sb.append("(").append(rows.size()).append(rows.size() == 1 ? " row)" : " rows)");
        return sb.toString();
    }

    private static String padRight(String s, int width){
        if(s.length() >= width) return s;
        return s + " ".repeat(width - s.length());
    }
}
