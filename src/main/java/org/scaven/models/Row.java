package org.scaven.models;

import java.util.ArrayList;
import java.util.List;

//Row will take input of values of different datatypes and append it to the empty list
public class Row{
    private List<Object> values;

    public Row(List<Object> values) {
        this.values = new ArrayList<>(values);
    }

    public List<Object> getValues() {
        return values;
    }

    public Object getValue(int index){
        return values.get(index);
    }

    public void setValue(int index, Object value){
        values.set(index, value);
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