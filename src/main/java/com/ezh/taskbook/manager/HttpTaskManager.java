package com.ezh.taskbook.manager;

import com.ezh.taskbook.task.AbstractTask;
import com.ezh.taskbook.task.taskSerializers.jsonAdapter.PropertyMarshallerOfObject;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class HttpTaskManager extends FileBackedTasksManager {

    private KVTaskClient client;
    private final String storageKey;
    private final String historyKey;

    public HttpTaskManager (URI url, String keyStoragePrefix) {
        storageKey = keyStoragePrefix + "STORAGE_KEY";
        historyKey = keyStoragePrefix + "HISTORY_KEY";
        try {
            client = new KVTaskClient(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void save () {
        client.put(historyKey, new Gson()
                .toJson(inMemoryHistoryManager.getHistory()
                        .stream()
                        .map(AbstractTask::getUuid)
                        .collect(Collectors.toList()))
        );
        client.put(storageKey, new GsonBuilder()
                .registerTypeAdapter(AbstractTask.class, new TypeToken<List<AbstractTask>>(){}.getType())
                .create()
                .toJson(storage.values()));
    }

    public HttpTaskManager loadFromServer(URI url, Integer port) {
        if (port == null) {
            throw new NullPointerException();
        }
        HttpTaskManager manager = new HttpTaskManager(url, port.toString());
        List<AbstractTask> reloadStorage = new GsonBuilder()
                .registerTypeAdapter(AbstractTask.class, new PropertyMarshallerOfObject())
                .create()
                .fromJson(client.load(storageKey), new TypeToken<List<AbstractTask>>(){}.getType());
        List<UUID> reloadHistory = new Gson()
                .fromJson(client.load(historyKey), new TypeToken<List<UUID>>(){}.getType());
        for (AbstractTask t : reloadStorage) {
            this.storage.put(t.getUuid(), t);
        }
        reloadStorage.forEach(t -> this.storage.put(t.getUuid(), t));
        reloadHistory.forEach(uuid -> this.inMemoryHistoryManager.add(storage.get(uuid)));

        return manager;
    }
}
