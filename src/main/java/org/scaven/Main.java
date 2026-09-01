package org.scaven;



import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class Main {
    static void main() {

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));


        //temp
        System.out.println("Enter: ");
        try {

            String[] userQuery = br.readLine().toLowerCase().split(" ");
            System.out.println("Thing is " + userQuery);

            for (String query : userQuery) {
                switch (query) {
                    case "select":
                        System.out.println("select selected");
                        break;
                    case "insert":
                        System.out.println("insert selected");
                        break;
                    case "update":
                        System.out.println("update selected");
                        break;
                    case "delete":
                        System.out.println("delete selected");
                        break;
                    default:
                        break;
                }

            }


        }catch (IOException e){
            throw new RuntimeException(e);
        }





//TODO: TEST: user input query -> output using the new token types


    }
}