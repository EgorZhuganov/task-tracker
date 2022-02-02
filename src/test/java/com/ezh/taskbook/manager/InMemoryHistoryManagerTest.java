package com.ezh.taskbook.manager;

import com.ezh.taskbook.task.Epic;
import com.ezh.taskbook.task.SingleTask;
import com.ezh.taskbook.task.Subtask;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class InMemoryHistoryManagerTest {

    @Test
    void add() {

        InMemoryHistoryManager historyManager = new InMemoryHistoryManager();

        Epic epic1 = new Epic();
        Epic epic2 = new Epic();
        Epic epic3 = new Epic();

        historyManager.add(epic1);
        historyManager.add(epic3);
        historyManager.add(epic2);

        Assertions.assertEquals(3, historyManager.getHistory().size());
    }

    @Test
    void remove() {

        InMemoryHistoryManager historyManager = new InMemoryHistoryManager();

        Epic epic1 = new Epic();
        Epic epic2 = new Epic();
        Epic epic3 = new Epic();
        Epic epic4 = new Epic();

        historyManager.add(epic1);
        historyManager.add(epic2);
        historyManager.add(epic3);
        historyManager.add(epic4);

        historyManager.remove(epic2.getUuid());
        Assertions.assertEquals(3, historyManager.getHistory().size());
    }

    @Test
    void getHistory() {

        InMemoryHistoryManager historyManager = new InMemoryHistoryManager();

        SingleTask singleTask1 = new SingleTask();
        SingleTask singleTask2 = new SingleTask();

        Subtask subtask1 = new Subtask();
        Subtask subtask2 = new Subtask();
        Subtask subtask3 = new Subtask();
        Subtask subtask4 = new Subtask();
        Subtask subtask5 = new Subtask();
        Subtask subtask6 = new Subtask();
        Subtask subtask7 = new Subtask();
        Subtask subtask8 = new Subtask();

        Epic epic1 = new Epic();
        Epic epic2 = new Epic();

        historyManager.add(singleTask2);
        historyManager.add(epic1);
        historyManager.add(epic2);
        historyManager.add(singleTask1);
        historyManager.add(subtask3);
        historyManager.add(subtask4);
        historyManager.add(subtask7);
        historyManager.add(subtask1);
        historyManager.add(subtask2);
        historyManager.add(subtask8);
        historyManager.add(subtask5);
        historyManager.add(subtask7);
        historyManager.add(subtask6);

        Assertions.assertEquals(10, historyManager.getHistory().size());
        Assertions.assertEquals(historyManager.getHistory().get(0).getUuid(), epic2.getUuid());
        Assertions.assertEquals(historyManager.getHistory().get(8).getUuid(), subtask7.getUuid());

    }
}