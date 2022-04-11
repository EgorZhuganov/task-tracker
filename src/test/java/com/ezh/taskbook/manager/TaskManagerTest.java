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
    void test1getEpicBySubtaskUuidWithoutAddingTaskToManagerShouldThrowsTaskNotFoundException() {
        Epic epic1 = new Epic();
        Subtask subtask1 = new Subtask(epic1);

        Assertions.assertThrows(TaskNotFoundException.class, () -> manager.getEpicBySubtaskUuid(subtask1.getUuid()));
    }

    @Test
    void test2getEpicBySubtaskUuidIfDoNotPutSubtaskInParameterOfMethodShouldThrowsTaskNotFoundException() throws TasksIntersectionException {
        Epic epic1 = new Epic();
        SingleTask singleTask1 = new SingleTask();

        manager.addSingleTask(singleTask1);
        manager.addEpic(epic1);

        Assertions.assertThrows(TaskNotFoundException.class, () -> manager.getSubtaskByUuid(singleTask1.getUuid()));
        Assertions.assertThrows(TaskNotFoundException.class, () -> manager.getSubtaskByUuid(epic1.getUuid()));
    }

    @Test
    void test3getEpicBySubtaskUuidShouldReturnEpic() throws TaskNotFoundException, TasksIntersectionException {
        Epic epic1 = new Epic();
        Subtask subtask1 = new Subtask(epic1);
        epic1.getSubtaskList().add(subtask1);

        manager.addEpicWithSubtask(epic1);

        Assertions.assertEquals(epic1, manager.getEpicBySubtaskUuid(subtask1.getUuid()));
    }

    @Test
    void test4getSubtaskByUuidWithoutAddingTaskToManagerShouldThrowsTaskNotFoundException() {
        Epic epic1 = new Epic();
        Subtask subtask1 = new Subtask(epic1);

        Assertions.assertThrows(TaskNotFoundException.class, () -> manager.getSubtaskByUuid(subtask1.getUuid()));
    }

    @Test
    void test5getSubtaskByUuidIfDoNotPutSubtaskInParameterOfMethodShouldThrowsTaskNotFoundException() throws TasksIntersectionException {
        Epic epic1 = new Epic();
        SingleTask singleTask1 = new SingleTask();

        manager.addSingleTask(singleTask1);
        manager.addEpic(epic1);

        Assertions.assertThrows(TaskNotFoundException.class, () -> manager.getSubtaskByUuid(singleTask1.getUuid()));
        Assertions.assertThrows(TaskNotFoundException.class, () -> manager.getSubtaskByUuid(epic1.getUuid()));
    }

    @Test
    void test6getSubtaskByUuidShouldReturnSubtask() throws TaskNotFoundException, TasksIntersectionException {
        Epic epic1 = new Epic();
        Subtask subtask1 = new Subtask(epic1);
        epic1.getSubtaskList().add(subtask1);

        manager.addEpicWithSubtask(epic1);

        Assertions.assertEquals(subtask1, manager.getSubtaskByUuid(subtask1.getUuid()));
    }

    @Test
    void test7getEpicByUuidWithoutAddingTaskToManagerShouldThrowsTaskNotFoundException() {
        Epic epic1 = new Epic();
        Assertions.assertThrows(TaskNotFoundException.class, () -> manager.getEpicByUuid(epic1.getUuid()));
    }

    @Test
    void test8getEpicByUuidIfDoNotPutSubtaskInParameterOfMethodShouldThrowsTaskNotFoundException() throws TasksIntersectionException {
        Epic epic1 = new Epic();
        Subtask subtask1 = new Subtask(epic1);
        SingleTask singleTask1 = new SingleTask();
        epic1.getSubtaskList().add(subtask1);

        manager.addSingleTask(singleTask1);
        manager.addEpicWithSubtask(epic1);

        Assertions.assertThrows(TaskNotFoundException.class, () -> manager.getEpicByUuid(singleTask1.getUuid()));
        Assertions.assertThrows(TaskNotFoundException.class, () -> manager.getEpicByUuid(subtask1.getUuid()));
    }

    @Test
    void test9getEpicByUuidShouldReturnEpic() throws TaskNotFoundException, TasksIntersectionException {
        Epic epic1 = new Epic();
        Subtask subtask1 = new Subtask(epic1);
        epic1.getSubtaskList().add(subtask1);

        manager.addEpicWithSubtask(epic1);
        Assertions.assertEquals(epic1, manager.getEpicByUuid(epic1.getUuid()));
    }

    @Test
    void test10getSingleTaskByUuidWithoutAddingTaskToManagerShouldThrowsTaskNotFoundException() {
        SingleTask singleTask1 = new SingleTask();
        Assertions.assertThrows(TaskNotFoundException.class, () -> manager.getSingleTaskByUuid(singleTask1.getUuid()));
    }

    @Test
    void test11getSingleTaskByUuidIfDoNotPutSubtaskInParameterOfMethodShouldThrowsTaskNotFoundException() throws TasksIntersectionException {
        Epic epic1 = new Epic();
        Subtask subtask1 = new Subtask(epic1);
        epic1.getSubtaskList().add(subtask1);

        manager.addEpicWithSubtask(epic1);

        Assertions.assertThrows(TaskNotFoundException.class, () -> manager.getSingleTaskByUuid(epic1.getUuid()));
        Assertions.assertThrows(TaskNotFoundException.class, () -> manager.getSingleTaskByUuid(subtask1.getUuid()));
    }

    @Test
    void test12getSingleTaskByUuidShouldReturnSingleTask() throws TaskNotFoundException, TasksIntersectionException {
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
    void test13getListSubtasksIfAddEpicWith3SubtasksShouldReturn3() throws TasksIntersectionException {
        Epic epic1 = new Epic();
        Subtask subtask1 = new Subtask(epic1);
        Subtask subtask2 = new Subtask(epic1);
        Subtask subtask3 = new Subtask(epic1);

        epic1.getSubtaskList().add(subtask1);
        epic1.getSubtaskList().add(subtask2);
        epic1.getSubtaskList().add(subtask3);

        manager.addEpicWithSubtask(epic1);
        Assertions.assertEquals(3, manager.getListSubtasks().size());
    }

    @Test
    void test14getListEpicsIfDoNotAddEpicsThroughManagerShouldReturn0() {
        Epic epic1 = new Epic();
        Epic epic2 = new Epic();

        Assertions.assertEquals(0, manager.getListEpics().size());
    }

    @Test
    void test15getListEpicsIfAdd2EpicsShouldReturn2() {
        Epic epic1 = new Epic();
        Epic epic2 = new Epic();

        manager.addEpic(epic1);
        manager.addEpic(epic2);
        Assertions.assertEquals(2, manager.getListEpics().size());
    }

    @Test
    void test16getListSubtasksByEpicIfDoNotAddEpicsThroughManagerShouldThrowTaskNotFoundException() {
        Epic epic1 = new Epic();
        Subtask subtask1 = new Subtask(epic1);
        Subtask subtask2 = new Subtask(epic1);

        Assertions.assertThrows(TaskNotFoundException.class, () -> manager.getListSubtasksByEpicUuid(epic1.getUuid()));
    }

    @Test
    void test17getListSubtasksByEpicIfAddEpicWith2SubtasksShouldReturn2() throws TaskNotFoundException, TasksIntersectionException {
        Epic epic1 = new Epic();
        Subtask subtask1 = new Subtask(epic1);
        Subtask subtask2 = new Subtask(epic1);
        epic1.getSubtaskList().add(subtask1);
        epic1.getSubtaskList().add(subtask2);

        manager.addEpicWithSubtask(epic1);

        Assertions.assertEquals(2, manager.getListSubtasksByEpicUuid(epic1.getUuid()).size());
    }

    @Test
    void test18getListSubtasksByEpicIfAddEpicWithoutAnySubtasksShouldReturn0() throws TaskNotFoundException, TasksIntersectionException {
        Epic epic1 = new Epic();

        manager.addEpicWithSubtask(epic1);

        Assertions.assertEquals(0, manager.getListSubtasksByEpicUuid(epic1.getUuid()).size());
    }

    @Test
    void test19getListSingleTasksIfDoNotAddSingleTaskThroughManagerShouldReturn0() {
        SingleTask singleTask1 = new SingleTask();
        Assertions.assertEquals(0, manager.getListSingleTasks().size());
    }

    @Test
    void test20getListSingleTasksIfAdd2SingleTasksShouldReturn2() throws TasksIntersectionException {
        SingleTask singleTask1 = new SingleTask();
        SingleTask singleTask2 = new SingleTask();

        manager.addSingleTask(singleTask1);
        manager.addSingleTask(singleTask2);
        Assertions.assertEquals(2, manager.getListSingleTasks().size());
    }

    @Test
    void test21addEpicShouldReturnFromManagerTheSameEpic() throws TaskNotFoundException {
        Epic epic1 = new Epic();

        manager.addEpic(epic1);
        Assertions.assertEquals(epic1, manager.getEpicByUuid(epic1.getUuid()));
    }

    @Test
    void test22addEpicAdd2EpicsShouldReturnFromManager2Epics() {
        Epic epic1 = new Epic();
        Epic epic2 = new Epic();

        manager.addEpic(epic1);
        manager.addEpic(epic2);

        Assertions.assertEquals(2, manager.getListEpics().size());
    }

    @Test /*must not put two Epics which have the same uuid*/
    void test23addEpicAddOneEpicTwiceShouldThrowsRuntimeException() {
        Epic epic1 = new Epic();
        manager.addEpic(epic1);
        Assertions.assertThrows(RuntimeException.class, () -> manager.addEpic(epic1));
    }

    @Test
    void test24addSingleTaskShouldReturnFromManagerTheSameSingleTask() throws TaskNotFoundException, TasksIntersectionException {
        SingleTask singleTask1 = new SingleTask();

        manager.addSingleTask(singleTask1);
        Assertions.assertEquals(singleTask1, manager.getSingleTaskByUuid(singleTask1.getUuid()));
    }

    @Test
    void test25addSingleTaskAdd2SingleTasksShouldReturnFromManager2SingleTask() throws TasksIntersectionException {
        SingleTask singleTask1 = new SingleTask();
        SingleTask singleTask2 = new SingleTask();

        manager.addSingleTask(singleTask1);
        manager.addSingleTask(singleTask2);

        Assertions.assertEquals(2, manager.getListSingleTasks().size());
    }

    @Test /*must not put two SingleTask which have the same uuid*/
    void test26addSingleTaskAddOneEpicTwiceShouldThrowsRuntimeException() throws TasksIntersectionException {
        SingleTask singleTask1 = new SingleTask();
        manager.addSingleTask(singleTask1);
        Assertions.assertThrows(RuntimeException.class, () -> manager.addSingleTask(singleTask1));
    }

    @Test
    void test27addEpicWithSubtaskAddOneEpicWithTwoSubtasksShouldReturnFromManagerStorage1EpicAnd2Subtasks() throws TaskNotFoundException, TasksIntersectionException {
        Epic epic1 = new Epic();
        Subtask subtask1 = new Subtask(epic1);
        Subtask subtask2 = new Subtask(epic1);
        epic1.getSubtaskList().add(subtask1);
        epic1.getSubtaskList().add(subtask2);

        manager.addEpicWithSubtask(epic1);
        Assertions.assertEquals(2, manager.getListSubtasks().size());
        Assertions.assertEquals(1, manager.getListEpics().size());
        Assertions.assertEquals(epic1, manager.getEpicByUuid(epic1.getUuid()));
    }

    @Test
        /* unable to add Epic even if try to add with subtask which  missing in the manager's storage because
         * must not put two Epic which have the same uuid */
    void test28addEpicWithSubtaskAddEpicWithTwoSubtasksAndThenTryToAddTheSameEpicWithAnotherSubtaskThrowsRuntimeException() throws TasksIntersectionException {
        Epic epic1 = new Epic();
        Subtask subtask1 = new Subtask(epic1);
        Subtask subtask2 = new Subtask(epic1);
        Subtask subtask3 = new Subtask(epic1);

        epic1.getSubtaskList().add(subtask1);
        epic1.getSubtaskList().add(subtask2);

        manager.addEpicWithSubtask(epic1);
        epic1.getSubtaskList().add(subtask3);

        Assertions.assertThrows(RuntimeException.class, () -> manager.addEpicWithSubtask(epic1));
    }

    @Test
    void test29addEpicWithSubtaskAddOneEpicWithTwoSubtasksShouldReturn2SubtasksFromThisEpic() throws TasksIntersectionException {
        Epic epic1 = new Epic();
        Subtask subtask1 = new Subtask(epic1);
        Subtask subtask2 = new Subtask(epic1);

        epic1.getSubtaskList().add(subtask1);
        epic1.getSubtaskList().add(subtask2);

        manager.addEpicWithSubtask(epic1);

        Assertions.assertEquals(2, epic1.getSubtaskList().size());
    }

    @Test /*If add subtask1 in epic1 and then use method addEpicWithSubtask(epic2 , subtask1), in subtask1 will has
     another epic, in this case epic2*/
    void test30addEpicWithSubtaskCheckThatSubtaskHasChangedEpic() throws TasksIntersectionException {
        Epic epic1 = new Epic();
        Epic epic2 = new Epic();
        Subtask subtask1 = new Subtask(epic1); //set epic1
        Subtask subtask2 = new Subtask(epic1); //set epic1

        epic2.getSubtaskList().add(subtask1);
        epic2.getSubtaskList().add(subtask2);

        manager.addEpicWithSubtask(epic2); //reset to epic2

        Assertions.assertEquals(epic2.getUuid(), subtask1.getEpicId());
        Assertions.assertEquals(epic2.getUuid(), subtask2.getEpicId());
    }

    @Test
    void test31addSubtaskInAddedEpicAddSubtaskWithTheSameUuidInOneEpicThrowsRuntimeException() throws TasksIntersectionException, TaskNotFoundException {
        Epic epic1 = new Epic();
        Subtask subtask1 = new Subtask(epic1);

        manager.addEpic(epic1);
        manager.addSubtaskInAddedEpic(subtask1);
        Assertions.assertThrows(RuntimeException.class, () -> manager.addSubtaskInAddedEpic(subtask1));
    }

    @Test
    void test32addSubtaskInAddedEpicAddTwoSubtasksInOneEpicShouldReturnFromStorageTwoSubtasks() throws TasksIntersectionException, TaskNotFoundException {
        Epic epic1 = new Epic();
        Subtask subtask1 = new Subtask(epic1);
        Subtask subtask2 = new Subtask(epic1);

        manager.addEpic(epic1);
        manager.addSubtaskInAddedEpic(subtask1);
        manager.addSubtaskInAddedEpic(subtask2);
        Assertions.assertEquals(2, manager.getListSubtasks().size());
    }

    @Test
    void test33addSubtaskInAddedEpicAddTwoSubtasksInOneEpicShouldReturnFromEpicTwoSubtasks() throws TasksIntersectionException, TaskNotFoundException {
        Epic epic1 = new Epic();
        Subtask subtask1 = new Subtask(epic1);
        Subtask subtask2 = new Subtask(epic1);

        manager.addEpic(epic1);
        manager.addSubtaskInAddedEpic(subtask1);
        manager.addSubtaskInAddedEpic(subtask2);
        Assertions.assertEquals(2, epic1.getSubtaskList().size());
    }

    @Test
    void test34changeSingleTaskByUuidStorageIsEmptyThrowsTaskNotFoundException() {
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
    void test35changeSingleTaskByUuidTryToChangeSingleTaskBySingleTaskWithTheSameUuidThrowsRuntimeException() throws TasksIntersectionException {
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
    void test36changeSingleTaskByUuidSetSingleTaskValuesFromAnotherSingleTaskShouldTheSameValuesInFirstTask() throws TaskNotFoundException, TasksIntersectionException {
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
    void test37changeEpicByUuidStorageIsEmptyThrowsTaskNotFoundException() {
        Epic epic1 = new Epic();
        Epic epic2 = new Epic();

        epic2.setName("I'm epic task");
        epic2.setDescription("About something");

        Assertions.assertThrows(TaskNotFoundException.class, () ->
                manager.changeEpicByUuid(epic1.getUuid(), epic2));
    }

    @Test
    void test38changeEpicByUuidTryToChangeEpicByEpicWithTheSameUuidThrowsRuntimeException() {
        Epic epic1 = new Epic();

        manager.addEpic(epic1);

        epic1.setName("I'm real task");
        epic1.setDescription("About something");

        Assertions.assertThrows(RuntimeException.class, () ->
                manager.changeEpicByUuid(epic1.getUuid(), epic1));
    }

    @Test
    void test39changeEpicByUuidSetEpicValuesFromAnotherEpicShouldTheSameValuesInFirstTask() throws TaskNotFoundException {
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
    void test40changeSubtaskByUuidStorageIsEmptyThrowsTaskNotFoundException() {
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
    void test41changeSubtaskByUuidTryToChangeSubtaskBySubtaskWithTheSameUuidThrowsRuntimeException() throws TasksIntersectionException {
        Epic epic1 = new Epic();
        Subtask subtask1 = new Subtask(epic1);
        epic1.getSubtaskList().add(subtask1);

        manager.addEpicWithSubtask(epic1);

        subtask1.setName("I'm real task");
        subtask1.setDescription("About something");
        subtask1.setStartTimeAndDuration(LocalDateTime.of(2022, 12, 12, 23, 59), Duration.ofDays(5));

        Assertions.assertThrows(IllegalArgumentException.class, () ->
                manager.changeSubtaskByUuid(subtask1.getUuid(), subtask1));
    }

    @Test
    void test42changeSubtaskByUuidSetSubtaskValuesFromAnotherSubtaskShouldTheSameValuesInFirstTask()
            throws TaskNotFoundException, TasksIntersectionException {
        Epic epic1 = new Epic();
        Subtask subtask1 = new Subtask(epic1);
        Subtask subtask2 = new Subtask(epic1);

        manager.addEpic(epic1);
        manager.addSubtaskInAddedEpic(subtask1);
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
    void test43clearSingleTasksAddTwoSingleTasksShouldReturnFromManagerAfterUseMethod0SingleTasks() throws TasksIntersectionException {
        SingleTask singleTask1 = new SingleTask();
        SingleTask singleTask2 = new SingleTask();

        manager.addSingleTask(singleTask1);
        manager.addSingleTask(singleTask2);
        Assertions.assertEquals(2, manager.getListSingleTasks().size());
        manager.clearSingleTasks();
        Assertions.assertEquals(0, manager.getListSingleTasks().size());
    }

    @Test
    void test44clearSingleTasksWithEmptyStorageNotThrowsAnyException() {
        SingleTask singleTask1 = new SingleTask();
        SingleTask singleTask2 = new SingleTask();

        Assertions.assertDoesNotThrow(() -> manager.clearSingleTasks());
    }

    @Test
    void test45clearEpicsAddTwoEpicsShouldReturnFromManagerAfterUseMethod0Epics() {
        Epic epic1 = new Epic();
        Epic epic2 = new Epic();

        manager.addEpic(epic1);
        manager.addEpic(epic2);
        Assertions.assertEquals(2, manager.getListEpics().size());
        manager.clearEpics();
        Assertions.assertEquals(0, manager.getListEpics().size());
    }

    @Test
    void test46clearEpicsWithEmptyStorageNotThrowsAnyException() {
        Epic epic1 = new Epic();

        Assertions.assertDoesNotThrow(() -> manager.clearEpics());
    }

    @Test
    void test47clearSubtasksInAllEpicWithEmptyStorageNotThrowsAnyException() {
        Epic epic1 = new Epic();
        Epic epic2 = new Epic();
        Subtask subtask1 = new Subtask(epic1);
        Subtask subtask2 = new Subtask(epic2);

        Assertions.assertDoesNotThrow(() -> manager.clearSubtasksInAllEpic());
    }

    @Test
    void test48clearSubtasksInAllEpicAdd2EpicAnd4SubtaskShouldReturnFromManager0Subtask() throws TasksIntersectionException {
        Epic epic1 = new Epic();
        Epic epic2 = new Epic();

        Subtask subtask1 = new Subtask(epic1);
        Subtask subtask2 = new Subtask(epic1);
        Subtask subtask3 = new Subtask(epic2);
        Subtask subtask4 = new Subtask(epic2);

        epic1.getSubtaskList().add(subtask1);
        epic1.getSubtaskList().add(subtask2);
        epic2.getSubtaskList().add(subtask3);
        epic2.getSubtaskList().add(subtask4);

        manager.addEpicWithSubtask(epic1);
        manager.addEpicWithSubtask(epic2);

        manager.clearSubtasksInAllEpic();
        Assertions.assertEquals(0, manager.getListSubtasks().size());

    }

    @Test
    void test49clearSubtasksInAllEpicAdd2EpicAnd4SubtaskShouldReturn0SubtaskFromTheseEpics() throws TasksIntersectionException {
        Epic epic1 = new Epic();
        Epic epic2 = new Epic();

        Subtask subtask1 = new Subtask(epic1);
        Subtask subtask2 = new Subtask(epic1);
        Subtask subtask3 = new Subtask(epic2);
        Subtask subtask4 = new Subtask(epic2);

        epic1.getSubtaskList().add(subtask1);
        epic1.getSubtaskList().add(subtask2);
        epic2.getSubtaskList().add(subtask3);
        epic2.getSubtaskList().add(subtask4);

        manager.addEpicWithSubtask(epic1);
        manager.addEpicWithSubtask(epic2);

        manager.clearSubtasksInAllEpic();
        Assertions.assertEquals(0, epic1.getSubtaskList().size());
        Assertions.assertEquals(0, epic2.getSubtaskList().size());
    }

    @Test
    void test50clearSubtasksInEpicIfEpicDoesNotAddToStorageThrowsTaskNotFoundException() {
        Epic epic1 = new Epic();
        Epic epic2 = new Epic();

        Subtask subtask1 = new Subtask(epic1);
        Subtask subtask2 = new Subtask(epic2);

        Assertions.assertThrows(TaskNotFoundException.class, () -> manager.clearSubtasksInEpicByEpicUuid(epic1.getUuid()));
    }

    @Test
    void test51clearSubtasksInEpicIfAddToEpic2SubtasksShouldReturnFromEpic0Subtask() throws TaskNotFoundException, TasksIntersectionException {
        Epic epic1 = new Epic();

        Subtask subtask1 = new Subtask(epic1);
        Subtask subtask2 = new Subtask(epic1);

        epic1.getSubtaskList().add(subtask1);
        epic1.getSubtaskList().add(subtask2);

        manager.addEpicWithSubtask(epic1);
        manager.clearSubtasksInEpicByEpicUuid(epic1.getUuid());
        Assertions.assertEquals(0, epic1.getSubtaskList().size());
    }

    @Test
    void test52clearSubtasksInEpicIfAddToEpic2SubtasksShouldReturnFromStorage0SubtaskOwnThisEpic() throws TaskNotFoundException, TasksIntersectionException {
        Epic epic1 = new Epic();

        Subtask subtask1 = new Subtask(epic1);
        Subtask subtask2 = new Subtask(epic1);

        epic1.getSubtaskList().add(subtask1);
        epic1.getSubtaskList().add(subtask2);

        manager.addEpicWithSubtask(epic1);
        manager.clearSubtasksInEpicByEpicUuid(epic1.getUuid());

        Assertions.assertEquals(0, manager.getListSubtasks().size());
    }

    @Test
    void test53clearSubtasksInEpicIfAddTo2Epics2SubtasksEachShouldReturn2SubtasksFromStorage()
            throws TaskNotFoundException, TasksIntersectionException {
        Epic epic1 = new Epic();
        Epic epic2 = new Epic();

        Subtask subtask1 = new Subtask(epic1);
        Subtask subtask2 = new Subtask(epic1);
        Subtask subtask3 = new Subtask(epic2);
        Subtask subtask4 = new Subtask(epic2);

        epic1.getSubtaskList().add(subtask1);
        epic1.getSubtaskList().add(subtask2);
        epic2.getSubtaskList().add(subtask3);
        epic2.getSubtaskList().add(subtask4);

        manager.addEpicWithSubtask(epic1);
        manager.addEpicWithSubtask(epic2);

        manager.clearSubtasksInEpicByEpicUuid(epic1.getUuid());

        Assertions.assertEquals(2, manager.getListSubtasks().size());
    }

    @Test
    void test54removeSingleTaskByUuidIfStorageNotContainsTaskThrowTaskNotFoundException() {
        SingleTask singleTask1 = new SingleTask();

        Assertions.assertThrows(TaskNotFoundException.class,
                () -> manager.removeSingleTaskByUuid(singleTask1.getUuid()));
    }

    @Test
    void test55removeSingleTaskByUuidAdd2SingleTasksShouldReturn1SingleTaskFromStorageAfterUseMethod()
            throws TaskNotFoundException, TasksIntersectionException {
        SingleTask singleTask1 = new SingleTask();
        SingleTask singleTask2 = new SingleTask();

        manager.addSingleTask(singleTask1);
        manager.addSingleTask(singleTask2);

        manager.removeSingleTaskByUuid(singleTask1.getUuid());
        Assertions.assertEquals(1, manager.getListSingleTasks().size());
    }

    @Test
    void test56removeSubtaskByUuidIfStorageNotContainsTaskThrowTaskNotFoundException() {
        Epic epic1 = new Epic();
        Subtask subtask1 = new Subtask(epic1);

        Assertions.assertThrows(TaskNotFoundException.class, () -> manager.removeSubtaskByUuid(subtask1.getUuid()));
    }

    @Test
    void test57removeSubtaskByUuidIfAddTwoSubtasksToEpicThenRemove1ShouldReturn1SubtaskFromThisEpic()
            throws TaskNotFoundException, TasksIntersectionException {
        Epic epic1 = new Epic();

        Subtask subtask1 = new Subtask(epic1);
        Subtask subtask2 = new Subtask(epic1);

        epic1.getSubtaskList().add(subtask1);
        epic1.getSubtaskList().add(subtask2);

        manager.addEpicWithSubtask(epic1);

        manager.removeSubtaskByUuid(subtask1.getUuid());
        Assertions.assertEquals(1, epic1.getSubtaskList().size());
    }

    @Test
    void test58removeSubtaskByUuidIfAddTwoSubtasksToEpicThenRemove1ShouldReturn1SubtaskFromStorage()
            throws TaskNotFoundException, TasksIntersectionException {
        Epic epic1 = new Epic();

        Subtask subtask1 = new Subtask(epic1);
        Subtask subtask2 = new Subtask(epic1);

        epic1.getSubtaskList().add(subtask1);
        epic1.getSubtaskList().add(subtask2);

        manager.addEpicWithSubtask(epic1);
        manager.removeSubtaskByUuid(subtask2.getUuid());
        Assertions.assertEquals(1, manager.getListSubtasks().size());
    }

    @Test
    void test59removeEpicByUuidIfStorageNotContainsTaskThrowTaskNotFoundException() {
        Epic epic1 = new Epic();

        Assertions.assertThrows(TaskNotFoundException.class,
                () -> manager.removeEpicByUuid(epic1.getUuid()));
    }

    @Test
    void test60removeEpicByUuidAdd2EpicShouldReturn1EpicFromStorageAfterUseMethod() throws TaskNotFoundException {
        Epic epic1 = new Epic();
        Epic epic2 = new Epic();

        manager.addEpic(epic1);
        manager.addEpic(epic2);

        manager.removeEpicByUuid(epic2.getUuid());
        Assertions.assertEquals(1, manager.getListEpics().size());
    }

    @Test
    void test61removeEpicByUuidAdd2EpicWithTwoSubtaskEachShouldReturn2SubtaskFromStorageAfterUseMethod()
            throws TaskNotFoundException, TasksIntersectionException {
        Epic epic1 = new Epic();
        Epic epic2 = new Epic();

        Subtask subtask1 = new Subtask(epic1);
        Subtask subtask2 = new Subtask(epic1);
        Subtask subtask3 = new Subtask(epic2);
        Subtask subtask4 = new Subtask(epic2);

        epic1.getSubtaskList().add(subtask1);
        epic1.getSubtaskList().add(subtask2);
        epic2.getSubtaskList().add(subtask3);
        epic2.getSubtaskList().add(subtask4);

        manager.addEpicWithSubtask(epic1);
        manager.addEpicWithSubtask(epic2);

        manager.removeEpicByUuid(epic2.getUuid());
        Assertions.assertEquals(2, manager.getListSubtasks().size());
    }

    @Test
    void test62getPrioritizedTasksShouldReturn4IfAdd4Tasks() throws TasksIntersectionException {
        Epic epic1 = new Epic();
        Subtask subtask1 = new Subtask(epic1);
        Subtask subtask2 = new Subtask(epic1);
        SingleTask singleTask1 = new SingleTask();

        subtask1.setStartTimeAndDuration(LocalDateTime.of(2022, 12, 20, 12, 20), Duration.ofDays(20));
        subtask2.setStartTimeAndDuration(LocalDateTime.of(2021, 12, 20, 12, 20), Duration.ofDays(30));
        singleTask1.setStartTimeAndDuration(LocalDateTime.of(2023, 12, 20, 12, 20), Duration.ofDays(40));

        epic1.getSubtaskList().add(subtask1);
        epic1.getSubtaskList().add(subtask2);

        manager.addEpicWithSubtask(epic1);
        manager.addSingleTask(singleTask1);

        Assertions.assertEquals(4, manager.getPrioritizedTasks().size());
    }

    @Test
    void test63getPrioritizedTasksShouldReturnFirstTaskWhichHasLeastStartTime() throws TasksIntersectionException {
        Epic epic1 = new Epic();
        Subtask subtask1 = new Subtask(epic1);
        Subtask subtask2 = new Subtask(epic1);
        SingleTask singleTask1 = new SingleTask();

        subtask1.setStartTimeAndDuration(LocalDateTime.of(2022, 12, 20, 12, 20), Duration.ofDays(20));
        subtask2.setStartTimeAndDuration(LocalDateTime.of(2021, 12, 20, 12, 20), Duration.ofDays(30));
        singleTask1.setStartTimeAndDuration(LocalDateTime.of(2023, 12, 20, 12, 20), Duration.ofDays(40));

        epic1.getSubtaskList().add(subtask1);
        epic1.getSubtaskList().add(subtask2);

        manager.addEpicWithSubtask(epic1);
        manager.addSingleTask(singleTask1);

        Assertions.assertEquals(subtask2, manager.getPrioritizedTasks().get(0));
    }

    @Test
    void test64getPrioritizedTasksShouldReturnLastTaskWithNullStartTime() throws TasksIntersectionException {
        Epic epic1 = new Epic();
        Subtask subtask1 = new Subtask(epic1);
        Subtask subtask2 = new Subtask(epic1);
        SingleTask singleTask1 = new SingleTask();

        subtask1.setStartTimeAndDuration(LocalDateTime.of(2022, 12, 20, 12, 20), Duration.ofDays(20));
        subtask2.setStartTimeAndDuration(LocalDateTime.of(2021, 12, 20, 12, 20), Duration.ofDays(30));
        singleTask1.setStartTimeAndDuration(LocalDateTime.of(2023, 12, 20, 12, 20), Duration.ofDays(40));

        epic1.getSubtaskList().add(subtask1);
        epic1.getSubtaskList().add(subtask2);

        manager.addEpicWithSubtask(epic1);
        manager.addSingleTask(singleTask1);

        Assertions.assertEquals(epic1, manager.getPrioritizedTasks().get(3));
    }

    @Test
    void test65isTimeValidIfTwoTasksHaveEqualsStartTimeThrowsIntersectException() {
        Epic epic1 = new Epic();
        Subtask subtask1 = new Subtask(epic1);
        Subtask subtask2 = new Subtask(epic1);

        subtask1.setStartTimeAndDuration(LocalDateTime.of(2022, 12, 20, 12, 20), Duration.ofDays(20));
        subtask2.setStartTimeAndDuration(LocalDateTime.of(2022, 12, 20, 12, 20), Duration.ofDays(30));

        epic1.getSubtaskList().add(subtask1);
        epic1.getSubtaskList().add(subtask2);

        Assertions.assertThrows(TasksIntersectionException.class, () -> manager.addEpicWithSubtask(epic1));
    }

    @Test
    void test66isTimeValidIfTwoTasksHaveEqualsEndTimeThrowsIntersectException() throws TasksIntersectionException {
        SingleTask singleTask1 = new SingleTask();
        SingleTask singleTask2 = new SingleTask();

        singleTask1.setStartTimeAndDuration(LocalDateTime.of(2022, 1, 11, 12, 20), Duration.ofDays(20));
        singleTask2.setStartTimeAndDuration(LocalDateTime.of(2022, 1, 1, 12, 20), Duration.ofDays(30));

        System.out.println(singleTask1.getEndTime());
        System.out.println(singleTask2.getEndTime());

        manager.addSingleTask(singleTask1);

        Assertions.assertThrows(TasksIntersectionException.class, () -> manager.addSingleTask(singleTask2));
    }

    @Test
    void test67isTimeValidIfOneTaskStartInEndTimeSecondTaskThrowsIntersectException() {
        Epic epic1 = new Epic();
        Subtask subtask1 = new Subtask(epic1);
        Subtask subtask2 = new Subtask(epic1);

        subtask1.setStartTimeAndDuration(LocalDateTime.of(2022, 12, 1, 12, 20), Duration.ofDays(10));
        subtask2.setStartTimeAndDuration(LocalDateTime.of(2022, 12, 11, 12, 20), Duration.ofDays(30));

        epic1.getSubtaskList().add(subtask1);
        epic1.getSubtaskList().add(subtask2);

        Assertions.assertThrows(TasksIntersectionException.class, () -> manager.addEpicWithSubtask(epic1));
    }

    @Test
    void test68isTimeValidIfFirstTaskEndInStartTimeSecondTaskThrowsIntersectException() {
        Epic epic1 = new Epic();
        Subtask subtask1 = new Subtask(epic1);
        Subtask subtask2 = new Subtask(epic1);

        subtask1.setStartTimeAndDuration(LocalDateTime.of(2022, 12, 11, 12, 20), Duration.ofDays(30));
        subtask2.setStartTimeAndDuration(LocalDateTime.of(2022, 12, 1, 12, 20), Duration.ofDays(10));

        epic1.getSubtaskList().add(subtask1);
        epic1.getSubtaskList().add(subtask2);

        Assertions.assertThrows(TasksIntersectionException.class, () -> manager.addEpicWithSubtask(epic1));
    }

    @Test
    void test69isTimeValidIfStartTimeOfTaskBetweenStartAndEndTimeAnotherTaskThrowsIntersectException() throws TasksIntersectionException {
        SingleTask singleTask1 = new SingleTask();
        SingleTask singleTask2 = new SingleTask();

        singleTask1.setStartTimeAndDuration(LocalDateTime.of(2022, 1, 9, 12, 20), Duration.ofDays(20));
        singleTask2.setStartTimeAndDuration(LocalDateTime.of(2022, 1, 1, 12, 20), Duration.ofDays(30));

        manager.addSingleTask(singleTask1);

        Assertions.assertThrows(TasksIntersectionException.class, () -> manager.addSingleTask(singleTask2));
    }

    @Test
    void test70isTimeValidIfEndTimeOfTaskBetweenStartAndEndTimeAnotherTaskThrowsIntersectException() throws TasksIntersectionException {
        SingleTask singleTask1 = new SingleTask();
        SingleTask singleTask2 = new SingleTask();

        singleTask1.setStartTimeAndDuration(LocalDateTime.of(2022, 1, 1, 12, 20), Duration.ofDays(20));
        singleTask2.setStartTimeAndDuration(LocalDateTime.of(2021, 12, 25, 12, 20), Duration.ofDays(10));

        manager.addSingleTask(singleTask1);

        Assertions.assertThrows(TasksIntersectionException.class, () -> manager.addSingleTask(singleTask2));
    }
}