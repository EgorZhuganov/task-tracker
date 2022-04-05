package com.ezh.taskbook.task;

import com.ezh.taskbook.exception.TaskNotFoundException;
import com.ezh.taskbook.exception.TasksIntersectionException;
import com.ezh.taskbook.manager.InMemoryTasksManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

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
    public void test2_checkStatusEpicWithOneSubtaskWhichHasStatusNewShouldStatusNew() throws TasksIntersectionException {
        manager.addEpicWithSubtask(epic1, subtask1);
        Assertions.assertEquals(StatusTask.NEW, epic1.getStatus());
    }

    @Test
    public void test3_checkStatusEpicWithOneSubtaskWhichHasStatusDoneShouldStatusDone() throws TasksIntersectionException {
        subtask1.setStatus(StatusTask.DONE);
        manager.addEpicWithSubtask(epic1, subtask1);
        Assertions.assertEquals(StatusTask.DONE, epic1.getStatus());
    }

    @Test
    public void test4_checkStatusEpicWithTwoSubtasksWhichHaveStatusDoneAndNewShouldStatusInProgress() throws TasksIntersectionException {
        subtask1.setStatus(StatusTask.DONE);
        subtask2.setStatus(StatusTask.NEW);
        manager.addEpicWithSubtask(epic1, subtask1, subtask2);
        Assertions.assertEquals(StatusTask.IN_PROGRESS, epic1.getStatus());
    }

    @Test
    public void test5_checkStatusEpicWithOneSubtaskWhichHasStatusInProgressAndNewShouldStatusInProgress() throws TasksIntersectionException {
        subtask1.setStatus(StatusTask.IN_PROGRESS);
        manager.addEpicWithSubtask(epic1, subtask1);
        Assertions.assertEquals(StatusTask.IN_PROGRESS, epic1.getStatus());
    }

    @Test
    public void test6_checkStatusEpicIfStatusSubtaskWillChangeFromNewToDoneShouldStatusDone() throws TaskNotFoundException, TasksIntersectionException {
        manager.addEpicWithSubtask(epic1, subtask1);
        subtask2.setStatus(StatusTask.DONE);
        manager.changeSubtaskByUuid(subtask1.getUuid(), subtask2);
        Assertions.assertEquals(StatusTask.DONE, epic1.getStatus());
    }

    @Test
    public void test7_checkStatusEpicIfStatusSubtaskWillChangeFromNewToInProgressShouldStatusInProgress() throws TaskNotFoundException, TasksIntersectionException {
        manager.addEpicWithSubtask(epic1, subtask1);
        subtask2.setStatus(StatusTask.IN_PROGRESS);
        manager.changeSubtaskByUuid(subtask1.getUuid(), subtask2);
        Assertions.assertEquals(StatusTask.IN_PROGRESS, epic1.getStatus());
    }

    @Test
    public void test8_checkStatusEpicIfItHasSubtaskWithStatusDoneAndWillAddSubtaskWithStatusNewShouldStatusInProgress() throws TasksIntersectionException, TaskNotFoundException {
        subtask1.setStatus(StatusTask.DONE);
        manager.addEpicWithSubtask(epic1, subtask1);
        subtask3.setStatus(StatusTask.NEW);
        manager.addSubtaskInAddedEpic(subtask3);
        Assertions.assertEquals(StatusTask.IN_PROGRESS, epic1.getStatus());
    }

    @Test /*epic calculate end time if at least one subtask does not have a duration or start time field*/
    public void test9_getEndTimeIfAdd2SubtaskWithDurationAndStartTimeAndOneWithoutTheseFieldsShouldReturnEndTime() throws TasksIntersectionException {
        subtask1.setStartTimeAndDuration(LocalDateTime.of(2022,12,20,15,0), Duration.ofDays(30));
        subtask2.setStartTimeAndDuration(LocalDateTime.of(2022,10,20,15,0), Duration.ofDays(10));

        manager.addEpicWithSubtask(epic1, subtask1, subtask2, subtask3);
        Assertions.assertEquals(subtask1.getEndTime(), epic1.getEndTime());
    }

    @Test /* return the biggest time of execute (duration+start time) */
    public void test10_getEndTimeIfAdd3SubtaskWithDurationAndStartTimeShouldReturnEndTime() throws TasksIntersectionException {
        subtask1.setStartTimeAndDuration(LocalDateTime.of(2022,12,10,23,0),Duration.ofDays(300));
        subtask2.setStartTimeAndDuration(LocalDateTime.of(2019,1,10,23,0),Duration.ofDays(10));
        subtask3.setStartTimeAndDuration(LocalDateTime.of(2020,1,10,23,0), Duration.ofDays(20));

        manager.addEpicWithSubtask(epic1, subtask1, subtask2, subtask3);

        Assertions.assertEquals(LocalDateTime.of(2022,12,10,23,0).
                plus(Duration.ofDays(300)), epic1.getEndTime());
    }

    @Test
    public void test11_getEndTimeIfOneOfSubtaskHasNotGotFieldStartTimeShouldReturnStartTimeAnotherSubtask() throws TasksIntersectionException {
        subtask1.setStartTimeAndDuration(LocalDateTime.of(2022,12,10,23,0), Duration.ofDays(300));
        subtask2.setStartTimeAndDuration(LocalDateTime.of(2022,1,10,23,0), Duration.ofDays(10));

        manager.addEpicWithSubtask(epic1, subtask1, subtask2, subtask3);  //without start time subtask3

        Assertions.assertEquals(subtask2.getStartTime(), epic1.getStartTime());
        Assertions.assertDoesNotThrow(() -> epic1.getEndTime());
    }

    @Test /*if no one Subtask return getEndTime as null*/
    public void test12_getEndTimeIfNoOneOfSubtaskHasNotGotFieldStartTimeShouldReturnNull () throws TasksIntersectionException {
        manager.addEpicWithSubtask(epic1, subtask1, subtask2, subtask3);

        Assertions.assertNull(epic1.getEndTime());
    }

    @Test
    public void test14_getStartTimeIfAdd3SubtaskWithStartTimeFindTaskWithTheSmallestStartTime() throws TasksIntersectionException {
        subtask1.setStartTimeAndDuration(LocalDateTime.of(2021,12,10,23,0), Duration.ofDays(5)); //without start duration
        subtask2.setStartTimeAndDuration(LocalDateTime.of(2020,1,10,23,0), Duration.ofDays(10)); //this is last
        subtask3.setStartTimeAndDuration(LocalDateTime.of(2019,1,10,23,0), Duration.ofDays(20));

        manager.addEpicWithSubtask(epic1, subtask1, subtask2, subtask3);

        Assertions.assertEquals(subtask3.getStartTime(), epic1.getStartTime());
    }

    @Test
    public void test15_getStartTimeIfAddSomeSubtasksAndOneWithoutStartTimeShouldReturnTheSmallestStartTimeNotException() throws TasksIntersectionException {
        subtask2.setStartTimeAndDuration(LocalDateTime.of(2020,1,10,23,0), Duration.ofDays(10)); //this is last
        subtask3.setStartTimeAndDuration(LocalDateTime.of(2019,1,10,23,0), Duration.ofDays(20));

        manager.addEpicWithSubtask(epic1, subtask1, subtask2, subtask3); //subtask2 hasn't got start time

        Assertions.assertEquals(subtask3.getStartTime(), epic1.getStartTime());
    }

    @Test
    public void test16_getDurationIfNoOneOfSubtasksHasNotGotDurationShouldReturnNull() throws TasksIntersectionException {
        manager.addEpicWithSubtask(epic1, subtask1, subtask2, subtask3);

        Assertions.assertNull(epic1.getDuration());
    }

    @Test /*duration count if subtask has start time and end time*/
    public void test17_getDurationIfAdd2SubtasksWithStartTimeAndDurationShouldReturnDurationBetweenTwoSubtasks() throws TasksIntersectionException {
        subtask2.setStartTimeAndDuration(LocalDateTime.of(2020,1,10,23,0), Duration.ofDays(10)); //this is last
        subtask3.setStartTimeAndDuration(LocalDateTime.of(2019,1,10,23,0), Duration.ofDays(20));


        manager.addEpicWithSubtask(epic1, subtask1, subtask2, subtask3);

        Assertions.assertEquals(Duration.between(subtask3.getStartTime(), subtask2.getEndTime()), epic1.getDuration());
    }
}