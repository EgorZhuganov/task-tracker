package com.ezh.taskbook.manager;

import com.ezh.taskbook.exception.TaskNotFoundException;
import com.ezh.taskbook.exception.TasksIntersectionException;
import com.ezh.taskbook.kvserver.KVServer;
import com.ezh.taskbook.task.Epic;
import com.ezh.taskbook.task.SingleTask;
import com.ezh.taskbook.task.Subtask;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;

class HttpTaskManagerTest extends TaskManagerTest<HttpTaskManager> {

    private KVServer kvServer; //port 8078
    private final URI serverUrl = URI.create("http://localhost:8078/");

    @BeforeEach
    public void beforeEach() throws IOException {
        kvServer = new KVServer(8078);
        kvServer.start();
        manager = new HttpTaskManager(serverUrl, "8080");
    }

    @AfterEach
    void afterEach() { kvServer.stop(); }

    @Test
    void test1loadFromServerIfAddOneTaskToHistoryShouldReturnSize1FromAnotherManager() throws TaskNotFoundException {
        Epic epic1 = new Epic();
        Epic epic2 = new Epic();
        manager.addEpic(epic1);
        manager.addEpic(epic2);
        manager.getEpicByUuid(epic1.getUuid());

        HttpTaskManager manager2 = new HttpTaskManager(serverUrl, "8080").loadFromServer(serverUrl, "8080");

        Assertions.assertEquals(manager.getHistory().size(), manager2.getHistory().size());
        Assertions.assertEquals(manager2.getEpicByUuid(epic1.getUuid()), manager2.getEpicByUuid(epic1.getUuid()));
    }

    @Test
    void test2getHistoryIfHistoryIsEmptyShouldReturnSize0() throws TasksIntersectionException {
        Epic epic1 = new Epic();
        Subtask subtask1 = new Subtask(epic1);
        SingleTask singleTask1 = new SingleTask();
        epic1.getSubtaskList().add(subtask1);

        manager.addSingleTask(singleTask1);
        manager.addEpicWithSubtask(epic1);

        HttpTaskManager manager = new HttpTaskManager(serverUrl, "8080").loadFromServer(serverUrl, "8080");
        Assertions.assertEquals(0, manager.getHistory().size());
    }

    @Test
    void test3getHistoryIfAdd3TasksToManagerAndGet3TasksHistoryNotEmptyShouldReturn3() throws TaskNotFoundException, TasksIntersectionException {
        Epic epic1 = new Epic();
        Subtask subtask1 = new Subtask(epic1);
        SingleTask singleTask1 = new SingleTask();

        epic1.getSubtaskList().add(subtask1);

        manager.addSingleTask(singleTask1);
        manager.addEpicWithSubtask(epic1);
        manager.getSingleTaskByUuid(singleTask1.getUuid());
        manager.getSubtaskByUuid(subtask1.getUuid());
        manager.getEpicByUuid(epic1.getUuid());

        HttpTaskManager manager = new HttpTaskManager(serverUrl, "8080").loadFromServer(serverUrl, "8080");
        Assertions.assertEquals(3, manager.getHistory().size());
    }

    @Test
    void test4getListTasksIfFileIsEmptyShouldReturn0() {
        HttpTaskManager manager = new HttpTaskManager(serverUrl, "8080").loadFromServer(serverUrl, "8080");
        Assertions.assertEquals(0, manager.getListEpics().size());
        Assertions.assertEquals(0, manager.getListSingleTasks().size());
        Assertions.assertEquals(0, manager.getListSubtasks().size());
    }

    @Test
    void test5getEpicWithoutAnySubtasks() throws TaskNotFoundException {
        Epic epic1 = new Epic();
        manager.addEpic(epic1);

        HttpTaskManager manager = new HttpTaskManager(serverUrl, "8080").loadFromServer(serverUrl, "8080");
        System.out.println(manager.getEpicByUuid(epic1.getUuid()));
    }

    @Test
    void test6loadManySubtasksAndOneEpicFromFile() throws TaskNotFoundException, TasksIntersectionException {
        Epic epic1 = new Epic();
        Subtask subtask1 = new Subtask(epic1);
        Subtask subtask2 = new Subtask(epic1);
        Subtask subtask3 = new Subtask(epic1);
        Subtask subtask4 = new Subtask(epic1);

        epic1.getSubtaskList().add(subtask1);
        epic1.getSubtaskList().add(subtask2);
        epic1.getSubtaskList().add(subtask3);
        epic1.getSubtaskList().add(subtask4);

        manager.addEpicWithSubtask(epic1);

        HttpTaskManager manager = new HttpTaskManager(serverUrl, "8080").loadFromServer(serverUrl, "8080");

        Assertions.assertEquals(epic1.getUuid(), manager.getEpicByUuid(epic1.getUuid()).getUuid());
    }
}