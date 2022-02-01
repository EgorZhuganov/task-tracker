package com.ezh.taskbook.manager;

import com.ezh.taskbook.task.AbstractTask;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {

    private Map<UUID, Node<AbstractTask>> historyLastTenTasks = new HashMap<>();
    static final int MAXIMUM_HISTORY_LENGTH = 10;
    private Node<AbstractTask> head;
    private Node<AbstractTask> tail;
    private int size = 0;

    @Override
    public void add(AbstractTask task) {
        linkLast(task);
        if (historyLastTenTasks.containsKey(task.getUuid()))
            remove(task.getUuid());
        if (historyLastTenTasks.isEmpty())
            historyLastTenTasks.put(task.getUuid(), head);
        else
            historyLastTenTasks.put(task.getUuid(), tail);
        cleanHistory();
    }

    private void cleanHistory() {
        if (historyLastTenTasks.size() > MAXIMUM_HISTORY_LENGTH) {
            remove(head.element.getUuid());
        }
    }

    @Override
    public void remove(UUID id) {
        if (historyLastTenTasks.containsKey(id)) {
            removeNode(historyLastTenTasks.get(id));
            historyLastTenTasks.remove(id);
        } else {
            throw new NoSuchElementException("This ID is not found in history");
        }
    }

    @Override
    public List<AbstractTask> getHistory() {
        List<AbstractTask> result = new ArrayList<>(size);
        int i = 0;
        for (Node<AbstractTask> x = head; x != null; x = x.next)
        result.add(i++, x.element);
        return result;
    }

    private void linkLast(AbstractTask element) {
        final Node<AbstractTask> oldTail = tail;
        final Node<AbstractTask> newNode = new Node<>(tail, element, null);
        tail = newNode;
        if (oldTail == null)
            head = newNode;
        else
            oldTail.next = newNode;
        size++;
    }

    private void removeNode(Node<AbstractTask> node) {
        final Node<AbstractTask> next = node.next;
        final Node<AbstractTask> prev = node.prev;

        if (prev == null) {
            head = next;
        } else {
            prev.next = next;
            node.prev = null;
        }

        if (next == null) {
            tail = prev;
        } else {
            next.prev = prev;
            node.next = null;
        }
        node.element = null;
        size--;
    }

    private static class Node<E> {
        E element;
        Node<E> next;
        Node<E> prev;

        Node(Node<E> prev, E element, Node<E> next) {
            this.element = element;
            this.next = next;
            this.prev = prev;
        }
    }
}
