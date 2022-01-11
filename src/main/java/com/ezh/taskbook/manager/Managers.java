package main.java.com.ezh.taskbook.manager;

public class Managers  {

    InMemoryTasksManager getDafault() {
        return new InMemoryTasksManager();
    }
}
