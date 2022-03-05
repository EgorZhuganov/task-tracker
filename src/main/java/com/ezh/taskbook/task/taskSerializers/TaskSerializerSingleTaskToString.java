package com.ezh.taskbook.task.taskSerializers;

import com.ezh.taskbook.task.*;

public class TaskSerializerSingleTaskToString extends TaskSerializerToString<AbstractTask> {

    @Override
    public SingleTask taskFromString(String value) {
        SingleTask singleTask = (SingleTask) super.taskFromString(value);
        String[] fields = value.split(CSV_SEPARATOR);
        singleTask.setDescription(fields[4]);
        singleTask.setName(fields[2]);
        singleTask.setStatus(StatusTask.valueOf(fields[3]));
        return singleTask;
    }

    @Override
    protected SingleTask initiateNewTask(String id) {
        return new SingleTask(id);
    }

    @Override
    protected String getType() {
        return TypeTask.SINGLE_TASK.name();
    }
}