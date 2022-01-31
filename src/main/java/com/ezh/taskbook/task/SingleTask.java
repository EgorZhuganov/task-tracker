package com.ezh.taskbook.task;

public class SingleTask extends AbstractTask {

    private StatusTask status;

    public SingleTask() {
        setStatus(StatusTask.NEW);
        setName(getName());
        setDescription(getDescription());
    }

    @Override
    public StatusTask getStatus() {
        return status;
    }

    public void setStatus(StatusTask status) {
        this.status = status;
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
