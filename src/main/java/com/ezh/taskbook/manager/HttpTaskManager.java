package com.ezh.taskbook.manager;

import com.ezh.taskbook.task.AbstractTask;
import com.ezh.taskbook.task.taskSerializers.jsonAdapter.PropertyMarshallerOfObject;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.net.URI;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class HttpTaskManager extends FileBackedTasksManager {

    private KVTaskClient client;
    private final String storageKey;
    private final String historyKey;

    public HttpTaskManager(URI urlKvServer, String keyStoragePrefix) {
        storageKey = keyStoragePrefix + "STORAGE_KEY";
        historyKey = keyStoragePrefix + "HISTORY_KEY";
        client = new KVTaskClient(urlKvServer);
    }

    @Override
    protected void save() {
        client.put(storageKey, new GsonBuilder()
                .registerTypeAdapter(AbstractTask.class, new PropertyMarshallerOfObject())
                .create()
                .toJson(storage.values(), new TypeToken<List<AbstractTask>>() {
                }.getType()));

        client.put(historyKey, new Gson()
                .toJson(inMemoryHistoryManager.getHistory()
                        .stream()
                        .map(AbstractTask::getUuid)
                        .collect(Collectors.toList()))
        );
    }

    public HttpTaskManager loadFromServer(URI urlKvServer, String keyStoragePrefix) {
        HttpTaskManager manager = new HttpTaskManager(urlKvServer, keyStoragePrefix);
        List<AbstractTask> reloadStorage = new GsonBuilder()
                .registerTypeAdapter(AbstractTask.class, new PropertyMarshallerOfObject())
                .create()
                .fromJson(client.load(storageKey), new TypeToken<List<AbstractTask>>() {
                }.getType());
        List<UUID> reloadHistory = new Gson()
                .fromJson(client.load(historyKey), new TypeToken<List<UUID>>() {
                }.getType());
        if (reloadStorage != null) {
            reloadStorage.forEach(t -> manager.storage.put(t.getUuid(), t));
        }
        if (reloadHistory != null) {
            reloadHistory.forEach(uuid -> manager.inMemoryHistoryManager.add(manager.storage.get(uuid)));
        }
        return manager;
    }
}
