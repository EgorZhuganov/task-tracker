package com.ezh.taskbook.task;

import com.ezh.taskbook.manager.InMemoryTasksManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class EpicTest {

    InMemoryTasksManager manager = new InMemoryTasksManager();
    Epic epic1 = new Epic();
    Subtask subtask1 = new Subtask(epic1);
    Subtask subtask2 = new Subtask(epic1);
    Subtask subtask3 = new Subtask(epic1);

    @Test
    public void test1_checkStatusEpicWithoutAnySubtaskShouldStatusNew() {
        manager.addEpic(epic1);
        Assertions.assertEquals(StatusTask.NEW, epic1.getStatus());
    }

    @Test
    public void test2_checkStatusEpicWithOneSubtaskWhichHasStatusNewShouldStatusNew() {
        manager.addEpicWithSubtask(epic1, subtask1);
        Assertions.assertEquals(StatusTask.NEW, epic1.getStatus());
    }

    @Test
    public void test3_checkStatusEpicWithOneSubtaskWhichHasStatusDoneShouldStatusDone() {
        subtask1.setStatus(StatusTask.DONE);
        manager.addEpicWithSubtask(epic1, subtask1);
        Assertions.assertEquals(StatusTask.DONE, epic1.getStatus());
    }

    @Test
    public void test4_checkStatusEpicWithTwoSubtasksWhichHaveStatusDoneAndNewShouldStatusInProgress() {
        subtask1.setStatus(StatusTask.DONE);
        subtask2.setStatus(StatusTask.NEW);
        manager.addEpicWithSubtask(epic1, subtask1, subtask2);
        Assertions.assertEquals(StatusTask.IN_PROGRESS, epic1.getStatus());
    }

    @Test
    public void test5_checkStatusEpicWithOneSubtaskWhichHasStatusInProgressAndNewShouldStatusInProgress() {
        subtask1.setStatus(StatusTask.IN_PROGRESS);
        manager.addEpicWithSubtask(epic1, subtask1);
        Assertions.assertEquals(StatusTask.IN_PROGRESS, epic1.getStatus());
    }

    @Test
    public void test6_checkStatusEpicIfStatusSubtaskWillChangeFromNewToDoneShouldStatusDone() {
        manager.addEpicWithSubtask(epic1, subtask1);
        subtask2.setStatus(StatusTask.DONE);
        manager.changeSubtaskByUuid(subtask1.getUuid(), subtask2);
        Assertions.assertEquals(StatusTask.DONE, epic1.getStatus());
    }

    @Test
    public void test7_checkStatusEpicIfStatusSubtaskWillChangeFromNewToInProgressShouldStatusInProgress() {
        manager.addEpicWithSubtask(epic1, subtask1);
        subtask2.setStatus(StatusTask.IN_PROGRESS);
        manager.changeSubtaskByUuid(subtask1.getUuid(), subtask2);
        Assertions.assertEquals(StatusTask.DONE, epic1.getStatus());
    }

    @Test
    public void test8_checkStatusEpicIfItHasSubtaskWithStatusDoneAndWillAddSubtaskWithStatusNewShouldStatusInProgress() {
        subtask1.setStatus(StatusTask.DONE);
        manager.addEpicWithSubtask(epic1, subtask1);
        subtask3.setStatus(StatusTask.NEW);
        manager.addSubtaskInAddedEpic(epic1, subtask3);
        Assertions.assertEquals(StatusTask.IN_PROGRESS, epic1.getStatus());
    }

}