package com.ezh.taskbook.manager;

import com.ezh.taskbook.task.AbstractTask;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public interface HistoryManager {

    void add(AbstractTask task);

    void remove(UUID id);

    List<AbstractTask> getHistory();

    static String asString(HistoryManager manager) {
        StringBuilder sb = new StringBuilder();
        manager.getHistory().forEach(abstractTask -> sb.append(abstractTask.getUuid().toString()).append(";"));
        return sb.toString();
    }

    static List<UUID> fromString(String value){
        List<UUID> uuidList = new ArrayList<>();
        String[] uuidFields = value.split(";");
        for (String uuid : uuidFields) {
            uuidList.add(UUID.fromString(uuid));
        }
        return uuidList;
    }
}
