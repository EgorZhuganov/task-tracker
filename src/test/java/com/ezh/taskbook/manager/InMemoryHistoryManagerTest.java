package com.ezh.taskbook.manager;

import com.ezh.taskbook.task.Epic;
import com.ezh.taskbook.task.SingleTask;
import com.ezh.taskbook.task.Subtask;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

class InMemoryHistoryManagerTest {

    HistoryManager historyManager = new InMemoryHistoryManager();

    @Test
    void test1addIfAddTwoTheSameTasksShould1TaskInHistoryManager() {
        Epic epic1 = new Epic();
        Epic epic2 = new Epic();

        historyManager.add(epic1);
        historyManager.add(epic1);

        Assertions.assertEquals(1, historyManager.getHistory().size());
    }

    @Test
    void test2addIfAdd3TasksShouldAddTaskNumber3ToEndOfHistory() {
        Epic epic1 = new Epic();
        Epic epic2 = new Epic();
        Epic epic3 = new Epic();

        historyManager.add(epic1);
        historyManager.add(epic3);
        historyManager.add(epic2);

        Assertions.assertEquals(epic2.getUuid(), historyManager.getHistory().get(2).getUuid());
    }

    @Test
    void test3addIfAddOneTasksShouldReturnThisTaskFromHistory() {
        Epic epic1 = new Epic();

        historyManager.add(epic1);

        Assertions.assertEquals(1, historyManager.getHistory().size());
    }

    @Test /*and check that task number 3 will first task*/
    void test4addAdd12TasksShouldReturnFromHistory10Tasks() {
        SingleTask singleTask1 = new SingleTask();
        SingleTask singleTask2 = new SingleTask();

        Epic epic1 = new Epic();
        Epic epic2 = new Epic();

        Subtask subtask1 = new Subtask(epic1);
        Subtask subtask2 = new Subtask(epic1);
        Subtask subtask3 = new Subtask(epic1);
        Subtask subtask4 = new Subtask(epic2);
        Subtask subtask5 = new Subtask(epic2);
        Subtask subtask6 = new Subtask(epic2);
        Subtask subtask7 = new Subtask(epic2);
        Subtask subtask8 = new Subtask(epic2);

        historyManager.add(singleTask2);
        historyManager.add(epic1);
        historyManager.add(epic2);
        historyManager.add(singleTask1);
        historyManager.add(subtask3);
        historyManager.add(subtask4);
        historyManager.add(subtask7); //attention this task will use next
        historyManager.add(subtask1);
        historyManager.add(subtask2);
        historyManager.add(subtask8); //task number 10
        historyManager.add(subtask5);
        historyManager.add(subtask7); //it uses here, the previous record was overwritten
        historyManager.add(subtask6);

        Assertions.assertEquals(10, historyManager.getHistory().size());
        Assertions.assertEquals(epic2.getUuid(), historyManager.getHistory().get(0).getUuid());
    }

    @Test
    void test5removeAdd3TasksRemoveFirstShouldReturnSecondHowFirstTask() {
        Epic epic1 = new Epic();
        Epic epic2 = new Epic();
        SingleTask singleTask1 = new SingleTask();

        historyManager.add(epic1);
        historyManager.add(epic2);
        historyManager.add(singleTask1);

        historyManager.remove(epic1.getUuid());
        Assertions.assertEquals(epic2.getUuid(), historyManager.getHistory().get(0).getUuid());
    }

    @Test
    void test6removeAdd3TasksRemoveSecondShouldReturnThirdHowSecondTask() {
        Epic epic1 = new Epic();
        Epic epic2 = new Epic();
        SingleTask singleTask1 = new SingleTask();

        historyManager.add(epic1);
        historyManager.add(epic2);
        historyManager.add(singleTask1);

        historyManager.remove(epic2.getUuid());
        Assertions.assertEquals(singleTask1.getUuid(), historyManager.getHistory().get(1).getUuid());
    }

    @Test
    void test7removeAdd3TasksRemoveThirdShouldReturnHistorySizeEquals2() {
        Epic epic1 = new Epic();
        Epic epic2 = new Epic();
        SingleTask singleTask1 = new SingleTask();

        historyManager.add(epic1);
        historyManager.add(epic2);
        historyManager.add(singleTask1);

        historyManager.remove(singleTask1.getUuid());
        Assertions.assertEquals(2, historyManager.getHistory().size());
    }

    @Test
    void test8getHistoryAdd0TasksShouldReturnSizeHistoryEquals0() {
        SingleTask singleTask1 = new SingleTask();
        SingleTask singleTask2 = new SingleTask();
        Epic epic1 = new Epic();
        Assertions.assertEquals(0, historyManager.getHistory().size());
    }

    @Test /* after remove singleTask2 and epic1 (two first tasks) expected that next task will first and order will save
     ** will use duplicate tasks */
    void test9getHistoryAdd12TasksShouldReturn10TasksInRightOrder() {
        SingleTask singleTask1 = new SingleTask();
        SingleTask singleTask2 = new SingleTask();

        Epic epic1 = new Epic();
        Epic epic2 = new Epic();

        Subtask subtask1 = new Subtask(epic1);
        Subtask subtask2 = new Subtask(epic1);
        Subtask subtask3 = new Subtask(epic1);
        Subtask subtask4 = new Subtask(epic2);
        Subtask subtask5 = new Subtask(epic2);
        Subtask subtask6 = new Subtask(epic2);
        Subtask subtask7 = new Subtask(epic2);
        Subtask subtask8 = new Subtask(epic2);

        historyManager.add(singleTask2);
        historyManager.add(epic1);
        historyManager.add(epic2);
        historyManager.add(singleTask1);
        historyManager.add(subtask3);
        historyManager.add(subtask4);
        historyManager.add(subtask7); //this task uses twice...here and
        historyManager.add(subtask1);
        historyManager.add(subtask2);
        historyManager.add(subtask8); //task number 10
        historyManager.add(subtask5);
        historyManager.add(subtask7); //here
        historyManager.add(subtask6);

        Assertions.assertEquals(historyManager.getHistory().get(0).getUuid(), epic2.getUuid());
        Assertions.assertEquals(historyManager.getHistory().get(3).getUuid(), subtask4.getUuid());
        Assertions.assertEquals(historyManager.getHistory().get(8).getUuid(), subtask7.getUuid());
    }

    @Test
    void test10asStringAdd5TasksShouldReturnUuidOwnTheseTasks() {
        SingleTask singleTask1 = new SingleTask();
        Epic epic1 = new Epic();
        Epic epic2 = new Epic();
        Subtask subtask1 = new Subtask(epic1);
        Subtask subtask2 = new Subtask(epic2);

        historyManager.add(singleTask1);
        historyManager.add(epic1);
        historyManager.add(epic2);
        historyManager.add(subtask1);
        historyManager.add(subtask2);

        String str = HistoryManager.asString(historyManager);
        String[] uuids = str.split(";");
        Assertions.assertEquals(singleTask1.getUuid().toString(), uuids[0]);
        Assertions.assertEquals(subtask2.getUuid().toString(), uuids[4]);
    }

    @Test
    void test11fromStringAdd5TasksShouldReturnUuidOwnTheseTasks() {
        SingleTask singleTask1 = new SingleTask();
        Epic epic1 = new Epic();
        Epic epic2 = new Epic();
        Subtask subtask1 = new Subtask(epic1);
        Subtask subtask2 = new Subtask(epic2);

        historyManager.add(singleTask1);
        historyManager.add(epic1);
        historyManager.add(epic2);
        historyManager.add(subtask1);
        historyManager.add(subtask2);

        String str = HistoryManager.asString(historyManager);
        List <UUID> uuids = new ArrayList<>(HistoryManager.fromString(str));
        Assertions.assertEquals(singleTask1.getUuid(), uuids.get(0));
        Assertions.assertEquals(subtask2.getUuid(), uuids.get(4));
    }
}