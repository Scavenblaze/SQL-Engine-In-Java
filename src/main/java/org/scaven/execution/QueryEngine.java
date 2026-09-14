package org.scaven.execution;

import org.scaven.statements.*;
import org.scaven.storage.StorageEngine;

public class QueryEngine {
    private final StorageEngine storageEngine;

    public QueryEngine(StorageEngine storageEngine) {
        this.storageEngine = storageEngine;
    }

    public String execute(Statement statement){
        Executor executor = switch (statement){
            case CreateStatement s -> new CreateExecutor(storageEngine, s);
            case InsertStatement s -> new InsertExecutor(storageEngine, s);
            case SelectStatement s -> new SelectExecutor(storageEngine, s);
            case DeleteStatement s  -> new DeleteExecutor(storageEngine, s);
            default -> throw new IllegalStateException("Unexpected value: " + statement);
        };
        return executor.execute();
    }

}
