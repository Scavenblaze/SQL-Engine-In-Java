package org.scaven;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.scaven.models.Row;
import org.scaven.models.Table;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RowTest {

    @ParameterizedTest
    @CsvSource({
            "1, Ezio, 27.5, true",
            "2, Altair, 21.2, true",
            "3, Desmond, 9.8, false"
    })
    void shouldAddRow(int id, String name, Double marks, Boolean pass) {
        Row row =  new Row(List.of(id,  name, marks, pass));
        assertEquals(List.of(id, name, marks, pass), row.getValues());
    }

    @Test
    void addRowToTable(){
        List<Row> expectedRows = List.of(
                new Row(List.of(1, "Ezio", 27.5, true)),
                new Row(List.of(2, "Altair", 21.2, true)),
                new Row(List.of(3, "Desmond", 9.8, false))
        );

        Table table = new Table("TestTable");
        for(Row row: expectedRows){
            table.addRow(row);
        }
    }
}

//TODO: Prevent addition of rows without any columns
//TODO: Add more org.scaven.test cases like no rows, one row, etc
