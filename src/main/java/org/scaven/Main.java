package org.scaven;



import org.scaven.sql.Token;
import org.scaven.sql.Tokenizer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;


public class Main {
    static void main() {

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        Tokenizer tokenizer = new Tokenizer("SELECT name, age FROM students WHERE age > 18;");
        List<Token> thing = tokenizer.tokenize();
        System.out.println(thing.toString());

//        try {
//            System.out.println("Enter: ");
//            String[] userQuery = br.readLine().toLowerCase().split(" ");
//            System.out.println("Thing is " + userQuery);
//
//
//
//        }catch (IOException e){
//            throw new RuntimeException(e);
//        }





//TODO: TEST: user input query -> output using the new token types


    }
}