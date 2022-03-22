package com.ezh.taskbook.task.taskSerializers;

import com.ezh.taskbook.task.*;

public abstract class TaskSerializerToString<T extends AbstractTask> {

    protected static final String CSV_SEPARATOR = ";";

    public String taskAsString(T task) {
        StringBuilder sb = new StringBuilder().
                append(task.getUuid()).append(CSV_SEPARATOR).
                append(getType()).append(CSV_SEPARATOR);
        sb = task.getName() != null ? sb.append(task.getName()).append(CSV_SEPARATOR) : sb.append(CSV_SEPARATOR);
        sb.append(task.getStatus()).append(CSV_SEPARATOR);
        sb = task.getDescription() != null ? sb.append(task.getDescription()).append(CSV_SEPARATOR) : sb.append(CSV_SEPARATOR);
        return sb.toString();
    }

    public T taskFromString(String value) {
        String[] stringValues = value.split(CSV_SEPARATOR);
        T task = initiateNewTask(stringValues[0]);
        return task;
    }

    abstract protected T initiateNewTask(String id);

    abstract protected String getType();
}