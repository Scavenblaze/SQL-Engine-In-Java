package org.scaven.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

//Row will take input of values of different datatypes and append it to the empty list
public class Row implements Serializable {
    private List<Object> values = new ArrayList<>();

    public Row(List<Object> values) {
        this.values = values;
    }

    public List<Object> getValues() {
        return values;
    }

    public void addValues(Object value) {
        values.add(value);
    }

    @Override
    public String toString() {
        return values.toString();
    }
}

//example
//columns = {id, name, age}
//rows    = {1, "AAA", 19}