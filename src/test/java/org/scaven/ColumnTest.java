package org.scaven;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.scaven.models.Column;
import org.scaven.models.Datatype;
import org.scaven.models.Table;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ColumnTest {

    @ParameterizedTest
    @CsvSource({
            "id, INT",
            "name, STRING",
            "marks, DOUBLE",
            "pass, BOOLEAN"
    })
    void shouldCreateColumn(String columnName, Datatype type) {
        Column column = new Column(columnName, type);

        assertEquals(columnName, column.getColumnName());
        assertEquals(type, column.getType());

    }

    @Test
    void shouldAddColumnToTable() {
        Table table = new Table("TestTable");

        List<Column> expectedColumns = List.of(
                new Column("id", Datatype.INT),
                new Column("name", Datatype.STRING),
                new Column("marks", Datatype.DOUBLE),
                new Column("pass", Datatype.BOOLEAN)
        );
        for  (Column column : expectedColumns) {
            table.addColumn(column);
        }

        List<Column> columns = table.getColumns();
        assertEquals(4,  columns.size());

        assertEquals("id",  columns.get(0).getColumnName());
        assertEquals(Datatype.INT, columns.get(0).getType());

        assertEquals("name", columns.get(1).getColumnName());
        assertEquals(Datatype.STRING, columns.get(1).getType());

        assertEquals("marks", columns.get(2).getColumnName());
        assertEquals(Datatype.DOUBLE, columns.get(2).getType());

        assertEquals("pass", columns.get(3).getColumnName());
        assertEquals(Datatype.BOOLEAN, columns.get(3).getType());
    }
}

//TODO: remove the assertEquals repetition by putting it in a loop
//TODO: Add more org.scaven.test cases like no columns, one column, etc