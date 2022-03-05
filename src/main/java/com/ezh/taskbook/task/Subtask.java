package com.ezh.taskbook.task;

public class Subtask extends AbstractTask {

    private StatusTask status;
    private Epic epic;

    public Subtask (Epic epic) {
        super();
        setStatus(StatusTask.NEW);
        this.epic = epic;
    }

    public Subtask (String id) { super(id); }

    @Override
    public TypeTask getType() { return TypeTask.SUBTASK; }

    @Override
    public StatusTask getStatus() { return status; }

    public void setStatus(StatusTask status) { this.status = status; }

    public Epic getEpic() { return epic; }

    public void setEpic(Epic epic) { this.epic = epic; }

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
