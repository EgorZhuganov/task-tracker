package com.ezh.taskbook.task.taskSerializer;

import com.ezh.taskbook.task.AbstractTask;
import com.ezh.taskbook.task.Epic;
import com.ezh.taskbook.task.Subtask;
import com.ezh.taskbook.task.taskSerializers.TaskSerializerSubtaskToString;
import com.ezh.taskbook.task.taskSerializers.TaskSerializerToString;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class TaskSerializerSubtaskToStringTest {

    @Test
    void taskAsString() {
        TaskSerializerToString<AbstractTask> serializer = new TaskSerializerSubtaskToString();

        Epic epic = new Epic();
        Subtask subtask = new Subtask(epic);

        epic.setName("I'm Epic");
        epic.setDescription("descriptions of Epic");
        subtask.setName("I'm Subtask");
        subtask.setDescription("descriptions of Subtask");

        String value = serializer.taskAsString(subtask);
        String[] fields = value.split(";");

        Assertions.assertEquals(6, fields.length);
        Assertions.assertEquals(subtask.getUuid().toString(), fields[0]);
        Assertions.assertEquals(subtask.getType().toString(), fields[1]);
        Assertions.assertEquals(subtask.getName(), fields[2]);
        Assertions.assertEquals(subtask.getStatus().toString(), fields[3]);
        Assertions.assertEquals(subtask.getDescription(), fields[4]);
        Assertions.assertEquals(subtask.getEpic().getUuid().toString(), fields[5]);
    }

    @Test
    void taskFromString() {
        TaskSerializerToString<AbstractTask> serializer = new TaskSerializerSubtaskToString();

        Epic epic = new Epic();
        Subtask subtask = new Subtask(epic);

        epic.setName("I'm Epic");
        epic.setDescription("descriptions of Epic");
        subtask.setName("I'm Subtask");
        subtask.setDescription("descriptions of Subtask");

        String value = serializer.taskAsString(subtask);
        Subtask subtaskSerialize = (Subtask) serializer.taskFromString(value);

        Assertions.assertEquals(subtask.getUuid(), subtaskSerialize.getUuid());
        Assertions.assertEquals(subtask.getName(), subtaskSerialize.getName());
        Assertions.assertEquals(subtask.getType(), subtaskSerialize.getType());
        Assertions.assertEquals(subtask.getStatus(), subtaskSerialize.getStatus());
        Assertions.assertEquals(subtask.getDescription(), subtaskSerialize.getDescription());
        Assertions.assertEquals(subtask.getEpic().getUuid(), subtaskSerialize.getEpic().getUuid());
    }

}