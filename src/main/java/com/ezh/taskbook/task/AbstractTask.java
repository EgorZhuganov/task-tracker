package com.ezh.taskbook.task;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public abstract class AbstractTask {

    private String name;
    private String description;
    private final UUID uuid;
    private static final List<UUID> uuidStorage = new ArrayList<>();

    public AbstractTask()  {
        this.uuid = UUID.randomUUID();
        if (uuidStorage.contains(uuid)){
            throw new RuntimeException("Duplicate keys, try create new Task");
        } else {
            uuidStorage.add(uuid);
        }
    }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }

    public UUID getUuid() { return uuid; }

    abstract StatusTask getStatus();

}
