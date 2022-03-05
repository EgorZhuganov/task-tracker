package com.ezh.taskbook.task.taskSerializer;

import com.ezh.taskbook.task.AbstractTask;
import com.ezh.taskbook.task.Epic;
import com.ezh.taskbook.task.Subtask;
import com.ezh.taskbook.task.taskSerializers.TaskSerializerEpicToString;
import com.ezh.taskbook.task.taskSerializers.TaskSerializerToString;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class TaskSerializerEpicToStringTest {

    @Test
    void taskAsString() {
        TaskSerializerToString<AbstractTask> serializer = new TaskSerializerEpicToString();

        Epic epic = new Epic();

        epic.setName("I'm Epic");
        epic.setDescription("descriptions of Epic");

        String value = serializer.taskAsString(epic);
        String[] fields = value.split(";");

        Assertions.assertEquals(5, fields.length);
        Assertions.assertEquals(epic.getUuid().toString(), fields[0]);
        Assertions.assertEquals(epic.getType().toString(), fields[1]);
        Assertions.assertEquals(epic.getName(), fields[2]);
        Assertions.assertEquals(epic.getStatus().toString(), fields[3]);
        Assertions.assertEquals(epic.getDescription(), fields[4]);
    }

    @Test
    void taskFromString() {
        TaskSerializerToString<AbstractTask>  serializer = new TaskSerializerEpicToString();

        Epic epic = new Epic();

        epic.setName("I'm Epic");
        epic.setDescription("descriptions of Epic");

        String value = serializer.taskAsString(epic);
        Epic epicSerialize = (Epic) serializer.taskFromString(value);

        Assertions.assertEquals(epic.getUuid(), epicSerialize.getUuid());
        Assertions.assertEquals(epic.getName(), epicSerialize.getName());
        Assertions.assertEquals(epic.getType(), epicSerialize.getType());
        Assertions.assertEquals(epic.getStatus(), epicSerialize.getStatus());
        Assertions.assertEquals(epic.getDescription(), epicSerialize.getDescription());
    }

}