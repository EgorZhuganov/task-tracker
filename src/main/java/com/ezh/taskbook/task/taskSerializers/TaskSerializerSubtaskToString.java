package com.ezh.taskbook.task.taskSerializers;

import com.ezh.taskbook.task.TypeTask;
import com.ezh.taskbook.task.*;

import java.time.Duration;
import java.time.LocalDateTime;

public class TaskSerializerSubtaskToString extends TaskSerializerToString<AbstractTask> {

    @Override
    public String taskAsString(AbstractTask task) {
        var sb = new StringBuilder(super.taskAsString(task));
        sb = task.getStartTime() != null ? sb.append(task.getStartTime()).append(CSV_SEPARATOR) : sb.append(CSV_SEPARATOR);
        sb = task.getDuration() != null ? sb.append(task.getDuration()).append(CSV_SEPARATOR) : sb.append(CSV_SEPARATOR);
        sb.append(((Subtask)task).getEpic().getUuid());
        return sb.toString();
    }

    @Override
    public Subtask taskFromString(String value) {
        Subtask subtask = (Subtask) super.taskFromString(value);
        String[] fields = value.split(CSV_SEPARATOR,-1);
        subtask.setName(fields[2]);
        subtask.setStatus(StatusTask.valueOf(fields[3]));
        subtask.setDescription(fields[4]);
        if (!fields[5].isEmpty())
            subtask.setStartTime(LocalDateTime.parse(fields[5]));
        if (!fields[6].isEmpty())
            subtask.setDuration(Duration.parse(fields[6]));
        subtask.setEpic(new Epic(fields[7]));

        return subtask;
    }

    @Override
    protected Subtask initiateNewTask(String id) { return new Subtask(id); }

    @Override
    protected String getType() {
        return TypeTask.SUBTASK.name();
    }
}