package com.ezh.taskbook.task.taskSerializers;

import com.ezh.taskbook.task.AbstractTask;
import com.ezh.taskbook.task.Epic;
import com.ezh.taskbook.task.TypeTask;

public class TaskSerializerEpicToString extends TaskSerializerToString<AbstractTask> {

    @Override
    public Epic taskFromString(String value) {
        Epic epic = (Epic) super.taskFromString(value);
        String[] fields = value.split(CSV_SEPARATOR);
        epic.setName(fields[2]);
        epic.setDescription(fields[4]);
        return epic;
    }

    @Override
    public Epic initiateNewTask(String id) { return new Epic(id); }

    @Override
    public String getType() {
        return TypeTask.EPIC.name();
    }
}
