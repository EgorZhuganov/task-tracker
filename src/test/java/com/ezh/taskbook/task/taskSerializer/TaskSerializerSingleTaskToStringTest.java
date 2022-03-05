package com.ezh.taskbook.task.taskSerializer;

import com.ezh.taskbook.task.*;
import com.ezh.taskbook.task.taskSerializers.TaskSerializerSingleTaskToString;
import com.ezh.taskbook.task.taskSerializers.TaskSerializerToString;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class TaskSerializerSingleTaskToStringTest {

    @Test
    void taskAsString() {

        TaskSerializerToString<AbstractTask> serializer = new TaskSerializerSingleTaskToString();

        SingleTask singleTask = new SingleTask();
        singleTask.setName("I'm Single task");
        singleTask.setDescription("descriptions of singleTask");

        String value = serializer.taskAsString(singleTask);
        String[] fields = value.split(";");

        Assertions.assertEquals(5, fields.length);
        Assertions.assertEquals(singleTask.getUuid().toString(), fields[0]);
        Assertions.assertEquals(singleTask.getType().toString(), fields[1]);
        Assertions.assertEquals(singleTask.getName(), fields[2]);
        Assertions.assertEquals(singleTask.getStatus().toString(), fields[3]);
        Assertions.assertEquals(singleTask.getDescription(), fields[4]);
    }

    @Test
    void taskFromString() {

        TaskSerializerToString<AbstractTask> serializer = new TaskSerializerSingleTaskToString();

        SingleTask singleTask = new SingleTask();
        singleTask.setName("I'm Single task");
        singleTask.setDescription("descriptions of singleTask");
        singleTask.setStatus(StatusTask.IN_PROGRESS);

        String value = serializer.taskAsString(singleTask);
        SingleTask singleTaskSerialize = (SingleTask) serializer.taskFromString(value);

        Assertions.assertEquals(singleTask.getUuid(), singleTaskSerialize.getUuid());
        Assertions.assertEquals(singleTask.getName(), singleTaskSerialize.getName());
        Assertions.assertEquals(singleTask.getType(), singleTaskSerialize.getType());
        Assertions.assertEquals(singleTask.getStatus(), singleTaskSerialize.getStatus());
        Assertions.assertEquals(singleTask.getDescription(), singleTaskSerialize.getDescription());

    }

}