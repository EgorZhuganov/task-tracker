package com.ezh.taskbook.task.taskSerializers;

import com.ezh.taskbook.task.AbstractTask;
import com.ezh.taskbook.task.Epic;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class TaskSerializerEpicToStringTest {

    @Test
    void taskAsString() {
        TaskSerializerToString<AbstractTask> serializer = new TaskSerializerEpicToString();

        Epic epic1 = new Epic();

        epic1.setName("I'm Epic");
        epic1.setDescription("descriptions of Epic");

        String value = serializer.taskAsString(epic1);
        String[] fields = value.split(";");

        Assertions.assertEquals(5, fields.length);
        Assertions.assertEquals(epic1.getUuid().toString(), fields[0]);
        Assertions.assertEquals(epic1.getType().toString(), fields[1]);
        Assertions.assertEquals(epic1.getName(), fields[2]);
        Assertions.assertEquals(epic1.getStatus().toString(), fields[3]);
        Assertions.assertEquals(epic1.getDescription(), fields[4]);
    }

    @Test
    void taskFromString() {
        TaskSerializerToString<AbstractTask>  serializer = new TaskSerializerEpicToString();

        Epic epic1 = new Epic();

        epic1.setName("I'm Epic");
        epic1.setDescription("descriptions of Epic");

        String value = serializer.taskAsString(epic1);
        Epic epicSerialize = (Epic) serializer.taskFromString(value);

        Assertions.assertEquals(epic1.getUuid(), epicSerialize.getUuid());
        Assertions.assertEquals(epic1.getName(), epicSerialize.getName());
        Assertions.assertEquals(epic1.getType(), epicSerialize.getType());
    }

}