package com.ezh.taskbook.task;

import com.ezh.taskbook.manager.InMemoryTasksManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class EpicTest {
    InMemoryTasksManager manager = new InMemoryTasksManager();

    @Test
    void getStatus() {
        Epic epic1 = new Epic();
        Subtask subtask1 = new Subtask(epic1);
        Subtask subtask2 = new Subtask(epic1);
        Subtask subtask3 = new Subtask(epic1);
        Subtask subtask4 = new Subtask(epic1);

        manager.addEpic(epic1);
        Assertions.assertEquals(StatusTask.NEW, epic1.getStatus()); //without subtasks

        manager.addSubtaskInAddedEpic(epic1, subtask1);
        Assertions.assertEquals(StatusTask.NEW, epic1.getStatus()); //with subtask status NEW

        subtask2.setStatus(StatusTask.DONE);
        manager.changeSubtaskByUuid(subtask1.getUuid(), subtask2);
        Assertions.assertEquals(StatusTask.DONE, epic1.getStatus()); //with subtask changed from NEW to DONE

        manager.addSubtaskInAddedEpic(epic1, subtask3);
        Assertions.assertEquals(StatusTask.IN_PROGRESS, epic1.getStatus()); // if add new subtask with status NEW

        subtask4.setStatus(StatusTask.DONE);
        manager.changeSubtaskByUuid(subtask3.getUuid(), subtask4);
        Assertions.assertEquals(StatusTask.DONE, epic1.getStatus()); //if change status subtask, status Epic also change

        Epic epic2 = new Epic();
        Subtask subtask6 = new Subtask(epic2);
        Subtask subtask7 = new Subtask(epic2);
        manager.addEpicWithSubtask(epic2, subtask6, subtask7);
        subtask6.setStatus(StatusTask.IN_PROGRESS);
        //if change status subtask, status Epic also change
        Assertions.assertEquals(StatusTask.IN_PROGRESS, epic2.getStatus());

        Epic epic3 = new Epic();
        Subtask subtask8 = new Subtask(epic3);
        Subtask subtask9 = new Subtask(epic3);
        subtask8.setStatus(StatusTask.DONE);
        subtask9.setStatus(StatusTask.IN_PROGRESS);

        manager.addEpicWithSubtask(epic3, subtask8, subtask9);
        Assertions.assertEquals(StatusTask.IN_PROGRESS, epic3.getStatus());

        manager.changeEpicByUuid(epic2.getUuid(), epic3);
        Assertions.assertEquals(StatusTask.IN_PROGRESS, epic3.getStatus());
    }
}