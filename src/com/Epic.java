package com;

import java.util.ArrayList;
import java.util.List;

public class Epic extends AbstractTask {

    private final List<Subtask> subtaskList = new ArrayList<>();

    public List<Subtask> getSubtaskList() {
        return subtaskList;
    }

    @Override
    public String toString() {
        return "Epic{" +
                "name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", status=" + getStatus() +
                ", uuid=" + getUuid() +
                "subtaskList=" + subtaskList +
                '}';
    }
}
