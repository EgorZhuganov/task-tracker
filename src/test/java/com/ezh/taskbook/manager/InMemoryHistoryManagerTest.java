package com.ezh.taskbook.manager;

import com.ezh.taskbook.task.Epic;
import com.ezh.taskbook.task.SingleTask;
import com.ezh.taskbook.task.Subtask;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class InMemoryHistoryManagerTest {

    TaskManager manager = new Managers().getDefault();
    @Test
    void add() {
        Epic epic1 = new Epic();
        epic1.setName("FirstEpic");
        manager.addEpic(epic1);
        manager.getEpicByUuid(epic1.getUuid());
        Assertions.assertEquals(1, manager.getHistoryManager().getHistory().size());
        System.out.println(manager.getHistoryManager().getHistory());

    }

    @Test
    void remove() {
        Epic epic1 = new Epic();
        Epic epic2 = new Epic();
        Epic epic3 = new Epic();

        Subtask subtask1 = new Subtask();
        Subtask subtask2 = new Subtask();
        Subtask subtask3 = new Subtask();

        epic1.setName("FirstEpic");
        epic2.setName("SecondEpicWithSub");
        epic3.setName("ThirdEpic");

        subtask1.setName("FirstSub");
        subtask2.setName("SecondSub");
        subtask3.setName("ThirdSub");

        manager.addEpic(epic1);
        manager.addEpicWithSubtask(epic2, subtask1, subtask2, subtask3);
        manager.addEpic(epic3);

        manager.getEpicByUuid(epic1.getUuid());
        manager.getEpicByUuid(epic2.getUuid());
        manager.getEpicByUuid(epic3.getUuid());

        manager.getHistoryManager().remove(epic2.getUuid());

        manager.getSubtaskByUuid(subtask2.getUuid());

        Assertions.assertEquals(3, manager.getHistoryManager().getHistory().size());
        System.out.println(manager.getHistoryManager().getHistory());
    }

    @Test
    void getHistory() {
        Epic epic1 = new Epic();
        Epic epic2 = new Epic();
        SingleTask singleTask1 = new SingleTask();
        SingleTask singleTask2 = new SingleTask();
        Subtask subtask1 = new Subtask();
        Subtask subtask2 = new Subtask();
        Subtask subtask3 = new Subtask();

        epic1.setName("FirstEpicWithoutSub");
        epic2.setName("SecondEpicWithSub");
        singleTask1.setName("FirstSingleTask");
        singleTask2.setName("SecondSingleTask");
        subtask1.setName("FirstSub");
        subtask2.setName("SecondSub");
        subtask3.setName("ThirdSub");

        manager.addEpic(epic1);
        manager.addEpicWithSubtask(epic2, subtask1, subtask2, subtask3);
        manager.addSingleTask(singleTask1);
        manager.addSingleTask(singleTask2);

        manager.getEpicByUuid(epic1.getUuid());
        manager.getEpicByUuid(epic2.getUuid());
        manager.getSingleTaskByUuid(singleTask1.getUuid());
        manager.getSingleTaskByUuid(singleTask2.getUuid());
        manager.getEpicByUuid(epic1.getUuid());
        manager.getSubtaskByUuid(subtask2.getUuid());
        manager.getSubtaskByUuid(subtask1.getUuid());
        manager.getSubtaskByUuid(subtask3.getUuid());

        Assertions.assertEquals(7, manager.getHistoryManager().getHistory().size());
        System.out.println(manager.getHistoryManager().getHistory());
    }

    @Test
    void cleanHistory() {
        Epic epic1 = new Epic();
        Epic epic2 = new Epic();
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

        epic1.setName("FirstEpicWithoutSub");
        epic2.setName("SecondEpicWithSub");
        singleTask1.setName("FirstSingleTask");
        singleTask2.setName("SecondSingleTask");
        subtask1.setName("FirstSub");
        subtask2.setName("SecondSub");
        subtask3.setName("ThirdSub");
        subtask4.setName("FourthSub");
        subtask5.setName("FifthSub");
        subtask6.setName("SixthSub");
        subtask7.setName("SeventhSub");
        subtask8.setName("EighthSub");

        manager.addEpicWithSubtask(epic1, subtask4, subtask5, subtask6, subtask7);
        manager.addEpicWithSubtask(epic2, subtask1, subtask2, subtask3, subtask8);
        manager.addSingleTask(singleTask1);
        manager.addSingleTask(singleTask2);

        manager.getEpicByUuid(epic1.getUuid());
        manager.getEpicByUuid(epic2.getUuid());
        manager.getSingleTaskByUuid(singleTask1.getUuid());
        manager.getSingleTaskByUuid(singleTask2.getUuid());
        manager.getEpicByUuid(epic1.getUuid());
        manager.getSubtaskByUuid(subtask2.getUuid());
        manager.getSubtaskByUuid(subtask1.getUuid());
        manager.getSubtaskByUuid(subtask3.getUuid());
        manager.getSubtaskByUuid(subtask4.getUuid());
        manager.getSubtaskByUuid(subtask7.getUuid());
        manager.getSubtaskByUuid(subtask8.getUuid());
        manager.getSubtaskByUuid(subtask5.getUuid());
        manager.getSubtaskByUuid(subtask6.getUuid());

        Assertions.assertEquals(10, manager.getHistoryManager().getHistory().size());
        System.out.println(manager.getHistoryManager().getHistory());


    }
}