package com.ezh.taskbook.task;

public class SingleTask extends AbstractTask {

    private StatusTask status;

    public SingleTask() {
        super();
        setStatus(StatusTask.NEW);
    }

    public SingleTask(String id) {
        super(id);
        setStatus(StatusTask.NEW);
    }

    @Override
    public TypeTask getType() { return TypeTask.SINGLE_TASK; }

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
