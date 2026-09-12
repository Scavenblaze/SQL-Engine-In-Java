package org.scaven.storage;

import org.scaven.database.proto.DataType;
import org.scaven.models.Column;
import org.scaven.models.Datatype;
import org.scaven.models.Row;
import org.scaven.models.Table;

import java.util.ArrayList;
import java.util.List;


//ProtoConverter class because Row uses List<Objects> (for string and datatype) but protobuf can only store repeated string

public final class ProtoConverter {

    private ProtoConverter() {}

    //Converts Table to protobuf table
    public static org.scaven.database.proto.Table toProtoTable(Table table) {
        var builder = org.scaven.database.proto.Table.newBuilder()
                .setName(table.getTableName());

        for (Column column : table.getColumns()) {
            builder.addColumns(toProtoColumn(column));
        }

        for (Row row : table.getRows()) {
            builder.addRows(toProtoRow(row));
        }

        return builder.build();
    }

    //Converts Column to protobuf column
    public static org.scaven.database.proto.Column toProtoColumn(Column column) {
        return org.scaven.database.proto.Column.newBuilder()
                .setName(column.getColumnName())
                .setType(toProtoDataType(column.getType()))
                .build();
    }

    //Converts Row to protobuf row
    public static org.scaven.database.proto.Row toProtoRow(Row row) {
        var builder = org.scaven.database.proto.Row.newBuilder();
        for (Object value : row.getValues()) {
            builder.addValues(objectToString(value));
        }
        return builder.build();
    }

    //Converts Datatype to protobuf datatype
    public static DataType toProtoDataType(Datatype type) {
        return switch (type) {
            case INT -> DataType.INT;
            case STRING -> DataType.STRING;
            case DOUBLE -> DataType.DOUBLE;
            case BOOLEAN -> DataType.BOOLEAN;
        };
    }


    //Converts protobuf Table to Table
    public static Table fromProtoTable(org.scaven.database.proto.Table protoTable) {
        Table table = new Table(protoTable.getName());

        //columns
        List<Datatype> columnTypes = new ArrayList<>();
        for (org.scaven.database.proto.Column protoCol : protoTable.getColumnsList()) {
            Datatype type = fromProtoDataType(protoCol.getType());
            table.addColumn(new Column(protoCol.getName(), type));
            columnTypes.add(type);
        }

        //rows with typed values
        for (org.scaven.database.proto.Row protoRow : protoTable.getRowsList()) {
            List<Object> values = new ArrayList<>();
            List<String> stringValues = protoRow.getValuesList();

            for (int i = 0; i < stringValues.size(); i++) {
                Datatype type = (i < columnTypes.size()) ? columnTypes.get(i) : Datatype.STRING;
                values.add(stringToObject(stringValues.get(i), type));
            }

            table.addRow(new Row(values));
        }

        return table;
    }

    //Converts protobuf datatype to Datatype
    public static Datatype fromProtoDataType(DataType protoType) {
        return switch (protoType) {
            case INT -> Datatype.INT;
            case STRING -> Datatype.STRING;
            case DOUBLE -> Datatype.DOUBLE;
            case BOOLEAN -> Datatype.BOOLEAN;
            default -> throw new IllegalArgumentException("Unknown protobuf DataType: " + protoType);
        };
    }

    //Serializes object to string for protobuf storage
    public static String objectToString(Object value) {
        if (value == null) {
            return "";
        }
        return value.toString();
    }

    //Deserialize back based on column's datatype
    public static Object stringToObject(String value, Datatype type) {
        if (value == null) {
            return null;
        }
        if (value.equalsIgnoreCase("null")) {
            return null;
        }
        if (value.isEmpty() && type != Datatype.STRING) {
            return null;
        }

        return switch (type) {
            case INT -> Integer.parseInt(value);
            case DOUBLE -> Double.parseDouble(value);
            case BOOLEAN -> {
                if ("true".equalsIgnoreCase(value)) yield Boolean.TRUE;
                if ("false".equalsIgnoreCase(value)) yield Boolean.FALSE;
                throw new IllegalArgumentException("Cannot convert '" + value + "' to BOOLEAN (expected 'true' or 'false')");
            }
            case STRING -> value;
        };
    }
}
