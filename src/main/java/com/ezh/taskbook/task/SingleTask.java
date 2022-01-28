package com.ezh.taskbook.task;

public class SingleTask extends AbstractTask {

    public SingleTask() {
        setStatus(StatusTask.NEW);
        setName(getName());
        setDescription(getDescription());
    }

    @Override
    public String toString() {
        return "SingleTask{" +
                "name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", status=" + getStatus() +
                ", uuid=" + getUuid() +
                '}';
    }
}
