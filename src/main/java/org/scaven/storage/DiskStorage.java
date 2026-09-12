package org.scaven.storage;

import org.scaven.models.Table;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;


public final class DiskStorage implements StorageEngine {

    private final Path storageDir;


    public DiskStorage(Path storageDir) {
        this.storageDir = storageDir;
        try {
            Files.createDirectories(storageDir);
        } catch (IOException e) {
            throw new RuntimeException("Failed to create storage directory: " + storageDir, e);
        }
    }

    @Override
    public void saveTable(Table table) {
        org.scaven.database.proto.Table protoTable = ProtoConverter.toProtoTable(table);
        byte[] data = protoTable.toByteArray();

        Path filePath = resolveTablePath(table.getTableName());
        try {
            Files.write(filePath, data);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save table " + table.getTableName());
        }
    }

    @Override
    public Table loadTable(String tableName) {
        Path filePath = resolveTablePath(tableName);
        if (!Files.exists(filePath)) {
            throw new IllegalArgumentException("Table '" + tableName + "' does not exist at " + filePath);
        }

        try {
            byte[] data = Files.readAllBytes(filePath);
            org.scaven.database.proto.Table protoTable = org.scaven.database.proto.Table.parseFrom(data);
            return ProtoConverter.fromProtoTable(protoTable);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load table " + tableName);
        }
    }

    @Override
    public boolean tableExists(String tableName) {
        return Files.exists(resolveTablePath(tableName));
    }

    @Override
    public void deleteTable(String tableName) {
        Path filePath = resolveTablePath(tableName);
        if (!Files.exists(filePath)) {
            throw new IllegalArgumentException("Table '" + tableName + "' does not exist at " + filePath);
        }

        try {
            Files.delete(filePath);
        } catch (IOException e) {
            throw new RuntimeException("Failed to delete table " + tableName);
        }
    }


    private Path resolveTablePath(String tableName) {
        return storageDir.resolve(tableName.toLowerCase() + ".db");
    }
}
