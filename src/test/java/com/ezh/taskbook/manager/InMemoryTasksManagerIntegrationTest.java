package com.ezh.taskbook.manager;

import com.ezh.taskbook.exception.TaskNotFoundException;
import com.ezh.taskbook.exception.TasksIntersectionException;
import com.ezh.taskbook.task.Epic;
import com.ezh.taskbook.task.SingleTask;
import com.ezh.taskbook.task.Subtask;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class InMemoryTasksManagerIntegrationTest {

    @Test
    void getEpicBySubtaskUuid() throws TaskNotFoundException, TasksIntersectionException {
        TaskManager manager = new InMemoryTasksManager();

        Epic epic1 = new Epic();
        Epic epic2 = new Epic();
        Subtask subtask1 = new Subtask(epic1);
        Subtask subtask2 = new Subtask(epic1);
        Subtask subtask3 = new Subtask(epic1);
        Subtask subtask4 = new Subtask(epic2);

        manager.addEpicWithSubtask(epic2);
        manager.addEpicWithSubtask(epic1, subtask1, subtask2, subtask3);
        //subtask4 not added in storage with help of manager
        Assertions.assertThrows(TaskNotFoundException.class, () -> manager.getEpicBySubtaskUuid(subtask4.getUuid()));
        Assertions.assertEquals(0, manager.getHistory().size());

        manager.getEpicBySubtaskUuid(subtask2.getUuid());
        Assertions.assertEquals(1, manager.getHistory().size());
        manager.removeEpicByUuid(epic2.getUuid());
        Assertions.assertEquals(1, manager.getHistory().size());
        Assertions.assertThrows(TaskNotFoundException.class, () -> manager.getEpicByUuid(epic2.getUuid()));
        manager.removeEpicByUuid(epic1.getUuid());
        Assertions.assertEquals(0, manager.getHistory().size());

    }

    @Test
    void getSubtaskByUuid() throws TaskNotFoundException, TasksIntersectionException {
        TaskManager manager = new InMemoryTasksManager();

        Epic epic1 = new Epic();
        Epic epic2 = new Epic();
        Subtask subtask1 = new Subtask(epic1);
        Subtask subtask2 = new Subtask(epic1);
        Subtask subtask3 = new Subtask(epic1);
        Subtask subtask4 = new Subtask(epic2);
        SingleTask singleTask = new SingleTask();

        manager.addEpicWithSubtask(epic2);
        manager.addEpicWithSubtask(epic1, subtask1, subtask2, subtask3);
        manager.getSubtaskByUuid(subtask1.getUuid());
        manager.getSubtaskByUuid(subtask1.getUuid());
        manager.getSubtaskByUuid(subtask1.getUuid());

        //subtask4 not added in storage with help of manager
        Assertions.assertThrows(TaskNotFoundException.class, () -> manager.getSubtaskByUuid(subtask4.getUuid()));
        Assertions.assertThrows(TaskNotFoundException.class, () -> manager.getSubtaskByUuid(singleTask.getUuid()));
        Assertions.assertThrows(TaskNotFoundException.class, () -> manager.getSubtaskByUuid(epic1.getUuid()));
        Assertions.assertEquals(2, manager.getListEpics().size());
        Assertions.assertEquals(1, manager.getHistory().size());
        Assertions.assertEquals(subtask1.getUuid(), manager.getHistory().get(0).getUuid());


    }

    @Test
    void getEpicByUuid() throws TaskNotFoundException, TasksIntersectionException {
        TaskManager manager = new InMemoryTasksManager();

        Epic epic1 = new Epic();
        Epic epic2 = new Epic();
        Epic epic3 = new Epic();
        Epic epic4 = new Epic();
        Subtask subtask1 = new Subtask(epic1);
        Subtask subtask2 = new Subtask(epic1);
        Subtask subtask3 = new Subtask(epic1);
        Subtask subtask4 = new Subtask(epic2);
        Subtask subtask5 = new Subtask(epic2);
        Subtask subtask6 = new Subtask(epic2);

        SingleTask singleTask = new SingleTask();

        manager.addEpicWithSubtask(epic2, subtask4, subtask5, subtask6);
        manager.addEpicWithSubtask(epic1, subtask1, subtask2, subtask3);

        Assertions.assertThrows(TaskNotFoundException.class, () -> manager.getEpicByUuid(singleTask.getUuid()));
        Assertions.assertThrows(TaskNotFoundException.class, () -> manager.getEpicByUuid(subtask1.getUuid()));

        manager.getEpicByUuid(epic1.getUuid());
        Assertions.assertEquals(1, manager.getHistory().size());
        Assertions.assertThrows(TaskNotFoundException.class, () -> manager.getEpicByUuid(epic4.getUuid()));

        manager.addEpic(epic3);
        manager.getEpicByUuid(epic3.getUuid());
        manager.getEpicByUuid(epic2.getUuid());
        Assertions.assertEquals(3, manager.getHistory().size());
        Assertions.assertEquals(epic1.getUuid(), manager.getHistory().get(0).getUuid());
        Assertions.assertEquals(epic2.getUuid(), manager.getHistory().get(2).getUuid());
        Assertions.assertEquals(epic3.getUuid(), manager.getHistory().get(1).getUuid());

    }

    @Test
    void getSingleTaskByUuid() throws TaskNotFoundException, TasksIntersectionException {
        TaskManager manager = new InMemoryTasksManager();

        Epic epic1 = new Epic();
        Subtask subtask1 = new Subtask(epic1);
        SingleTask singleTask1 = new SingleTask();

        Assertions.assertThrows(TaskNotFoundException.class, () -> manager.getSingleTaskByUuid(singleTask1.getUuid()));
        manager.addSingleTask(singleTask1);

        Assertions.assertEquals(singleTask1.getUuid(), manager.getSingleTaskByUuid(singleTask1.getUuid()).getUuid());
        Assertions.assertThrows(TaskNotFoundException.class, () -> manager.getSingleTaskByUuid(epic1.getUuid()));
        Assertions.assertThrows(TaskNotFoundException.class, () -> manager.getSingleTaskByUuid(subtask1.getUuid()));
        Assertions.assertEquals(1, manager.getHistory().size());

    }

    @Test
    void clearSingleTasks() throws TaskNotFoundException, TasksIntersectionException {
        TaskManager manager = new InMemoryTasksManager();

        SingleTask singleTask1 = new SingleTask();
        SingleTask singleTask2 = new SingleTask();
        SingleTask singleTask3 = new SingleTask();
        SingleTask singleTask4 = new SingleTask();

        manager.addSingleTask(singleTask1);
        manager.addSingleTask(singleTask2);
        manager.addSingleTask(singleTask4);

        Assertions.assertThrows(TaskNotFoundException.class, () -> manager.getSingleTaskByUuid(singleTask3.getUuid()));
        Assertions.assertEquals(3, manager.getListSingleTasks().size());
        Assertions.assertEquals(0, manager.getHistory().size());

        manager.getSingleTaskByUuid(singleTask2.getUuid());
        manager.getSingleTaskByUuid(singleTask1.getUuid());
        manager.clearSingleTasks();

        Assertions.assertEquals(0, manager.getListSingleTasks().size());
        Assertions.assertEquals(0, manager.getHistory().size());
    }

    @Test
    void clearEpics() throws TaskNotFoundException, TasksIntersectionException {
        TaskManager manager = new InMemoryTasksManager();

        Epic epic1 = new Epic();
        Epic epic2 = new Epic();
        Epic epic3 = new Epic();
        Epic epic4 = new Epic();
        Subtask subtask1 = new Subtask(epic2);
        Subtask subtask2 = new Subtask(epic2);
        Subtask subtask3 = new Subtask(epic2);

        manager.addEpic(epic1);
        manager.addEpicWithSubtask(epic2, subtask2, subtask3, subtask1);
        manager.addEpic(epic3);
        manager.addEpic(epic4);

        manager.getEpicByUuid(epic4.getUuid());
        manager.getEpicByUuid(epic1.getUuid());
        manager.getEpicByUuid(epic3.getUuid());
        manager.getSubtaskByUuid(subtask3.getUuid());

        Assertions.assertEquals(4, manager.getListEpics().size());
        Assertions.assertEquals(4, manager.getHistory().size());
        manager.clearEpics();
        Assertions.assertEquals(0, manager.getListEpics().size());
        Assertions.assertEquals(0, manager.getHistory().size());

    }

    @Test
    void clearSubtasksInAllEpic() throws TaskNotFoundException, TasksIntersectionException {
        TaskManager manager = new InMemoryTasksManager();

        Epic epic1 = new Epic();
        Epic epic2 = new Epic();
        Epic epic3 = new Epic();

        Subtask subtask1 = new Subtask(epic1);
        Subtask subtask2 = new Subtask(epic1);
        Subtask subtask3 = new Subtask(epic1);
        Subtask subtask4 = new Subtask(epic2);
        Subtask subtask5 = new Subtask(epic2);

        Assertions.assertEquals(0, epic1.getSubtaskList().size());
        manager.addEpicWithSubtask(epic1, subtask1, subtask2, subtask3);
        Assertions.assertEquals(3, epic1.getSubtaskList().size());

        manager.addEpic(epic2);
        manager.addSubtaskInAddedEpic(subtask4);
        manager.addSubtaskInAddedEpic(subtask5);
        Assertions.assertEquals(2, epic2.getSubtaskList().size());

        manager.addEpic(epic3);
        manager.getEpicByUuid(epic3.getUuid());
        manager.getSubtaskByUuid(subtask4.getUuid());
        manager.getEpicByUuid(epic1.getUuid());
        manager.getEpicByUuid(epic2.getUuid());

        manager.clearSubtasksInAllEpic();
        Assertions.assertEquals(0, epic1.getSubtaskList().size());
        Assertions.assertEquals(0, epic2.getSubtaskList().size());
        Assertions.assertEquals(3, manager.getHistory().size());
    }

    @Test
    void clearSubtasksInEpic() throws TaskNotFoundException, TasksIntersectionException {
        TaskManager manager = new InMemoryTasksManager();

        Epic epic1 = new Epic();
        Epic epic2 = new Epic();
        Subtask subtask1 = new Subtask(epic1);
        Subtask subtask2 = new Subtask(epic1);
        Subtask subtask3 = new Subtask(epic1);
        Subtask subtask4 = new Subtask(epic2);
        Subtask subtask5 = new Subtask(epic2);

        manager.addEpicWithSubtask(epic1, subtask1, subtask2, subtask3);
        manager.addEpicWithSubtask(epic2, subtask4, subtask5);

        manager.getSubtaskByUuid(subtask2.getUuid());
        manager.getEpicByUuid(epic2.getUuid());

        Assertions.assertEquals(2, manager.getHistory().size());

        manager.clearSubtasksInEpicByEpicUuid(epic1.getUuid());
        manager.clearSubtasksInEpicByEpicUuid(epic2.getUuid());

        Assertions.assertEquals(0, epic1.getSubtaskList().size());
        Assertions.assertEquals(1, manager.getHistory().size());
        Assertions.assertEquals(0, epic2.getSubtaskList().size());
    }

    @Test
    void removeSingleTaskByUuid() throws TaskNotFoundException, TasksIntersectionException {
        TaskManager manager = new InMemoryTasksManager();

        SingleTask singleTask1 = new SingleTask();
        SingleTask singleTask2 = new SingleTask();
        SingleTask singleTask3 = new SingleTask();

        manager.addSingleTask(singleTask1);
        manager.addSingleTask(singleTask2);
        manager.addSingleTask(singleTask3);

        manager.getSingleTaskByUuid(singleTask3.getUuid());
        manager.getSingleTaskByUuid(singleTask1.getUuid());
        manager.getSingleTaskByUuid(singleTask2.getUuid());

        Assertions.assertEquals(3, manager.getHistory().size());

        manager.removeSingleTaskByUuid(singleTask1.getUuid());

        Assertions.assertEquals(2, manager.getHistory().size());
    }

    @Test
    void removeSubtaskByUuid() throws TaskNotFoundException, TasksIntersectionException {
        TaskManager manager = new InMemoryTasksManager();

        Epic epic1 = new Epic();
        Epic epic2 = new Epic();

        Subtask subtask1 = new Subtask(epic1);
        Subtask subtask2 = new Subtask(epic1);
        Subtask subtask3 = new Subtask(epic1);
        Subtask subtask5 = new Subtask(epic2);

        manager.addEpicWithSubtask(epic1, subtask1, subtask2, subtask3);
        manager.addEpicWithSubtask(epic2, subtask5);

        manager.getSubtaskByUuid(subtask1.getUuid());
        manager.getSubtaskByUuid(subtask3.getUuid());

        Assertions.assertEquals(2, manager.getHistory().size());

        manager.removeSubtaskByUuid(subtask5.getUuid());
        manager.removeSubtaskByUuid(subtask2.getUuid());

        Assertions.assertEquals(2, manager.getHistory().size());

        manager.removeSubtaskByUuid(subtask1.getUuid());
        Assertions.assertEquals(1, manager.getHistory().size());
        Assertions.assertEquals(1, manager.getListSubtasksByEpicUuid(epic1.getUuid()).size());

        Epic epic3 = new Epic();
        Epic epic4 = new Epic();

        Subtask subtask6 = new Subtask(epic3);
        Subtask subtask7 = new Subtask(epic3);
        Subtask subtask8 = new Subtask(epic3);
        Subtask subtask9 = new Subtask(epic3);
        Subtask subtask10 = new Subtask(epic3);
        Subtask subtask11 = new Subtask(epic4);
        Subtask subtask12 = new Subtask(epic4);
        Subtask subtask13 = new Subtask(epic4);
        Subtask subtask14 = new Subtask(epic4);
        Subtask subtask15 = new Subtask(epic4);

        manager.addEpicWithSubtask(epic3, subtask6, subtask7, subtask8, subtask9, subtask10);
        manager.addEpicWithSubtask(epic4, subtask11, subtask12, subtask13, subtask14, subtask15);

        manager.getSubtaskByUuid(subtask6.getUuid());
        manager.getSubtaskByUuid(subtask7.getUuid());
        manager.getSubtaskByUuid(subtask8.getUuid());
        manager.getSubtaskByUuid(subtask9.getUuid());
        manager.getSubtaskByUuid(subtask10.getUuid());
        manager.getSubtaskByUuid(subtask11.getUuid());
        manager.getSubtaskByUuid(subtask12.getUuid());
        manager.getSubtaskByUuid(subtask13.getUuid());
        manager.getSubtaskByUuid(subtask14.getUuid());

        Assertions.assertEquals(10, manager.getHistory().size());

        manager.removeSubtaskByUuid(subtask15.getUuid()); //didn't call get, this task is absent in task history

        Assertions.assertEquals(10, manager.getHistory().size());
    }

    @Test
    void removeEpicByUuid() throws TaskNotFoundException, TasksIntersectionException {
        TaskManager manager = new InMemoryTasksManager();

        Epic epic1 = new Epic();
        Epic epic2 = new Epic();

        Subtask subtask1 = new Subtask(epic2);
        Subtask subtask2 = new Subtask(epic2);
        Subtask subtask5 = new Subtask(epic2);

        Assertions.assertThrows(TaskNotFoundException.class, () -> manager.removeEpicByUuid(epic1.getUuid()));
        manager.addEpic(epic1);
        manager.addEpicWithSubtask(epic2, subtask1, subtask2, subtask5);

        manager.getEpicByUuid(epic1.getUuid());
        manager.getEpicByUuid(epic2.getUuid());
        manager.getSubtaskByUuid(subtask2.getUuid());
        manager.getSubtaskByUuid(subtask5.getUuid());

        Assertions.assertEquals(4, manager.getHistory().size());

        manager.removeEpicByUuid(epic2.getUuid());

        Assertions.assertEquals(1, manager.getHistory().size());
    }
}