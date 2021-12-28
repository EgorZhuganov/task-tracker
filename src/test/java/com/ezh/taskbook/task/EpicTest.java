package test.java.com.ezh.taskbook.task;

import main.java.com.ezh.taskbook.manager.Manager;
import main.java.com.ezh.taskbook.task.Epic;
import main.java.com.ezh.taskbook.task.StatusTask;
import main.java.com.ezh.taskbook.task.Subtask;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {
    Manager manager = new Manager();

    @Test
    void getStatus() {
        Epic epic1 = new Epic();
        Subtask subtask1 = new Subtask();
        Subtask subtask2 = new Subtask();
        Subtask subtask3 = new Subtask();
        Subtask subtask4 = new Subtask();

        manager.addEpic(epic1);
        Assertions.assertEquals(StatusTask.NEW, epic1.getStatus()); //without subtasks

        manager.addSubtaskInCreatedEpic(epic1, subtask1);
        Assertions.assertEquals(StatusTask.NEW, epic1.getStatus()); //with subtask status NEW

        subtask2.setStatus(StatusTask.DONE);
        manager.changeSubtaskByUuid(subtask1.getUuid(), subtask2);
        Assertions.assertEquals(StatusTask.DONE, epic1.getStatus()); //with subtask changed from NEW to DONE

        manager.addSubtaskInCreatedEpic(epic1, subtask3);
        Assertions.assertEquals(StatusTask.IN_PROGRESS, epic1.getStatus()); // if add new subtask with status NEW

        subtask4.setStatus(StatusTask.DONE);
        manager.changeSubtaskByUuid(subtask3.getUuid(), subtask4);
        Assertions.assertEquals(StatusTask.DONE, epic1.getStatus()); //if change status subtask, status Epic also change

        Epic epic2 = new Epic();
        Subtask subtask6 = new Subtask();
        Subtask subtask7 = new Subtask();
        manager.addEpicWithSubtask(epic2, subtask6, subtask7);
        subtask6.setStatus(StatusTask.IN_PROGRESS);
        Assertions.assertEquals(StatusTask.IN_PROGRESS, epic2.getStatus()); //if change status subtask, status Epic also change

        Epic epic3 = new Epic();
        Subtask subtask8 = new Subtask();
        Subtask subtask9 = new Subtask();
        subtask8.setStatus(StatusTask.DONE);
        subtask9.setStatus(StatusTask.IN_PROGRESS);

        manager.addEpicWithSubtask(epic3, subtask8, subtask9);
        Assertions.assertEquals(StatusTask.IN_PROGRESS, epic3.getStatus());

        manager.changeEpicByUuid(epic2.getUuid(), epic3);
        Assertions.assertEquals(StatusTask.IN_PROGRESS, epic3.getStatus());
    }
}