package com.ezh.taskbook.manager;

import com.ezh.taskbook.task.AbstractTask;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {

    private Map<UUID, AbstractTask> historyLastTenTasks = new LinkedHashMap<>();
    static final int MAXIMUM_HISTORY_LENGTH = 10;

    @Override
    public void add(AbstractTask task) {
        if (historyLastTenTasks.containsKey(task.getUuid())) {
            remove(task.getUuid());
        }
        historyLastTenTasks.put(task.getUuid(), task);
    }

    @Override
    public void remove(UUID id) {
        if (historyLastTenTasks.containsKey(id)) {
            historyLastTenTasks.remove(id);
        } else {
            throw new RuntimeException("This ID is not found in history");
        }
    }

    @Override
    public List<AbstractTask> getHistory() {
        return new ArrayList<>(historyLastTenTasks.values());
    }

    @Override
    public void cleanHistory(){
        if (historyLastTenTasks.size() > MAXIMUM_HISTORY_LENGTH) {
            historyLastTenTasks = historyLastTenTasks.
                    entrySet().
                    stream().
                    limit(MAXIMUM_HISTORY_LENGTH).
                    collect(LinkedHashMap::new,                                      // Supplier
                            (map, task) -> map.put(task.getKey(), task.getValue()),  // Accumulator
                            Map::putAll);                                            // Combiner
        }
    }
}
