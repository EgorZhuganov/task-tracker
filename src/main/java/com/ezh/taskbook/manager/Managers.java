package main.java.com.ezh.taskbook.manager;

public class Managers {

    public TaskManager getDefault() {
        return new InMemoryTasksManager();
    }
}
