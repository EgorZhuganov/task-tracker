package com.ezh.taskbook.task.taskSerializers;

import com.ezh.taskbook.task.AbstractTask;
import com.ezh.taskbook.task.Epic;
import com.ezh.taskbook.task.SingleTask;
import com.ezh.taskbook.task.Subtask;
import com.ezh.taskbook.task.taskSerializers.jsonAdapter.DurationAdapter;
import com.ezh.taskbook.task.taskSerializers.jsonAdapter.LocalDateTimeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.time.Duration;
import java.time.LocalDateTime;

public abstract class TaskSerializerToJson<T extends AbstractTask> {

    public static String taskToJson(AbstractTask task) {
        Gson gson = prepareGson();
        return gson.toJson(task);
    }

    public static SingleTask singleTaskFromJson(String value) {
        Gson gson = prepareGson();
        return gson.fromJson(String.valueOf(value), SingleTask.class);
    }

    public static Epic epicFromJson(String value) {
        Gson gson = prepareGson();
        return gson.fromJson(String.valueOf(value), Epic.class);
    }

    public static Subtask subtaskFromJson(String value) {
        Gson gson = prepareGson();
        return gson.fromJson(String.valueOf(value), Subtask.class);
    }

    public static Gson prepareGson() {
        return new GsonBuilder()
                .registerTypeAdapter(Duration.class, new DurationAdapter())
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
//                .setPrettyPrinting()
                .create();
    }
}
