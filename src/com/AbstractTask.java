package com;

import java.util.UUID;

import static com.Manager.epicMap;
import static com.Manager.taskList;

abstract class AbstractTask {

    private String name;
    private String description;
    private StatusTask status;
    private final UUID uuid;

    public AbstractTask()  {
        this.status = StatusTask.NEW;
        this.uuid = UUID.randomUUID();
        if (epicMap.containsKey(uuid)){
            try {
                throw new Exception("Duplicate keys, try create new Task"); //I cannot find DuplicateKeyException
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (!taskList.isEmpty()) {
            for (RealTask task : taskList) {
                if (task.getUuid().equals(uuid)) {
                    try {
                        throw new Exception("Duplicate keys, try create new Task");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } else if (!epicMap.isEmpty()) {
            for (Epic epic : epicMap.values()) {
                for (Subtask subtask : epic.getSubtaskList()) {
                    if (subtask.getUuid().equals(uuid)){
                        try {
                            throw new Exception("Duplicate keys, try create new Task");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AbstractTask that = (AbstractTask) o;

        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (description != null ? !description.equals(that.description) : that.description != null) return false;
        if (status != that.status) return false;
        return uuid != null ? uuid.equals(that.uuid) : that.uuid == null;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (uuid != null ? uuid.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Task{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", uuid=" + uuid +
                '}';
    }
}
