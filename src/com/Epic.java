package com;

import java.util.ArrayList;
import java.util.List;

public class Epic extends AbstractTask {

    private List<Subtask> subtaskList = new ArrayList<>();

    public List<Subtask> getSubtaskList() {
        return subtaskList;
    }

    public void setSubtaskList(List<Subtask> subtaskList) {
        this.subtaskList = subtaskList;
    }
}
