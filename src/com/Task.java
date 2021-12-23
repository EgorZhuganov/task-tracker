package com;

import java.util.UUID;

public class Task {
    private String name;
    private String description;
    private final UUID uuid;
    private StatusTask status;

    public Task() {
        this.status = StatusTask.NEW;
        this.uuid = UUID.randomUUID();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public UUID getUuid() { return uuid; }

    public StatusTask getStatus() { return status; }

    public void setStatus(StatusTask status) { this.status = status; }

}
