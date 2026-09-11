package org.scaven.sql;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/* Example Output
SELECT          → Token(SELECT, SELECT)
name            → Token(IDENTIFIER, "name")
,               → Token(COMMA, ",")
age             → Token(IDENTIFIER, "age")
FROM            → Token(FROM, FROM)
students        → Token(IDENTIFIER, "students")
WHERE           → Token(WHERE, WHERE)
age             → Token(IDENTIFIER, "age")
>               → Token(GREATER_THAN, ">")
18              → Token(INTEGER, "18")
;               → Token(SEMICOLON, ";")
 */
public class Tokenizer {
    private final String input;
    private int position;

    private static final Map<String, TokenType> KEYWORDS =  Map.ofEntries(
            Map.entry("SELECT", TokenType.SELECT),
            Map.entry("FROM", TokenType.FROM),
            Map.entry("WHERE", TokenType.WHERE),
            Map.entry("INSERT", TokenType.INSERT),
            Map.entry("INTO", TokenType.INTO),
            Map.entry("VALUES", TokenType.VALUES),
            Map.entry("CREATE", TokenType.CREATE),
            Map.entry("TABLE", TokenType.TABLE),
            Map.entry("DELETE", TokenType.DELETE),
            Map.entry("ORDER", TokenType.ORDER),
            Map.entry("BY", TokenType.BY),

            Map.entry("INT", TokenType.INT),
            Map.entry("STRING", TokenType.STRING),
            Map.entry("DOUBLE", TokenType.DOUBLE),
            Map.entry("BOOLEAN", TokenType.BOOLEAN)
            );

    public Tokenizer(String input) {
        this.input = input;
    }

    public String getInput() {
        return input;
    }

    public List<Token> tokenize() {
        List<Token> tokens = new ArrayList<>();

        while(position < input.length()) {
            if (Character.isWhitespace(input.charAt(position))){
                position++;
            }
            else if(Character.isLetter(input.charAt(position))){
                tokens.add(readWord());
            }
            else if(Character.isDigit(input.charAt(position))){
                tokens.add(readNumber());
            }
            else if(input.charAt(position) == '\''){
                tokens.add(readString());
            }
            else{
                tokens.add(readSymbol());
            }
        }
        return tokens;
    }

    private Token readWord(){
        int start = position;

        while(position < input.length() && Character.isLetterOrDigit(input.charAt(position))){
            position++;
        }

        String word = input.substring(start, position);
        TokenType type = KEYWORDS.get(word.toUpperCase());

        if(type == null){
            type = TokenType.IDENTIFIER;
        }

        return new Token(word, type);
    }

    private Token readNumber(){
        int start = position;

        while(position < input.length() && Character.isDigit(input.charAt(position))){
            position++;
        }

        return new Token(input.substring(start, position), TokenType.INTEGER);
    }

    private Token readString(){
        position++; //skips opening '
        int  start = position;

        while (position < input.length() && input.charAt(position) != '\''){
            position++;
        }
        if(position >= input.length()){ //if there is no second '
            throw new IllegalStateException("Unterminated string");
        }

        String value = input.substring(start, position);
        position++; //skips second '

        return new Token(value, TokenType.STRING);
    }

    private Token readSymbol(){
        char current = input.charAt(position++);
        return switch(current){
            case '(' -> new Token("(", TokenType.LEFT_PAREN);
            case ')' -> new Token(")", TokenType.RIGHT_PAREN);
            case ',' -> new Token(",", TokenType.COMMA);
            case ';' -> new Token(";", TokenType.SEMICOLON);
            case '*' -> new Token("*", TokenType.STAR);
            case '='  -> new Token("=", TokenType.EQUALS);
            case '<' -> new Token("<", TokenType.LESS_THAN);
            case '>' -> new Token(">", TokenType.GREATER_THAN);
            default -> throw new IllegalArgumentException("Unrecognized symbol" + current);
        };
    }
}