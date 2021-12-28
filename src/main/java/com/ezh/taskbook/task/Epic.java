package main.java.com.ezh.taskbook.task;

import java.util.ArrayList;
import java.util.List;

public class Epic extends AbstractTask {

    private final List<Subtask> subtaskList = new ArrayList<>();

    public List<Subtask> getSubtaskList() {
        return subtaskList;
    }

    @Override
    public StatusTask getStatus() {
        int counterNew = 0;
        int counterDone = 0;
        for (Subtask currentSubtask : subtaskList) {
            if (currentSubtask.getStatus() == StatusTask.NEW) {
                counterNew++;
            } else if (currentSubtask.getStatus() == StatusTask.DONE) {
                counterDone++;
            }
        }
        StatusTask status;
        if (counterNew == subtaskList.size() || subtaskList.isEmpty()) {
            status = StatusTask.NEW;
        } else if (counterDone == subtaskList.size()) {
            status = StatusTask.DONE;
        } else {
            status = StatusTask.IN_PROGRESS;
        }
        return status;
    }
}
