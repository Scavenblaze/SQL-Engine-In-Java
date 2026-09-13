package org.scaven.models;


// Column will take input of Column Name and its datatype
public class Column{

    private final String columnName;
    private final Datatype type;

    public Column(String columnName, Datatype type) {
        this.columnName = columnName;
        this.type = type;
    }

    public String getColumnName() {
        return columnName;
    }

    public Datatype getType() {
        return type;
    }

    @Override
    public String toString() {
        return columnName + "(" + type + ")";
    }

}

//example
//columns = {id, name, age}
//rows    = {1, "AAA", 19}