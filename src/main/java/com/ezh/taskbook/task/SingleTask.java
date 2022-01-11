package main.java.com.ezh.taskbook.task;

public class SingleTask extends AbstractTask {

    public SingleTask() {
        setStatus(StatusTask.NEW);
        setName(getName());
        setDescription(getDescription());
    }
}
