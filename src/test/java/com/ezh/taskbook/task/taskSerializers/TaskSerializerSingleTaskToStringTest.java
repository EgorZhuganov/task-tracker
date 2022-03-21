package com.ezh.taskbook.task.taskSerializers;

import com.ezh.taskbook.task.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

class TaskSerializerSingleTaskToStringTest {

    @Test
    void taskAsString() {

        TaskSerializerToString<AbstractTask> serializer = new TaskSerializerSingleTaskToString();

        SingleTask singleTask = new SingleTask();
        singleTask.setName("I'm Single task");
        singleTask.setDescription("descriptions of singleTask");
        singleTask.setStartTime(LocalDateTime.now());
        singleTask.setDuration(Duration.ofDays(10));

        String value = serializer.taskAsString(singleTask);
        String[] fields = value.split(";");

        Assertions.assertEquals(7, fields.length);
        Assertions.assertEquals(singleTask.getUuid().toString(), fields[0]);
        Assertions.assertEquals(singleTask.getType().toString(), fields[1]);
        Assertions.assertEquals(singleTask.getName(), fields[2]);
        Assertions.assertEquals(singleTask.getStatus().toString(), fields[3]);
        Assertions.assertEquals(singleTask.getDescription(), fields[4]);
        Assertions.assertEquals(singleTask.getStartTime().toString(), fields[5]);
        Assertions.assertEquals(singleTask.getDuration().toString(), fields[6]);
    }

    @Test
    void taskFromString() {

        TaskSerializerToString<AbstractTask> serializer = new TaskSerializerSingleTaskToString();

        SingleTask singleTask = new SingleTask();
        singleTask.setName("I'm Single task");
        singleTask.setDescription("descriptions of singleTask");
        singleTask.setStatus(StatusTask.IN_PROGRESS);
        singleTask.setStartTime(LocalDateTime.now());
        singleTask.setDuration(Duration.ofDays(10));

        String value = serializer.taskAsString(singleTask);
        SingleTask singleTaskSerialize = (SingleTask) serializer.taskFromString(value);

        Assertions.assertEquals(singleTask.getUuid(), singleTaskSerialize.getUuid());
        Assertions.assertEquals(singleTask.getName(), singleTaskSerialize.getName());
        Assertions.assertEquals(singleTask.getType(), singleTaskSerialize.getType());
        Assertions.assertEquals(singleTask.getStatus(), singleTaskSerialize.getStatus());
        Assertions.assertEquals(singleTask.getDescription(), singleTaskSerialize.getDescription());
        Assertions.assertEquals(singleTask.getStartTime(), singleTaskSerialize.getStartTime());
        Assertions.assertEquals(singleTask.getDuration(), singleTaskSerialize.getDuration());
    }

}