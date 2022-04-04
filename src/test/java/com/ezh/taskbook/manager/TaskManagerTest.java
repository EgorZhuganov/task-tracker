package com.ezh.taskbook.manager;

import com.ezh.taskbook.exception.TaskNotFoundException;
import com.ezh.taskbook.exception.TasksIntersectionException;
import com.ezh.taskbook.task.Epic;
import com.ezh.taskbook.task.SingleTask;
import com.ezh.taskbook.task.StatusTask;
import com.ezh.taskbook.task.Subtask;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

abstract class TaskManagerTest<ManagerType extends TaskManager> {

    ManagerType manager;

    @Test
    void test1_getEpicBySubtaskUuidWithoutAddingTaskToManagerShouldThrowsTaskNotFoundException() {
        Epic epic1 = new Epic();
        Subtask subtask2 = new Subtask(epic1);

        Assertions.assertThrows(TaskNotFoundException.class, () -> manager.getEpicBySubtaskUuid(subtask2.getUuid()));
    }

    @Test
    void test2_getEpicBySubtaskUuidIfDoNotPutSubtaskInParameterOfMethodShouldThrowsTaskNotFoundException() throws TasksIntersectionException {
        Epic epic1 = new Epic();
        SingleTask singleTask1 = new SingleTask();

        manager.addSingleTask(singleTask1);
        manager.addEpic(epic1);

        Assertions.assertThrows(TaskNotFoundException.class, () -> manager.getSubtaskByUuid(singleTask1.getUuid()));
        Assertions.assertThrows(TaskNotFoundException.class, () -> manager.getSubtaskByUuid(epic1.getUuid()));
    }

    @Test
    void test3_getEpicBySubtaskUuidShouldReturnEpic() throws TaskNotFoundException, TasksIntersectionException {
        Epic epic1 = new Epic();
        Subtask subtask2 = new Subtask(epic1);

        manager.addEpicWithSubtask(epic1, subtask2);

        Assertions.assertEquals(epic1, manager.getEpicBySubtaskUuid(subtask2.getUuid()));
    }

    @Test
    void test4_getSubtaskByUuidWithoutAddingTaskToManagerShouldThrowsTaskNotFoundException() {
        Epic epic1 = new Epic();
        Subtask subtask2 = new Subtask(epic1);

        Assertions.assertThrows(TaskNotFoundException.class, () -> manager.getSubtaskByUuid(subtask2.getUuid()));
    }

    @Test
    void test5_getSubtaskByUuidIfDoNotPutSubtaskInParameterOfMethodShouldThrowsTaskNotFoundException() throws TasksIntersectionException {
        Epic epic1 = new Epic();
        SingleTask singleTask1 = new SingleTask();

        manager.addSingleTask(singleTask1);
        manager.addEpic(epic1);

        Assertions.assertThrows(TaskNotFoundException.class, () -> manager.getSubtaskByUuid(singleTask1.getUuid()));
        Assertions.assertThrows(TaskNotFoundException.class, () -> manager.getSubtaskByUuid(epic1.getUuid()));
    }

    @Test
    void test6_getSubtaskByUuidShouldReturnSubtask() throws TaskNotFoundException, TasksIntersectionException {
        Epic epic1 = new Epic();
        Subtask subtask2 = new Subtask(epic1);

        manager.addEpicWithSubtask(epic1, subtask2);

        Assertions.assertEquals(subtask2, manager.getSubtaskByUuid(subtask2.getUuid()));
    }

    @Test
    void test7_getEpicByUuidWithoutAddingTaskToManagerShouldThrowsTaskNotFoundException() {
        Epic epic1 = new Epic();
        Assertions.assertThrows(TaskNotFoundException.class, () -> manager.getEpicByUuid(epic1.getUuid()));
    }

    @Test
    void test8_getEpicByUuidIfDoNotPutSubtaskInParameterOfMethodShouldThrowsTaskNotFoundException() throws TasksIntersectionException {
        Epic epic1 = new Epic();
        Subtask subtask1 = new Subtask(epic1);
        SingleTask singleTask1 = new SingleTask();

        manager.addSingleTask(singleTask1);
        manager.addEpicWithSubtask(epic1, subtask1);

        Assertions.assertThrows(TaskNotFoundException.class, () -> manager.getEpicByUuid(singleTask1.getUuid()));
        Assertions.assertThrows(TaskNotFoundException.class, () -> manager.getEpicByUuid(subtask1.getUuid()));
    }

    @Test
    void test9_getEpicByUuidShouldReturnEpic() throws TaskNotFoundException, TasksIntersectionException {
        Epic epic1 = new Epic();
        Subtask subtask2 = new Subtask(epic1);
        manager.addEpicWithSubtask(epic1, subtask2);
        Assertions.assertEquals(epic1, manager.getEpicByUuid(epic1.getUuid()));
    }

    @Test
    void test10_getSingleTaskByUuidWithoutAddingTaskToManagerShouldThrowsTaskNotFoundException() {
        SingleTask singleTask1 = new SingleTask();
        Assertions.assertThrows(TaskNotFoundException.class, () -> manager.getSingleTaskByUuid(singleTask1.getUuid()));
    }

    @Test
    void test11_getSingleTaskByUuidIfDoNotPutSubtaskInParameterOfMethodShouldThrowsTaskNotFoundException() throws TasksIntersectionException {
        Epic epic1 = new Epic();
        Subtask subtask1 = new Subtask(epic1);

        manager.addEpicWithSubtask(epic1, subtask1);

        Assertions.assertThrows(TaskNotFoundException.class, () -> manager.getSingleTaskByUuid(epic1.getUuid()));
        Assertions.assertThrows(TaskNotFoundException.class, () -> manager.getSingleTaskByUuid(subtask1.getUuid()));
    }

    @Test
    void test12_getSingleTaskByUuidShouldReturnSingleTask() throws TaskNotFoundException, TasksIntersectionException {
        SingleTask singleTask1 = new SingleTask();
        manager.addSingleTask(singleTask1);
        manager.getSingleTaskByUuid(singleTask1.getUuid());
        Assertions.assertEquals(singleTask1, manager.getSingleTaskByUuid(singleTask1.getUuid()));
    }

    @Test
    void test13_getListSubtasksIfDoNotAddSubtasksThroughManagerShouldReturn0() {
        Epic epic1 = new Epic();
        Subtask subtask1 = new Subtask(epic1);
        Subtask subtask2 = new Subtask(epic1);

        Assertions.assertEquals(0, manager.getListSubtasks().size());
    }

    @Test
    void test13_getListSubtasksIfAddEpicWith3SubtasksShouldReturn3() throws TasksIntersectionException {
        Epic epic1 = new Epic();
        Subtask subtask1 = new Subtask(epic1);
        Subtask subtask2 = new Subtask(epic1);
        Subtask subtask3 = new Subtask(epic1);

        manager.addEpicWithSubtask(epic1, subtask1, subtask2, subtask3);
        Assertions.assertEquals(3, manager.getListSubtasks().size());
    }

    @Test
    void test14_getListEpicsIfDoNotAddEpicsThroughManagerShouldReturn0() {
        Epic epic1 = new Epic();
        Epic epic2 = new Epic();

        Assertions.assertEquals(0, manager.getListEpics().size());
    }

    @Test
    void test15_getListEpicsIfAdd2EpicsShouldReturn2() {
        Epic epic1 = new Epic();
        Epic epic2 = new Epic();

        manager.addEpic(epic1);
        manager.addEpic(epic2);
        Assertions.assertEquals(2, manager.getListEpics().size());
    }

    @Test
    void test16_getListSubtasksByEpicIfDoNotAddEpicsThroughManagerShouldThrowTaskNotFoundException() {
        Epic epic1 = new Epic();
        Subtask subtask1 = new Subtask(epic1);
        Subtask subtask2 = new Subtask(epic1);

        Assertions.assertThrows(TaskNotFoundException.class, () -> manager.getListSubtasksByEpicUuid(epic1.getUuid()));
    }

    @Test
    void test17_getListSubtasksByEpicIfAddEpicWith2SubtasksShouldReturn2() throws TaskNotFoundException, TasksIntersectionException {
        Epic epic1 = new Epic();
        Subtask subtask1 = new Subtask(epic1);
        Subtask subtask2 = new Subtask(epic1);

        manager.addEpicWithSubtask(epic1, subtask1, subtask2);

        Assertions.assertEquals(2, manager.getListSubtasksByEpicUuid(epic1.getUuid()).size());
    }

    @Test
    void test18_getListSubtasksByEpicIfAddEpicWithoutAnySubtasksShouldReturn0() throws TaskNotFoundException, TasksIntersectionException {
        Epic epic1 = new Epic();

        manager.addEpicWithSubtask(epic1);

        Assertions.assertEquals(0, manager.getListSubtasksByEpicUuid(epic1.getUuid()).size());
    }

    @Test
    void test19_getListSingleTasksIfDoNotAddSingleTaskThroughManagerShouldReturn0() {
        SingleTask singleTask1 = new SingleTask();
        Assertions.assertEquals(0, manager.getListSingleTasks().size());
    }

    @Test
    void test20_getListSingleTasksIfAdd2SingleTasksShouldReturn2() throws TasksIntersectionException {
        SingleTask singleTask1 = new SingleTask();
        SingleTask singleTask2 = new SingleTask();

        manager.addSingleTask(singleTask1);
        manager.addSingleTask(singleTask2);
        Assertions.assertEquals(2, manager.getListSingleTasks().size());
    }

    @Test
    void test21_addEpicShouldReturnFromManagerTheSameEpic() throws TaskNotFoundException {
        Epic epic1 = new Epic();

        manager.addEpic(epic1);
        Assertions.assertEquals(epic1, manager.getEpicByUuid(epic1.getUuid()));
    }

    @Test
    void test22_addEpicAdd2EpicsShouldReturnFromManager2Epics() {
        Epic epic1 = new Epic();
        Epic epic2 = new Epic();

        manager.addEpic(epic1);
        manager.addEpic(epic2);

        Assertions.assertEquals(2, manager.getListEpics().size());
    }

    @Test /*must not put two Epics which have the same uuid*/
    void test23_addEpicAddOneEpicTwiceShouldThrowsRuntimeException() {
        Epic epic1 = new Epic();
        manager.addEpic(epic1);
        Assertions.assertThrows(RuntimeException.class, () -> manager.addEpic(epic1));
    }

    @Test
    void test24_addSingleTaskShouldReturnFromManagerTheSameSingleTask() throws TaskNotFoundException, TasksIntersectionException {
        SingleTask singleTask1 = new SingleTask();

        manager.addSingleTask(singleTask1);
        Assertions.assertEquals(singleTask1, manager.getSingleTaskByUuid(singleTask1.getUuid()));
    }

    @Test
    void test25_addSingleTaskAdd2SingleTasksShouldReturnFromManager2SingleTask() throws TasksIntersectionException {
        SingleTask singleTask1 = new SingleTask();
        SingleTask singleTask2 = new SingleTask();

        manager.addSingleTask(singleTask1);
        manager.addSingleTask(singleTask2);

        Assertions.assertEquals(2, manager.getListSingleTasks().size());
    }

    @Test /*must not put two SingleTask which have the same uuid*/
    void test26_addSingleTaskAddOneEpicTwiceShouldThrowsRuntimeException() throws TasksIntersectionException {
        SingleTask singleTask1 = new SingleTask();
        manager.addSingleTask(singleTask1);
        Assertions.assertThrows(RuntimeException.class, () -> manager.addSingleTask(singleTask1));
    }

    @Test
    void test27_addEpicWithSubtaskAddOneEpicWithTwoSubtasksShouldReturnFromManagerStorage1EpicAnd2Subtasks() throws TaskNotFoundException, TasksIntersectionException {
        Epic epic1 = new Epic();
        Subtask subtask1 = new Subtask(epic1);
        Subtask subtask2 = new Subtask(epic1);

        manager.addEpicWithSubtask(epic1, subtask1, subtask2);
        Assertions.assertEquals(2, manager.getListSubtasks().size());
        Assertions.assertEquals(1, manager.getListEpics().size());
        Assertions.assertEquals(epic1, manager.getEpicByUuid(epic1.getUuid()));
    }

    @Test
        /* unable to add Epic even if try to add with subtask which  missing in the manager's storage because
         * must not put two Epic which have the same uuid */
    void test28_addEpicWithSubtaskAddEpicWithTwoSubtasksAndThenTryToAddTheSameEpicWithAnotherSubtaskThrowsRuntimeException() throws TasksIntersectionException {
        Epic epic1 = new Epic();
        Subtask subtask1 = new Subtask(epic1);
        Subtask subtask2 = new Subtask(epic1);
        Subtask subtask3 = new Subtask(epic1);

        manager.addEpicWithSubtask(epic1, subtask1, subtask2);

        Assertions.assertThrows(RuntimeException.class, () -> manager.addEpicWithSubtask(epic1, subtask3));
    }

    @Test
    void test29_addEpicWithSubtaskAddOneEpicWithTwoSubtasksShouldReturn2SubtasksFromThisEpic() throws TasksIntersectionException {
        Epic epic1 = new Epic();
        Subtask subtask1 = new Subtask(epic1);
        Subtask subtask2 = new Subtask(epic1);

        manager.addEpicWithSubtask(epic1, subtask1, subtask2);

        Assertions.assertEquals(2, epic1.getSubtaskList().size());
    }

    @Test /*If add subtask1 in epic1 and then use method addEpicWithSubtask(epic2 , subtask1), in subtask1 will has
     another epic, in this case epic2*/
    void test30_addEpicWithSubtaskCheckThatSubtaskHasChangedEpic() throws TasksIntersectionException {
        Epic epic1 = new Epic();
        Epic epic2 = new Epic();
        Subtask subtask1 = new Subtask(epic1); //set epic1
        Subtask subtask2 = new Subtask(epic1); //set epic1

        manager.addEpicWithSubtask(epic2, subtask1, subtask2); //reset to epic2

        Assertions.assertEquals(epic2.getUuid(), subtask1.getEpicId());
        Assertions.assertEquals(epic2.getUuid(), subtask2.getEpicId());
    }

    @Test
    void test31_addSubtaskInAddedEpicAddSubtaskWithTheSameUuidInOneEpicThrowsRuntimeException() throws TasksIntersectionException {
        Epic epic1 = new Epic();
        Subtask subtask1 = new Subtask(epic1);

        manager.addEpic(epic1);
        manager.addSubtaskInAddedEpic(epic1, subtask1);
        Assertions.assertThrows(RuntimeException.class, () -> manager.addSubtaskInAddedEpic(epic1, subtask1));
    }

    @Test
    void test32_addSubtaskInAddedEpicAddTwoSubtasksInOneEpicShouldReturnFromStorageTwoSubtasks() throws TasksIntersectionException {
        Epic epic1 = new Epic();
        Subtask subtask1 = new Subtask(epic1);
        Subtask subtask2 = new Subtask(epic1);

        manager.addEpic(epic1);
        manager.addSubtaskInAddedEpic(epic1, subtask1);
        manager.addSubtaskInAddedEpic(epic1, subtask2);
        Assertions.assertEquals(2, manager.getListSubtasks().size());
    }

    @Test
    void test33_addSubtaskInAddedEpicAddTwoSubtasksInOneEpicShouldReturnFromEpicTwoSubtasks() throws TasksIntersectionException {
        Epic epic1 = new Epic();
        Subtask subtask1 = new Subtask(epic1);
        Subtask subtask2 = new Subtask(epic1);

        manager.addEpic(epic1);
        manager.addSubtaskInAddedEpic(epic1, subtask1);
        manager.addSubtaskInAddedEpic(epic1, subtask2);
        Assertions.assertEquals(2, epic1.getSubtaskList().size());
    }

    @Test
    void test34_changeSingleTaskByUuidStorageIsEmptyThrowsTaskNotFoundException() {
        SingleTask singleTask1 = new SingleTask();
        SingleTask singleTask2 = new SingleTask();

        singleTask2.setName("I'm real task");
        singleTask2.setDescription("About something");
        singleTask2.setStatus(StatusTask.DONE);
        singleTask2.setStartTimeAndDuration(LocalDateTime.now(), Duration.ofDays(5));

        Assertions.assertThrows(TaskNotFoundException.class, () ->
                manager.changeSingleTaskByUuid(singleTask1.getUuid(), singleTask2));
    }


    @Test
    void test35_changeSingleTaskByUuidTryToChangeSingleTaskBySingleTaskWithTheSameUuidThrowsRuntimeException() throws TasksIntersectionException {
        SingleTask singleTask1 = new SingleTask();

        manager.addSingleTask(singleTask1);

        singleTask1.setName("I'm real task");
        singleTask1.setDescription("About something");
        singleTask1.setStatus(StatusTask.DONE);
        singleTask1.setStartTimeAndDuration(LocalDateTime.now(), Duration.ofDays(5));

        Assertions.assertThrows(RuntimeException.class, () ->
                manager.changeSingleTaskByUuid(singleTask1.getUuid(), singleTask1));
    }

    @Test
    void test36_changeSingleTaskByUuidSetSingleTaskValuesFromAnotherSingleTaskShouldTheSameValuesInFirstTask() throws TaskNotFoundException, TasksIntersectionException {
        SingleTask singleTask1 = new SingleTask();
        SingleTask singleTask2 = new SingleTask();

        singleTask2.setName("I'm real task");
        singleTask2.setDescription("About something");
        singleTask2.setStatus(StatusTask.DONE);
        singleTask2.setStartTimeAndDuration(LocalDateTime.of(2022, 12, 12, 23, 59), Duration.ofDays(5));

        manager.addSingleTask(singleTask1);
        manager.changeSingleTaskByUuid(singleTask1.getUuid(), singleTask2);

        Assertions.assertEquals(singleTask2.getName(), manager.getSingleTaskByUuid(singleTask1.getUuid()).getName());
        Assertions.assertEquals(singleTask2.getDescription(),
                manager.getSingleTaskByUuid(singleTask1.getUuid()).getDescription());
        Assertions.assertEquals(singleTask2.getStatus(), manager.getSingleTaskByUuid(singleTask1.getUuid()).getStatus());
        Assertions.assertEquals(singleTask2.getType(), manager.getSingleTaskByUuid(singleTask1.getUuid()).getType());
        Assertions.assertEquals(singleTask2.getStartTime(), manager.getSingleTaskByUuid(singleTask1.getUuid()).getStartTime());
        Assertions.assertEquals(singleTask2.getDuration(), manager.getSingleTaskByUuid(singleTask1.getUuid()).getDuration());
    }

    @Test
    void test37_changeEpicByUuidStorageIsEmptyThrowsTaskNotFoundException() {
        Epic epic1 = new Epic();
        Epic epic2 = new Epic();

        epic2.setName("I'm epic task");
        epic2.setDescription("About something");

        Assertions.assertThrows(TaskNotFoundException.class, () ->
                manager.changeEpicByUuid(epic1.getUuid(), epic2));
    }

    @Test
    void test38_changeEpicByUuidTryToChangeEpicByEpicWithTheSameUuidThrowsRuntimeException() {
        Epic epic1 = new Epic();

        manager.addEpic(epic1);

        epic1.setName("I'm real task");
        epic1.setDescription("About something");

        Assertions.assertThrows(RuntimeException.class, () ->
                manager.changeEpicByUuid(epic1.getUuid(), epic1));
    }

    @Test
    void test39_changeEpicByUuidSetEpicValuesFromAnotherEpicShouldTheSameValuesInFirstTask() throws TaskNotFoundException {
        Epic epic1 = new Epic();
        Epic epic2 = new Epic();

        manager.addEpic(epic1);

        epic2.setName("Find yourself");
        epic2.setDescription("Wake up at 8 am");

        manager.changeEpicByUuid(epic1.getUuid(), epic2);

        Assertions.assertEquals(epic2.getName(), manager.getEpicByUuid(epic1.getUuid()).getName());
        Assertions.assertEquals(epic2.getDescription(),
                manager.getEpicByUuid(epic1.getUuid()).getDescription());
        Assertions.assertEquals(epic2.getStatus(), manager.getEpicByUuid(epic1.getUuid()).getStatus());
        Assertions.assertEquals(epic2.getType(), manager.getEpicByUuid(epic1.getUuid()).getType());
    }

    @Test
    void test40_changeSubtaskByUuidStorageIsEmptyThrowsTaskNotFoundException() {
        Epic epic1 = new Epic();
        Subtask subtask1 = new Subtask(epic1);
        Subtask subtask2 = new Subtask(epic1);

        subtask2.setName("I'm epic task");
        subtask2.setDescription("About something");
        subtask2.setStartTimeAndDuration(LocalDateTime.now(), Duration.ofDays(5));

        Assertions.assertThrows(TaskNotFoundException.class, () ->
                manager.changeSubtaskByUuid(subtask1.getUuid(), subtask2));
    }

    @Test
    void test41_changeSubtaskByUuidTryToChangeSubtaskBySubtaskWithTheSameUuidThrowsRuntimeException() throws TasksIntersectionException {
        Epic epic1 = new Epic();
        Subtask subtask1 = new Subtask(epic1);

        manager.addEpicWithSubtask(epic1, subtask1);

        subtask1.setName("I'm real task");
        subtask1.setDescription("About something");
        subtask1.setStartTimeAndDuration(LocalDateTime.of(2022, 12, 12, 23, 59), Duration.ofDays(5));

        Assertions.assertThrows(RuntimeException.class, () ->
                manager.changeSubtaskByUuid(subtask1.getUuid(), subtask1));
    }

    @Test
    void test42_changeSubtaskByUuidSetSubtaskValuesFromAnotherSubtaskShouldTheSameValuesInFirstTask() throws TaskNotFoundException, TasksIntersectionException {
        Epic epic1 = new Epic();
        Subtask subtask1 = new Subtask(epic1);
        Subtask subtask2 = new Subtask(epic1);

        manager.addEpic(epic1);
        manager.addSubtaskInAddedEpic(epic1, subtask1);
        subtask2.setStatus(StatusTask.IN_PROGRESS);
        subtask2.setName("My name is Subtask");
        subtask2.setDescription("I have to do something important");
        subtask1.setStartTimeAndDuration(LocalDateTime.of(2022, 12, 12, 23, 59), Duration.ofDays(5));
        manager.changeSubtaskByUuid(subtask1.getUuid(), subtask2);

        Assertions.assertEquals(subtask2.getName(), manager.getSubtaskByUuid(subtask1.getUuid()).getName());
        Assertions.assertEquals(subtask2.getDescription(),
                manager.getSubtaskByUuid(subtask1.getUuid()).getDescription());
        Assertions.assertEquals(subtask2.getStatus(), manager.getSubtaskByUuid(subtask1.getUuid()).getStatus());
        Assertions.assertEquals(subtask2.getStartTime(), manager.getSubtaskByUuid(subtask1.getUuid()).getStartTime());
        Assertions.assertEquals(subtask2.getDuration(), manager.getSubtaskByUuid(subtask1.getUuid()).getDuration());
    }

    @Test
    void test43_clearSingleTasksAddTwoSingleTasksShouldReturnFromManagerAfterUseMethod0SingleTasks() throws TasksIntersectionException {
        SingleTask singleTask1 = new SingleTask();
        SingleTask singleTask2 = new SingleTask();

        manager.addSingleTask(singleTask1);
        manager.addSingleTask(singleTask2);
        Assertions.assertEquals(2, manager.getListSingleTasks().size());
        manager.clearSingleTasks();
        Assertions.assertEquals(0, manager.getListSingleTasks().size());
    }

    @Test
    void test44_clearSingleTasksWithEmptyStorageNotThrowsAnyException() {
        SingleTask singleTask1 = new SingleTask();
        SingleTask singleTask2 = new SingleTask();

        Assertions.assertDoesNotThrow(() -> manager.clearSingleTasks());
    }

    @Test
    void test45_clearEpicsAddTwoEpicsShouldReturnFromManagerAfterUseMethod0Epics() {
        Epic epic1 = new Epic();
        Epic epic2 = new Epic();

        manager.addEpic(epic1);
        manager.addEpic(epic2);
        Assertions.assertEquals(2, manager.getListEpics().size());
        manager.clearEpics();
        Assertions.assertEquals(0, manager.getListEpics().size());
    }

    @Test
    void test46_clearEpicsWithEmptyStorageNotThrowsAnyException() {
        Epic epic1 = new Epic();

        Assertions.assertDoesNotThrow(() -> manager.clearEpics());
    }

    @Test
    void test47_clearSubtasksInAllEpicWithEmptyStorageNotThrowsAnyException() {
        Epic epic1 = new Epic();
        Epic epic2 = new Epic();
        Subtask subtask1 = new Subtask(epic1);
        Subtask subtask2 = new Subtask(epic2);

        Assertions.assertDoesNotThrow(() -> manager.clearSubtasksInAllEpic());
    }

    @Test
    void test48_clearSubtasksInAllEpicAdd2EpicAnd4SubtaskShouldReturnFromManager0Subtask() throws TasksIntersectionException {
        Epic epic1 = new Epic();
        Epic epic2 = new Epic();

        Subtask subtask1 = new Subtask(epic1);
        Subtask subtask2 = new Subtask(epic1);
        Subtask subtask3 = new Subtask(epic2);
        Subtask subtask4 = new Subtask(epic2);

        manager.addEpicWithSubtask(epic1, subtask1, subtask2);
        manager.addEpicWithSubtask(epic2, subtask3, subtask4);

        manager.clearSubtasksInAllEpic();
        Assertions.assertEquals(0, manager.getListSubtasks().size());

    }

    @Test
    void test49_clearSubtasksInAllEpicAdd2EpicAnd4SubtaskShouldReturn0SubtaskFromTheseEpics() throws TasksIntersectionException {
        Epic epic1 = new Epic();
        Epic epic2 = new Epic();

        Subtask subtask1 = new Subtask(epic1);
        Subtask subtask2 = new Subtask(epic1);
        Subtask subtask3 = new Subtask(epic2);
        Subtask subtask4 = new Subtask(epic2);

        manager.addEpicWithSubtask(epic1, subtask1, subtask2);
        manager.addEpicWithSubtask(epic2, subtask3, subtask4);

        manager.clearSubtasksInAllEpic();
        Assertions.assertEquals(0, epic1.getSubtaskList().size());
        Assertions.assertEquals(0, epic2.getSubtaskList().size());
    }

    @Test
    void test50_clearSubtasksInEpicIfEpicDoesNotAddToStorageThrowsTaskNotFoundException() {
        Epic epic1 = new Epic();
        Epic epic2 = new Epic();

        Subtask subtask1 = new Subtask(epic1);
        Subtask subtask2 = new Subtask(epic2);

        Assertions.assertThrows(TaskNotFoundException.class, () -> manager.clearSubtasksInEpic(epic1));
    }

    @Test
    void test51_clearSubtasksInEpicIfAddToEpic2SubtasksShouldReturnFromEpic0Subtask() throws TaskNotFoundException, TasksIntersectionException {
        Epic epic1 = new Epic();

        Subtask subtask1 = new Subtask(epic1);
        Subtask subtask2 = new Subtask(epic1);

        manager.addEpicWithSubtask(epic1, subtask1, subtask2);
        manager.clearSubtasksInEpic(epic1);
        Assertions.assertEquals(0, epic1.getSubtaskList().size());
    }

    @Test
    void test52_clearSubtasksInEpicIfAddToEpic2SubtasksShouldReturnFromStorage0SubtaskOwnThisEpic() throws TaskNotFoundException, TasksIntersectionException {
        Epic epic1 = new Epic();

        Subtask subtask1 = new Subtask(epic1);
        Subtask subtask2 = new Subtask(epic1);

        manager.addEpicWithSubtask(epic1, subtask1, subtask2);
        manager.clearSubtasksInEpic(epic1);

        Assertions.assertEquals(0, manager.getListSubtasks().size());
    }

    @Test
    void test53_clearSubtasksInEpicIfAddTo2Epics2SubtasksEachAfterUseMethodForOneEpicShouldReturnFromStorage2SubtaskOwnAnotherEpic() throws TaskNotFoundException, TasksIntersectionException {
        Epic epic1 = new Epic();
        Epic epic2 = new Epic();

        Subtask subtask1 = new Subtask(epic1);
        Subtask subtask2 = new Subtask(epic1);
        Subtask subtask3 = new Subtask(epic2);
        Subtask subtask4 = new Subtask(epic2);

        manager.addEpicWithSubtask(epic1, subtask1, subtask2);
        manager.addEpicWithSubtask(epic2, subtask3, subtask4);
        manager.clearSubtasksInEpic(epic1);

        Assertions.assertEquals(2, manager.getListSubtasks().size());
    }

    @Test
    void test54_removeSingleTaskByUuidIfStorageNotContainsTaskThrowTaskNotFoundException() {
        SingleTask singleTask1 = new SingleTask();

        Assertions.assertThrows(TaskNotFoundException.class,
                () -> manager.removeSingleTaskByUuid(singleTask1.getUuid()));
    }

    @Test
    void test55_removeSingleTaskByUuidAdd2SingleTasksShouldReturn1SingleTaskFromStorageAfterUseMethod() throws TaskNotFoundException, TasksIntersectionException {
        SingleTask singleTask1 = new SingleTask();
        SingleTask singleTask2 = new SingleTask();

        manager.addSingleTask(singleTask1);
        manager.addSingleTask(singleTask2);

        manager.removeSingleTaskByUuid(singleTask1.getUuid());
        Assertions.assertEquals(1, manager.getListSingleTasks().size());
    }

    @Test
    void test56_removeSubtaskByUuidIfStorageNotContainsTaskThrowTaskNotFoundException() {
        Epic epic1 = new Epic();
        Subtask subtask1 = new Subtask(epic1);

        Assertions.assertThrows(TaskNotFoundException.class, () -> manager.removeSubtaskByUuid(subtask1.getUuid()));
    }

    @Test
    void test57_removeSubtaskByUuidIfAddTwoSubtasksToEpicThenRemove1ShouldReturn1SubtaskFromThisEpic() throws TaskNotFoundException, TasksIntersectionException {
        Epic epic1 = new Epic();

        Subtask subtask1 = new Subtask(epic1);
        Subtask subtask2 = new Subtask(epic1);

        manager.addEpicWithSubtask(epic1, subtask1, subtask2);

        manager.removeSubtaskByUuid(subtask1.getUuid());
        Assertions.assertEquals(1, epic1.getSubtaskList().size());
    }

    @Test
    void test58_removeSubtaskByUuidIfAddTwoSubtasksToEpicThenRemove1ShouldReturn1SubtaskFromStorage() throws TaskNotFoundException, TasksIntersectionException {
        Epic epic1 = new Epic();

        Subtask subtask1 = new Subtask(epic1);
        Subtask subtask2 = new Subtask(epic1);

        manager.addEpicWithSubtask(epic1, subtask1, subtask2);
        manager.removeSubtaskByUuid(subtask2.getUuid());
        Assertions.assertEquals(1, manager.getListSubtasks().size());
    }

    @Test
    void test59_removeEpicByUuidIfStorageNotContainsTaskThrowTaskNotFoundException() {
        Epic epic1 = new Epic();

        Assertions.assertThrows(TaskNotFoundException.class,
                () -> manager.removeEpicByUuid(epic1.getUuid()));
    }

    @Test
    void test60_removeEpicByUuidAdd2EpicShouldReturn1EpicFromStorageAfterUseMethod() throws TaskNotFoundException {
        Epic epic1 = new Epic();
        Epic epic2 = new Epic();

        manager.addEpic(epic1);
        manager.addEpic(epic2);

        manager.removeEpicByUuid(epic2.getUuid());
        Assertions.assertEquals(1, manager.getListEpics().size());
    }

    @Test
    void test61_removeEpicByUuidAdd2EpicWithTwoSubtaskEachShouldReturn2SubtaskFromStorageAfterUseMethod() throws TaskNotFoundException, TasksIntersectionException {
        Epic epic1 = new Epic();
        Epic epic2 = new Epic();

        Subtask subtask1 = new Subtask(epic1);
        Subtask subtask2 = new Subtask(epic1);
        Subtask subtask3 = new Subtask(epic2);
        Subtask subtask4 = new Subtask(epic2);

        manager.addEpicWithSubtask(epic1, subtask1, subtask2);
        manager.addEpicWithSubtask(epic2, subtask3, subtask4);

        manager.removeEpicByUuid(epic2.getUuid());
        Assertions.assertEquals(2, manager.getListSubtasks().size());
    }

    @Test
    void test62_getPrioritizedTasksShouldReturn4IfAdd4Tasks() throws TasksIntersectionException {
        Epic epic1 = new Epic();
        Subtask subtask1 = new Subtask(epic1);
        Subtask subtask2 = new Subtask(epic1);
        SingleTask singleTask1 = new SingleTask();

        subtask1.setStartTimeAndDuration(LocalDateTime.of(2022,12,20,12,20),Duration.ofDays(20));
        subtask2.setStartTimeAndDuration(LocalDateTime.of(2021,12,20,12,20), Duration.ofDays(30));
        singleTask1.setStartTimeAndDuration(LocalDateTime.of(2023,12,20,12,20), Duration.ofDays(40));

        manager.addEpicWithSubtask(epic1, subtask1, subtask2);
        manager.addSingleTask(singleTask1);

        Assertions.assertEquals(4, manager.getPrioritizedTasks().size());
    }

    @Test
    void test63_getPrioritizedTasksShouldReturnFirstTaskWhichHasLeastStartTime() throws TasksIntersectionException {
        Epic epic1 = new Epic();
        Subtask subtask1 = new Subtask(epic1);
        Subtask subtask2 = new Subtask(epic1);
        SingleTask singleTask1 = new SingleTask();

        subtask1.setStartTimeAndDuration(LocalDateTime.of(2022,12,20,12,20),Duration.ofDays(20));
        subtask2.setStartTimeAndDuration(LocalDateTime.of(2021,12,20,12,20), Duration.ofDays(30));
        singleTask1.setStartTimeAndDuration(LocalDateTime.of(2023,12,20,12,20), Duration.ofDays(40));

        manager.addEpicWithSubtask(epic1, subtask1, subtask2);
        manager.addSingleTask(singleTask1);

        Assertions.assertEquals(subtask2, manager.getPrioritizedTasks().get(0));
    }

    @Test
    void test62_getPrioritizedTasksShouldReturnLastTaskWithNullStartTime() throws TasksIntersectionException {
        Epic epic1 = new Epic();
        Subtask subtask1 = new Subtask(epic1);
        Subtask subtask2 = new Subtask(epic1);
        SingleTask singleTask1 = new SingleTask();

        subtask1.setStartTimeAndDuration(LocalDateTime.of(2022,12,20,12,20),Duration.ofDays(20));
        subtask2.setStartTimeAndDuration(LocalDateTime.of(2021,12,20,12,20), Duration.ofDays(30));
        singleTask1.setStartTimeAndDuration(LocalDateTime.of(2023,12,20,12,20), Duration.ofDays(40));

        manager.addEpicWithSubtask(epic1, subtask1, subtask2);
        manager.addSingleTask(singleTask1);

        Assertions.assertEquals(epic1, manager.getPrioritizedTasks().get(3));
    }

    @Test
    void test62_isTimeValidIfTwoTasksHaveEqualsStartTimeThrowsIntersectException() {
        Epic epic1 = new Epic();
        Subtask subtask1 = new Subtask(epic1);
        Subtask subtask2 = new Subtask(epic1);

        subtask1.setStartTimeAndDuration(LocalDateTime.of(2022,12,20,12,20),Duration.ofDays(20));
        subtask2.setStartTimeAndDuration(LocalDateTime.of(2022,12,20,12,20), Duration.ofDays(30));

        Assertions.assertThrows(TasksIntersectionException.class, () -> manager.addEpicWithSubtask(epic1, subtask1, subtask2));
    }

    @Test
    void test63_isTimeValidIfTwoTasksHaveEqualsEndTimeThrowsIntersectException() throws TasksIntersectionException {
        SingleTask singleTask1 = new SingleTask();
        SingleTask singleTask2 = new SingleTask();

        singleTask1.setStartTimeAndDuration(LocalDateTime.of(2022,1,11,12,20),Duration.ofDays(20));
        singleTask2.setStartTimeAndDuration(LocalDateTime.of(2022,1,1,12,20), Duration.ofDays(30));

        System.out.println(singleTask1.getEndTime());
        System.out.println(singleTask2.getEndTime());

        manager.addSingleTask(singleTask1);

        Assertions.assertThrows(TasksIntersectionException.class, () -> manager.addSingleTask(singleTask2));
    }

    @Test
    void test64_isTimeValidIfOneTaskStartInEndTimeSecondTaskThrowsIntersectException() {
        Epic epic1 = new Epic();
        Subtask subtask1 = new Subtask(epic1);
        Subtask subtask2 = new Subtask(epic1);

        subtask1.setStartTimeAndDuration(LocalDateTime.of(2022,12,1,12,20),Duration.ofDays(10));
        subtask2.setStartTimeAndDuration(LocalDateTime.of(2022,12,11,12,20), Duration.ofDays(30));

        Assertions.assertThrows(TasksIntersectionException.class, () -> manager.addEpicWithSubtask(epic1, subtask1, subtask2));
    }

    @Test
    void test64_isTimeValidIfFirstTaskEndInStartTimeSecondTaskThrowsIntersectException() {
        Epic epic1 = new Epic();
        Subtask subtask1 = new Subtask(epic1);
        Subtask subtask2 = new Subtask(epic1);

        subtask1.setStartTimeAndDuration(LocalDateTime.of(2022,12,11,12,20),Duration.ofDays(30));
        subtask2.setStartTimeAndDuration(LocalDateTime.of(2022,12,1,12,20), Duration.ofDays(10));

        Assertions.assertThrows(TasksIntersectionException.class, () -> manager.addEpicWithSubtask(epic1, subtask1, subtask2));
    }

    @Test
    void test66_isTimeValidIfStartTimeOfTaskBetweenStartAndEndTimeAnotherTaskThrowsIntersectException() throws TasksIntersectionException {
        SingleTask singleTask1 = new SingleTask();
        SingleTask singleTask2 = new SingleTask();

        singleTask1.setStartTimeAndDuration(LocalDateTime.of(2022,1,9,12,20),Duration.ofDays(20));
        singleTask2.setStartTimeAndDuration(LocalDateTime.of(2022,1,1,12,20), Duration.ofDays(30));

        manager.addSingleTask(singleTask1);

        Assertions.assertThrows(TasksIntersectionException.class, () -> manager.addSingleTask(singleTask2));
    }

    @Test
    void test67_isTimeValidIfEndTimeOfTaskBetweenStartAndEndTimeAnotherTaskThrowsIntersectException() throws TasksIntersectionException {
        SingleTask singleTask1 = new SingleTask();
        SingleTask singleTask2 = new SingleTask();

        singleTask1.setStartTimeAndDuration(LocalDateTime.of(2022,1,1,12,20),Duration.ofDays(20));
        singleTask2.setStartTimeAndDuration(LocalDateTime.of(2021,12,25,12,20), Duration.ofDays(10));

        manager.addSingleTask(singleTask1);

        Assertions.assertThrows(TasksIntersectionException.class, () -> manager.addSingleTask(singleTask2));
    }
}