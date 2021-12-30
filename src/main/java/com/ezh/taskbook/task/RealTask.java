package main.java.com.ezh.taskbook.task;

public class RealTask extends AbstractTask {

    public RealTask() {
        setStatus(StatusTask.NEW);
        setName(getName());
        setDescription(getDescription());
    }
}
