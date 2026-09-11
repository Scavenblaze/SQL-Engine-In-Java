package org.scaven;



import org.scaven.sql.SqlParser;
import org.scaven.sql.Token;
import org.scaven.sql.Tokenizer;
import org.scaven.statements.Statement;

import java.util.List;


public class Main {
    static void main() {

        //String query = "SELECT age FROM students WHERE age > 10 ORDER BY age";
        //String query = "insert into students values(1, 'DouglasDouglas', 34);";
        String query = "create table students(id int, name string, age int, thing boolean, thing2 double);";
        //String query = "delete from students where age < 18;";

        Tokenizer tokenizer = new Tokenizer(query);
        List<Token> tokens = tokenizer.tokenize();

        System.out.println("TOKENS: ");
        for (Token token : tokens) {
            System.out.println(token);
        }

        SqlParser parser = new SqlParser(tokens);
        Statement statement = parser.parse();

        System.out.println("\nParsed Statements: ");
        System.out.println(statement);





//TODO: Query Execution and Storage


    }
}