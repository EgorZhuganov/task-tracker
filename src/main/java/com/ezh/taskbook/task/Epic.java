package main.java.com.ezh.taskbook.task;

import java.util.ArrayList;
import java.util.List;

public class Epic extends AbstractTask {

    private final List<Subtask> subtaskList = new ArrayList<>();

    public List<Subtask> getSubtaskList() {
        return subtaskList;
    }
}
