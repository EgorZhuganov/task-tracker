package main.java.com.ezh.taskbook.task;

public class Subtask extends AbstractTask {

    public Subtask() {
        setStatus(StatusTask.NEW);
        setName(getName());
        setDescription(getDescription());
    }
}
