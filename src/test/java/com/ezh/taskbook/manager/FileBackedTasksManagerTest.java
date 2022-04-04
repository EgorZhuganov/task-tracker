package com.ezh.taskbook.manager;

import com.ezh.taskbook.exception.TaskNotFoundException;
import com.ezh.taskbook.exception.TasksIntersectionException;
import com.ezh.taskbook.task.Epic;
import com.ezh.taskbook.task.SingleTask;
import com.ezh.taskbook.task.Subtask;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager> {

    @BeforeEach
    void setup() {
        File file = new File("test.txt");
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        assert writer != null;
        writer.print("");
        writer.close();
        manager = new FileBackedTasksManager(file);
    }

    @Test
    void test1_getHistoryIfHistoryIsEmptyShouldReturnSize0() throws TasksIntersectionException {
        Epic epic1 = new Epic();
        Subtask subtask1 = new Subtask(epic1);
        SingleTask singleTask1 = new SingleTask();

        manager.addSingleTask(singleTask1);
        manager.addEpicWithSubtask(epic1, subtask1);

        FileBackedTasksManager manager = FileBackedTasksManager.loadFromFile(new File("test.txt"));
        Assertions.assertEquals(0, manager.getHistory().size());
    }

    @Test
    void test2_getHistoryIfAdd3TasksToManagerAndGet3TasksHistoryNotEmptyShouldReturn3() throws TaskNotFoundException, TasksIntersectionException {
        Epic epic1 = new Epic();
        Subtask subtask1 = new Subtask(epic1);
        SingleTask singleTask1 = new SingleTask();

        manager.addSingleTask(singleTask1);
        manager.addEpicWithSubtask(epic1, subtask1);
        manager.getSingleTaskByUuid(singleTask1.getUuid());
        manager.getSubtaskByUuid(subtask1.getUuid());
        manager.getEpicByUuid(epic1.getUuid());
        FileBackedTasksManager manager = FileBackedTasksManager.loadFromFile(new File("test.txt"));
        Assertions.assertEquals(3, manager.getHistory().size());
    }

    @Test
    void test3_getListTasksIfFileIsEmptyShouldReturn0() {
        FileBackedTasksManager manager = FileBackedTasksManager.loadFromFile(new File("test.txt"));
        Assertions.assertEquals(0, manager.getListEpics().size());
        Assertions.assertEquals(0, manager.getListSingleTasks().size());
        Assertions.assertEquals(0, manager.getListSubtasks().size());
    }

    @Test
    void test4_getEpicWithoutAnySubtasks() throws TaskNotFoundException {
        Epic epic1 = new Epic();
        manager.addEpic(epic1);
        FileBackedTasksManager manager = FileBackedTasksManager.loadFromFile(new File("test.txt"));
        System.out.println(manager.getEpicByUuid(epic1.getUuid()));
    }

    @Test /*should that subtask will add in tempStorage in method loadFromFile and then will get from it and add to
    own subtask*/
    void test4_loadManySubtasksAndOneEpicFromFile() throws TaskNotFoundException, TasksIntersectionException {
        Epic epic1 = new Epic();
        Subtask subtask1 = new Subtask(epic1);
        Subtask subtask2 = new Subtask(epic1);
        Subtask subtask3 = new Subtask(epic1);
        Subtask subtask4 = new Subtask(epic1);

        manager.addEpicWithSubtask(epic1, subtask1, subtask2, subtask3, subtask4);

        FileBackedTasksManager manager = FileBackedTasksManager.loadFromFile(new File("test.txt"));

        Assertions.assertEquals(epic1.getUuid(), manager.getEpicByUuid(epic1.getUuid()).getUuid());
    }
}