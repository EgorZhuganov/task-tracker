package com.ezh.taskbook.task;

public class Subtask extends AbstractTask {

    public Subtask() {
        setStatus(StatusTask.NEW);
        setName(getName());
        setDescription(getDescription());
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", status=" + getStatus() +
                ", uuid=" + getUuid() +
                '}';
    }
}
