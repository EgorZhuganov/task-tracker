package com;

public class Subtask extends AbstractTask {

    Epic epic;
    @Override
    public String toString() {
        return "Subtask{" +
                "Epic='" + epic.getName() +
                "name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", status=" + getStatus() +
                ", uuid=" + getUuid() +
                "}";
    }

}
