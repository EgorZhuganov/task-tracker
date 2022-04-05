package com.ezh.taskbook.task.taskSerializers;

import com.ezh.taskbook.exception.TasksIntersectionException;
import com.ezh.taskbook.manager.InMemoryTasksManager;
import com.ezh.taskbook.task.Epic;
import com.ezh.taskbook.task.SingleTask;
import com.ezh.taskbook.task.StatusTask;
import com.ezh.taskbook.task.Subtask;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

class TaskSerializerToJsonTest {

    @Test
    void taskToJsonForSingleTask() {
        SingleTask singleTask1 = new SingleTask();
        singleTask1.setName("name");
        singleTask1.setDescription("descr");
        singleTask1.setStatus(StatusTask.DONE);
        singleTask1.setStartTimeAndDuration(LocalDateTime.of(2022,2,12,20,10), Duration.ofDays(10));

        System.out.println(TaskSerializerToJson.taskToJson(singleTask1));
    }

    @Test
    void taskToJsonForEpic() throws TasksIntersectionException {
        Epic epic1 = new Epic();
        epic1.setName("name");
        epic1.setDescription("descr");
        Subtask subtask1 = new Subtask(epic1);
        Subtask subtask2 = new Subtask(epic1);
        subtask1.setName("Subtask1 name");
        subtask2.setName("Subtask2 name");
        subtask1.setStatus(StatusTask.DONE);
        subtask1.setStartTimeAndDuration(LocalDateTime.of(2022,2,12,20,10), Duration.ofDays(10));

        InMemoryTasksManager manager = new InMemoryTasksManager();
        manager.addEpicWithSubtask(epic1, subtask1, subtask2);

        System.out.println(TaskSerializerToJson.taskToJson(epic1));
    }

    @Test
    void taskToJsonForSubtask() {
        Epic epic1 = new Epic();
        Subtask subtask1 = new Subtask(epic1);
        subtask1.setName("Subtask1 name");
        subtask1.setStatus(StatusTask.DONE);
        subtask1.setStartTimeAndDuration(LocalDateTime.of(2022,2,12,20,10), Duration.ofDays(10));
        subtask1.setDescription("Description Subtask1");

        System.out.println(TaskSerializerToJson.taskToJson(subtask1));
    }

    @Test
    void singleTaskFromJson() {
        SingleTask singleTask1 = new SingleTask();
        singleTask1.setName("name");
        singleTask1.setDescription("descr");
        singleTask1.setStatus(StatusTask.DONE);
        singleTask1.setStartTimeAndDuration(LocalDateTime.of(2022,2,12,20,10), Duration.ofDays(10));

        String singleTaskAsJson = TaskSerializerToJson.taskToJson(singleTask1);
        SingleTask singleTaskAfterSerialize = TaskSerializerToJson.singleTaskFromJson(singleTaskAsJson);

        Assertions.assertEquals(singleTask1.getName(), singleTaskAfterSerialize.getName());
        Assertions.assertEquals(singleTask1.getDescription(), singleTaskAfterSerialize.getDescription());
        Assertions.assertEquals(singleTask1.getStatus(), singleTaskAfterSerialize.getStatus());
        Assertions.assertEquals(singleTask1.getStartTime(), singleTaskAfterSerialize.getStartTime());
        Assertions.assertEquals(singleTask1.getDuration(), singleTaskAfterSerialize.getDuration());
        Assertions.assertEquals(singleTask1.getUuid(), singleTaskAfterSerialize.getUuid());
        Assertions.assertEquals(singleTask1.getType(), singleTaskAfterSerialize.getType());
        Assertions.assertEquals(singleTask1.getEndTime(), singleTaskAfterSerialize.getEndTime());
    }

//    @Test
//    void epicFromJson() throws TasksIntersectionException {

//    }
//
//    @Test
//    void subtaskFromJson() {

//    }
}