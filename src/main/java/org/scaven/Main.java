package org.scaven;

import org.scaven.database.proto.Table;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {
    static void main() {

        Table t = Table.newBuilder().setName("students").build();
        System.out.println(t.getName());


        byte[] data = t.toByteArray();

        System.out.println(data.length);
        System.out.println(data.toString());

        try {
            Files.createDirectories(Paths.get("./generated_db"));
            Files.write(Path.of("./generated_db/students.bin"), data);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }
}