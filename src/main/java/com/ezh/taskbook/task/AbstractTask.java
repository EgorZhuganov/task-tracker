package com.ezh.taskbook.task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

public abstract class AbstractTask {

    private String name;
    private String description;
    private final UUID uuid;

    public AbstractTask(String id){ this.uuid = UUID.fromString(id); }

    public AbstractTask()  { this.uuid = UUID.randomUUID(); }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }

    public UUID getUuid() { return uuid; }

    public abstract StatusTask getStatus();

    public abstract TypeTask getType();

    public abstract Duration getDuration();

    public abstract LocalDateTime getStartTime();

    public abstract LocalDateTime getEndTime();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AbstractTask that = (AbstractTask) o;

        return uuid != null ? uuid.equals(that.uuid) : that.uuid == null;
    }

    @Override
    public int hashCode() {
        return uuid != null ? uuid.hashCode() : 0;
    }
}
