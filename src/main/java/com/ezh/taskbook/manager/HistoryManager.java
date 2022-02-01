package com.ezh.taskbook.manager;

import com.ezh.taskbook.task.AbstractTask;

import java.util.List;
import java.util.UUID;

public interface HistoryManager {

    void add(AbstractTask task);

    void remove(UUID id);

    List<AbstractTask> getHistory();

}
