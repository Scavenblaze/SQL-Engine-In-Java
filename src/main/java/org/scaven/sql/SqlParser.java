package org.scaven.sql;

import org.scaven.models.Datatype;
import org.scaven.statements.*;

import java.util.ArrayList;
import java.util.List;

public class SqlParser {
    private final List<Token> tokens;
    private int position = 0;

    public SqlParser(List<Token> tokens) {
        this.tokens = tokens;
    }

    public Statement parse() {
        if(tokens.isEmpty()) {
            throw new IllegalStateException("No tokens in SqlParser");
        }

        Statement statement = switch (tokens.get(position).type()) {
            case SELECT -> parseSelect();
            case INSERT -> parseInsert();
            case CREATE -> parseCreateTable();
            case DELETE -> parseDelete();
            default -> throw new IllegalArgumentException("Unexpected Token: " + tokens.get(position));
        };

        // Consume any optional trailing semicolons
        while (match(TokenType.SEMICOLON)) {
            // keep consuming
        }

        if (position < tokens.size()) {
            throw new IllegalArgumentException("Unexpected token after statement: " + tokens.get(position));
        }

        return statement;
    }

    private SelectStatement parseSelect() {
        consume(TokenType.SELECT);

        List<String> columns = parseSelectedColumns();
        consume(TokenType.FROM);
        String tableName = consume(TokenType.IDENTIFIER).value();
        Condition condition = null;

        if(match(TokenType.WHERE)) {
            condition = parseCondition();
        }

        String orderBy = null;

        if(match(TokenType.ORDER)){
            consume(TokenType.BY);
            orderBy = consume(TokenType.IDENTIFIER).value();
        }
        match(TokenType.SEMICOLON);
        return new SelectStatement(columns, tableName, condition, orderBy);
    }

    private List<String> parseSelectedColumns() {
        List<String> columns = new ArrayList<>();

        if(match(TokenType.STAR)) {
            columns.add("*");
            return columns;
        }

        columns.add(consume(TokenType.IDENTIFIER).value());

        while(match(TokenType.COMMA)) {
            columns.add(consume(TokenType.IDENTIFIER).value());
        }

        return columns;
    }

    private Condition parseCondition() {
        String column = consume(TokenType.IDENTIFIER).value();
        Operator operator = parseOperator();
        String value = parseValue();

        return new Condition(column, operator, value);
    }

    private Operator parseOperator() {
        Token token = tokens.get(position);

        return switch (token.type()){
            case EQUALS -> {
                position++;
                yield Operator.EQUALS;
            }

            case GREATER_THAN -> {
                position++;
                yield Operator.GREATER_THAN;
            }

            case LESS_THAN -> {
                position++;
                yield Operator.LESS_THAN;
            }

            default -> throw new IllegalArgumentException("Unexpected Operator found: " + token);
        };
    }

    private String parseValue() {
        Token token = tokens.get(position);

        return switch(token.type()){
            case STRING, INTEGER, DOUBLE_LITERAL, IDENTIFIER -> {
                position++;
                yield token.value();
            }

            default -> throw new IllegalArgumentException("Unexpected Value found: " + token);
        };
    }

    private InsertStatement parseInsert() {
        consume(TokenType.INSERT);
        consume(TokenType.INTO);

        String tableName = consume(TokenType.IDENTIFIER).value();

        consume(TokenType.VALUES);
        consume(TokenType.LEFT_PAREN);

        List<String> values = new ArrayList<>();

        values.add(parseValue());

        while(match(TokenType.COMMA)) {
            values.add(parseValue());
        }

        consume(TokenType.RIGHT_PAREN);
        match(TokenType.SEMICOLON);

        return new InsertStatement(tableName, values);
    }

    private CreateStatement parseCreateTable(){
        consume(TokenType.CREATE);
        consume(TokenType.TABLE);

        String tableName = consume(TokenType.IDENTIFIER).value();
        consume(TokenType.LEFT_PAREN);

        List<String> columnNames = new ArrayList<>();
        List<Datatype> columnTypes = new ArrayList<>();
        parseColumnDefinition(columnNames, columnTypes);

        while(match(TokenType.COMMA)) {
            parseColumnDefinition(columnNames, columnTypes);
        }

        consume(TokenType.RIGHT_PAREN);
        match(TokenType.SEMICOLON);

        return new CreateStatement(tableName, columnNames, columnTypes);
    }

    private Datatype parseDataType() {
        Token token = tokens.get(position);

        position++;

        return switch (token.type()) {
            case INT -> Datatype.INT;
            case STRING -> Datatype.STRING;
            case DOUBLE -> Datatype.DOUBLE;
            case BOOLEAN -> Datatype.BOOLEAN;

            default -> throw new IllegalArgumentException("Unexpected Datatype found: " + token);
        };
    }

    private void parseColumnDefinition(List<String> columnNames, List<Datatype> columnTypes) {
        String columnName = consume(TokenType.IDENTIFIER).value();
        Datatype datatype = parseDataType();

        columnNames.add(columnName);
        columnTypes.add(datatype);
    }

    private DeleteStatement parseDelete() {
        consume(TokenType.DELETE);
        consume(TokenType.FROM);
        String tableName = consume(TokenType.IDENTIFIER).value();
        Condition condition = null;

        if(match(TokenType.WHERE)) {
            condition = parseCondition();
        }

        match(TokenType.SEMICOLON);

        return new DeleteStatement(tableName, condition);
    }

    private Token consume(TokenType expected) {
        if(position >= tokens.size()) {
            throw new IllegalArgumentException("Expected " + expected + " but reached end of input");
        }

        Token token = tokens.get(position);

        if(token.type() != expected) {
            throw new IllegalArgumentException("Expected " + expected + " but found " + token.type());
        }

        position++;
        return token;
    }

    private boolean match(TokenType type) {
        if(position < tokens.size() && tokens.get(position).type() == type) {
            position++;
            return true;
        }
        return false;
    }

}
