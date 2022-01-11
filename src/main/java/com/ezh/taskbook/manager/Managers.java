package main.java.com.ezh.taskbook.manager;

public class Managers  {

    public InMemoryTasksManager getDefault() {
        return new InMemoryTasksManager();
    }
}
