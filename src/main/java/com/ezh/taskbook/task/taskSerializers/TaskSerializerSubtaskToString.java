package com.ezh.taskbook.task.taskSerializers;

import com.ezh.taskbook.task.*;

public class TaskSerializerSubtaskToString extends TaskSerializerToString<AbstractTask> {

    @Override
    public String taskAsString(AbstractTask task) {
        var sb = new StringBuilder(super.taskAsString(task))
                .append(((Subtask)task).getEpic().getUuid());
        return sb.toString();
    }

    @Override
    public Subtask taskFromString(String value) {
        Subtask subtask = (Subtask) super.taskFromString(value);
        String[] fields = value.split(CSV_SEPARATOR);
        subtask.setName(fields[2]);
        subtask.setStatus(StatusTask.valueOf(fields[3]));
        subtask.setDescription(fields[4]);
        subtask.setEpic(new Epic(fields[5]));
        return subtask;
    }

    @Override
    protected Subtask initiateNewTask(String id) { return new Subtask(id); }

    @Override
    protected String getType() {
        return TypeTask.SUBTASK.name();
    }
}