package com.ezh.taskbook.manager;

import com.ezh.taskbook.task.AbstractTask;
import com.ezh.taskbook.task.Epic;
import com.ezh.taskbook.task.SingleTask;
import com.ezh.taskbook.task.Subtask;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

class InMemoryHistoryManagerTest {

    @Test
    void add() throws IllegalAccessException, NoSuchFieldException {

        InMemoryHistoryManager historyManager = new InMemoryHistoryManager();
        Class<? extends InMemoryHistoryManager> ihm = historyManager.getClass();
        Field field = ihm.getDeclaredField("historyLastTenTasks");
        field.setAccessible(true);
        Map<?, ?> hashMap = (HashMap<?, ?>) field.get(historyManager);

        Epic epic1 = new Epic();
        Epic epic2 = new Epic();
        Epic epic3 = new Epic();

        historyManager.add(epic1);
        historyManager.add(epic3);
        historyManager.add(epic2);

        Assertions.assertEquals(3, hashMap.size());
    }

    @Test
    void remove() throws NoSuchFieldException, IllegalAccessException {

        InMemoryHistoryManager historyManager = new InMemoryHistoryManager();
        Class<? extends InMemoryHistoryManager> ihm = historyManager.getClass();
        Field field = ihm.getDeclaredField("historyLastTenTasks");
        field.setAccessible(true);
        Map<?, ?> hashMap = (HashMap<?, ?>) field.get(historyManager);

        Epic epic1 = new Epic();
        Epic epic2 = new Epic();
        Epic epic3 = new Epic();
        Epic epic4 = new Epic();

        historyManager.add(epic1);
        historyManager.add(epic2);
        historyManager.add(epic3);
        historyManager.add(epic4);

        historyManager.remove(epic2.getUuid());

        Assertions.assertEquals(3, hashMap.size());
        Assertions.assertEquals(3, historyManager.getHistory().size());
    }

    @Test
    void removeNode() throws NoSuchFieldException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {

        InMemoryHistoryManager historyManager = new InMemoryHistoryManager();
        Class<? extends InMemoryHistoryManager> ihm = historyManager.getClass();
        Class<?> node = InMemoryHistoryManager.class.getDeclaredClasses()[0];

        Method methodRemoveNode = historyManager.getClass().getDeclaredMethod("removeNode", node);
        var tail = ihm.getDeclaredField("tail");
        var head = ihm.getDeclaredField("head");
        Method methodLinkLast = historyManager.getClass().getDeclaredMethod("linkLast", AbstractTask.class);

        tail.setAccessible(true);
        head.setAccessible(true);
        methodRemoveNode.setAccessible(true);
        methodLinkLast.setAccessible(true);

        Epic epic1 = new Epic();
        Epic epic2 = new Epic();
        Epic epic3 = new Epic();
        Epic epic4 = new Epic();

        methodLinkLast.invoke(historyManager, epic3);
        methodLinkLast.invoke(historyManager, epic4);
        methodLinkLast.invoke(historyManager, epic2);
        methodLinkLast.invoke(historyManager, epic1);

        var tailNode = tail.get(historyManager);
        var prevTailNode = tailNode.getClass().getDeclaredField("prev");
        var nextTailNode = tailNode.getClass().getDeclaredField("next");

        var headNode = head.get(historyManager);
        var prevHeadNode = headNode.getClass().getDeclaredField("prev");
        var nextHeadNode = headNode.getClass().getDeclaredField("next");

        var prevNode = prevTailNode.get(tailNode);
        var nextNode = nextHeadNode.get(headNode);

        methodRemoveNode.invoke(historyManager, prevNode);
        methodRemoveNode.invoke(historyManager, nextNode);

        Assertions.assertNull(nextTailNode.get(tailNode));
        Assertions.assertNull(prevHeadNode.get(headNode));
        Assertions.assertEquals(headNode, prevTailNode.get(tailNode));

    }

    @Test
    void linkLast() throws
            NoSuchMethodException, InvocationTargetException, IllegalAccessException, NoSuchFieldException {

        InMemoryHistoryManager historyManager = new InMemoryHistoryManager();
        Class<? extends InMemoryHistoryManager> ihm = historyManager.getClass();

        Method methodLinkLast = historyManager.getClass().getDeclaredMethod("linkLast", AbstractTask.class);
        Field tail = ihm.getDeclaredField("tail");
        Field head = ihm.getDeclaredField("head");

        tail.setAccessible(true);
        head.setAccessible(true);
        methodLinkLast.setAccessible(true);

        Epic epic1 = new Epic();
        Epic epic2 = new Epic();
        Epic epic3 = new Epic();

        Assertions.assertNull(tail.get(historyManager));
        Assertions.assertNull(head.get(historyManager));

        methodLinkLast.invoke(historyManager, epic1);
        methodLinkLast.invoke(historyManager, epic3);
        methodLinkLast.invoke(historyManager, epic2);
        methodLinkLast.invoke(historyManager, epic1);

        Assertions.assertNotNull(tail.get(historyManager));
        Assertions.assertNotNull(head.get(historyManager));

        var tailNode = tail.get(historyManager);
        var tailElement = tailNode.getClass().getDeclaredField("element");
        var prevTailNode = tailNode.getClass().getDeclaredField("prev");
        var nextTailNode = tailNode.getClass().getDeclaredField("next");

        var headNode = head.get(historyManager);
        var headElement = headNode.getClass().getDeclaredField("element");
        var prevHeadNode = headNode.getClass().getDeclaredField("prev");
        var nextHeadNode = headNode.getClass().getDeclaredField("next");

        var firstTask = (AbstractTask) headElement.get(headNode);
        var lastTask = (AbstractTask) tailElement.get(tailNode);

        var prevNode = prevTailNode.get(tailNode);
        var prevFieldElement = prevNode.getClass().getDeclaredField("element");
        var prevElement = (AbstractTask) prevFieldElement.get(prevNode);

        var nextNode = nextHeadNode.get(headNode);
        var nextFieldElement = headNode.getClass().getDeclaredField("element");
        var nextElement = (AbstractTask) nextFieldElement.get(nextNode);

        Assertions.assertNull(nextTailNode.get(tailNode));
        Assertions.assertNull(prevHeadNode.get(headNode));
        Assertions.assertEquals(epic1.getUuid(), firstTask.getUuid());
        Assertions.assertEquals(epic1.getUuid(), lastTask.getUuid());
        Assertions.assertEquals(epic2.getUuid(), prevElement.getUuid());
        Assertions.assertEquals(epic3.getUuid(), nextElement.getUuid());

        methodLinkLast.invoke(historyManager, epic3);   //look change or not tail
        var tailNodeAfterFirstCheck = tail.get(historyManager);
        var tailElementAfterFirstCheck = tailNodeAfterFirstCheck.getClass().getDeclaredField("element");
        tailElementAfterFirstCheck.setAccessible(true);
        var lastTask2 = (AbstractTask) tailElementAfterFirstCheck.get(tailNodeAfterFirstCheck);
        Assertions.assertEquals(epic3.getUuid(), lastTask2.getUuid());
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

    @Test
    void cleanHistory() throws NoSuchFieldException, IllegalAccessException {

        InMemoryHistoryManager historyManager = new InMemoryHistoryManager();
        Class<? extends InMemoryHistoryManager> ihm = historyManager.getClass();
        Field field = ihm.getDeclaredField("historyLastTenTasks");
        field.setAccessible(true);
        Map<?, ?> hashMap = (HashMap<?, ?>) field.get(historyManager);

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

        historyManager.add(epic1);
        historyManager.add(epic2);
        historyManager.add(singleTask2);
        historyManager.add(singleTask1);

        historyManager.add(subtask8);
        historyManager.add(subtask7);
        historyManager.add(subtask6);
        historyManager.add(subtask5); //call two time
        historyManager.add(subtask4); //call two time
        historyManager.add(subtask3);
        historyManager.add(subtask2);
        historyManager.add(subtask1);
        historyManager.add(subtask4);
        historyManager.add(subtask5);

        Assertions.assertEquals(10, hashMap.size()); //call all tasks (12), but size always 10
        Assertions.assertNull(hashMap.get(epic1));
        Assertions.assertNull(hashMap.get(epic2));
    }
}