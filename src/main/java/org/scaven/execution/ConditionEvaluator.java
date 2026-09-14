package org.scaven.execution;

import org.scaven.models.Datatype;
import org.scaven.models.Row;
import org.scaven.models.Table;
import org.scaven.statements.Condition;
import org.scaven.statements.Operator;


public class ConditionEvaluator{

    private ConditionEvaluator(){}

    public static boolean matches(Row row, Condition condition, Table table){
        if(condition == null){
            return true;
        }

        int colIndex = table.getColumnIndex(condition.column());
        if(colIndex < 0){
            throw new IllegalArgumentException("Unknown column in WHERE clause: " + condition.column());
        }

        Object rowValue = row.getValue(colIndex);
        Datatype type = table.getColumns().get(colIndex).getType();
        String conditionValue = condition.value();
        Operator operator = condition.operator();

        return evaluateCondition(rowValue, conditionValue, type, operator);
    }

    private static boolean evaluateCondition(Object rowValue, String conditionValue, Datatype type, Operator operator){
        if(rowValue == null){
            return false;
        }

        return switch (type){
            case INT -> {
                int rowInt = ((Number) rowValue).intValue();
                int condInt = Integer.parseInt(conditionValue);
                yield compareInts(rowInt, condInt, operator);
            }
            case DOUBLE -> {
                double rowDbl = ((Number) rowValue).doubleValue();
                double condDbl = Double.parseDouble(conditionValue);
                yield compareDoubles(rowDbl, condDbl, operator);
            }
            case STRING -> {
                String rowStr = rowValue.toString();
                yield compareStrings(rowStr, conditionValue, operator);
            }
            case BOOLEAN -> {
                boolean rowBool = (Boolean) rowValue;
                boolean condBool = Boolean.parseBoolean(conditionValue);
                yield compareBooleans(rowBool, condBool, operator);
            }
        };
    }

    private static boolean compareInts(int a, int b, Operator op){
        return switch (op){
            case EQUALS -> a == b;
            case GREATER_THAN -> a > b;
            case LESS_THAN -> a < b;
        };
    }

    private static boolean compareDoubles(double a, double b, Operator op){
        return switch (op){
            case EQUALS -> Double.compare(a, b) == 0;
            case GREATER_THAN -> a > b;
            case LESS_THAN -> a < b;
        };
    }

    private static boolean compareStrings(String a, String b, Operator op){
        return switch (op){
            case EQUALS -> a.equalsIgnoreCase(b);
            case GREATER_THAN -> a.compareToIgnoreCase(b) > 0;
            case LESS_THAN -> a.compareToIgnoreCase(b) < 0;
        };
    }

    private static boolean compareBooleans(boolean a, boolean b, Operator op){
        return switch (op){
            case EQUALS -> a == b;
            case GREATER_THAN, LESS_THAN -> false; //comparison not meaningful for booleans
        };
    }
}
