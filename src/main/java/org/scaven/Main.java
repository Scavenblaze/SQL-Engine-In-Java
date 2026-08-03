package org.scaven;

import org.scaven.models.Column;
import org.scaven.models.Datatype;
import org.scaven.models.Row;
import org.scaven.models.Table;

import java.util.List;

public class Main {
    static void main() {
        Table table = new Table("Student");

        table.addColumn(new Column("id", Datatype.INT));
        table.addColumn(new Column("name", Datatype.STRING));
        table.addColumn(new Column("age", Datatype.INT));

        table.addRow(new Row(List.of(1, "Lando", 29)));
        table.addRow(new Row(List.of(2, "Oscar", 17)));
        table.addRow(new Row(List.of(3, "Max", 32)));

        System.out.println("Table name is: " + table.getTableName());
        System.out.println("Column name is: " + table.getColumns());
        System.out.println("Row name is: " + table.getRows());

    }
}

//TODO: Create test cases to test models