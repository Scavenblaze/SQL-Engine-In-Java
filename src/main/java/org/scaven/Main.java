package org.scaven;

import org.scaven.execution.QueryEngine;
import org.scaven.sql.SqlParser;
import org.scaven.sql.Token;
import org.scaven.sql.Tokenizer;
import org.scaven.statements.Statement;
import org.scaven.storage.DiskStorage;
import org.scaven.storage.StorageEngine;

import java.nio.file.Path;
import java.util.List;
import java.util.Scanner;


public class Main{
    private static final String STORAGE_DIR = "generated_db";

    public static void main(String[] args){
        StorageEngine storageEngine = new DiskStorage(Path.of(STORAGE_DIR));
        QueryEngine queryEngine = new QueryEngine(storageEngine);
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== Java SQL Engine ===");
        System.out.println("Type SQL queries (CREATE, INSERT, SELECT, DELETE).");
        System.out.println("Type 'exit' or 'quit' to stop.\n");

        while(true){
            System.out.print("sql> ");

            if(!scanner.hasNextLine()){
                break;
            }

            String input = scanner.nextLine().trim();

            if(input.isEmpty()){
                continue;
            }

            if(input.equalsIgnoreCase("exit") || input.equalsIgnoreCase("quit")){
                System.out.println("Goodbye.");
                break;
            }

            try{
                //tokenize
                Tokenizer tokenizer = new Tokenizer(input);
                List<Token> tokens = tokenizer.tokenize();

                //parse
                SqlParser parser = new SqlParser(tokens);
                Statement statement = parser.parse();

                //execute
                String result = queryEngine.execute(statement);
                System.out.println(result);

            }catch(Exception e){
                System.out.println("ERROR: " + e.getMessage());
            }

            System.out.println();
        }

        scanner.close();
    }
}