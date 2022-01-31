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
        Epic epic2 = new Epic();
        Epic epic3 = new Epic();

        epic1.setName("FirstEpic");
        epic2.setName("SecondEpic");
        epic3.setName("ThirdEpic");

        manager.addEpic(epic1);
        manager.addEpic(epic2);
        manager.addEpic(epic3);

        manager.getEpicByUuid(epic1.getUuid());
        manager.getEpicByUuid(epic2.getUuid());
        manager.getEpicByUuid(epic3.getUuid());
        manager.getEpicByUuid(epic1.getUuid());
        manager.getEpicByUuid(epic2.getUuid());

    }

    @Test
    void remove() {
        Epic epic1 = new Epic();
        Epic epic2 = new Epic();
        Epic epic3 = new Epic();
        Epic epic4 = new Epic();

        epic1.setName("FirstEpic");
        epic2.setName("SecondEpic");
        epic3.setName("ThirdEpic");
        epic4.setName("FourthEpic");

        manager.addEpic(epic1);
        manager.addEpic(epic2);
        manager.addEpic(epic3);
        manager.addEpic(epic4);

        manager.getEpicByUuid(epic1.getUuid());
        manager.getEpicByUuid(epic2.getUuid());
        manager.getEpicByUuid(epic3.getUuid());
        manager.getEpicByUuid(epic4.getUuid());

        manager.removeTaskFromHistory(epic2.getUuid());

        Assertions.assertEquals(3, manager.getHistory().size());
        System.out.println("Excellent");
    }

    @Test
    void getHistory() {
        Epic epic1 = new Epic();
        Epic epic2 = new Epic();
        Epic epic3 = new Epic();
        Epic epic4 = new Epic();

        epic1.setName("FirstEpic");
        epic2.setName("SecondEpic");
        epic3.setName("ThirdEpic");
        epic4.setName("FourthEpic");

        manager.addEpic(epic1);
        manager.addEpic(epic2);
        manager.addEpic(epic3);
        manager.addEpic(epic4);

        manager.getEpicByUuid(epic1.getUuid());
        manager.getEpicByUuid(epic2.getUuid());
        manager.getEpicByUuid(epic3.getUuid());
        manager.getEpicByUuid(epic4.getUuid());
        manager.getEpicByUuid(epic1.getUuid());

        Assertions.assertEquals(4, manager.getHistory().size());

        System.out.println(manager.getHistory());
        System.out.println("Excellent");
    }

    @Test
    void cleanHistory() { //в тест попадает 12 тасок, первые две должны удалиться, т.к. max - 10
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
        manager.getEpicByUuid(epic2.getUuid()); //таска уйдет из списка просмотров, т.к. она первая с головы (эпик 1 не первый - т.к. был повторный вызов)
        manager.getSingleTaskByUuid(singleTask1.getUuid()); //становится первой с головы и тоже уходит из списка
        manager.getSingleTaskByUuid(singleTask2.getUuid());
        manager.getEpicByUuid(epic1.getUuid()); //повторный вызов эпик1, первый вызов удаляется и переносится
        manager.getSubtaskByUuid(subtask2.getUuid());
        manager.getSubtaskByUuid(subtask1.getUuid());
        manager.getSubtaskByUuid(subtask3.getUuid());
        manager.getSubtaskByUuid(subtask4.getUuid());
        manager.getSubtaskByUuid(subtask7.getUuid());
        manager.getSubtaskByUuid(subtask8.getUuid());
        manager.getSubtaskByUuid(subtask5.getUuid());
        manager.getSubtaskByUuid(subtask6.getUuid());

        Assertions.assertEquals(10, manager.getHistory().size());
        System.out.println(manager.getHistory());
    }
}