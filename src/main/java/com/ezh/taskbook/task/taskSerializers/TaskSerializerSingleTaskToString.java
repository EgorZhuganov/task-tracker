package com.ezh.taskbook.task.taskSerializers;

import com.ezh.taskbook.task.TypeTask;
import com.ezh.taskbook.task.*;

import java.time.Duration;
import java.time.LocalDateTime;

public class TaskSerializerSingleTaskToString extends TaskSerializerToString<AbstractTask> {

    @Override
    public String taskAsString(AbstractTask task) {
        var sb = new StringBuilder(super.taskAsString(task)).
                append(task.getStartTime()).append(CSV_SEPARATOR).
                append(task.getDuration()).append(CSV_SEPARATOR);
        return sb.toString();
    }

    @Override
    public SingleTask taskFromString(String value) {
        SingleTask singleTask = (SingleTask) super.taskFromString(value);
        String[] fields = value.split(CSV_SEPARATOR);
        singleTask.setName(fields[2]);
        singleTask.setStatus(StatusTask.valueOf(fields[3]));
        singleTask.setDescription(fields[4]);
        singleTask.setStartTime(LocalDateTime.parse(fields[5]));
        singleTask.setDuration(Duration.parse(fields[6]));
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