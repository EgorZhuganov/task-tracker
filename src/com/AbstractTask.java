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

    public String getName() { return name; }

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
