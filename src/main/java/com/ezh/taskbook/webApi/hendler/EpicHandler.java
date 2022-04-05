package com.ezh.taskbook.webApi.hendler;

import com.ezh.taskbook.manager.TaskManager;
import com.ezh.taskbook.task.Epic;
import com.ezh.taskbook.task.Subtask;
import com.ezh.taskbook.task.taskSerializers.TaskSerializerToJson;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.UUID;
import java.util.stream.Collectors;

public class EpicHandler implements HttpHandler {

    private final TaskManager manager;

    public EpicHandler(TaskManager manager) {
        this.manager = manager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();

        switch (method) {
            case "GET":
                try {
                    UUID uuid = UUID.fromString(exchange.getRequestURI()
                            .getPath()
                            .substring("/tasks/epic/".length()));
                    manager.getEpicByUuid(uuid);
                    System.out.println("Epic was got");
                    exchange.sendResponseHeaders(200, 0);
                } catch (Exception e) {
                    e.printStackTrace();
                    exchange.sendResponseHeaders(400, -1);
                }
                break;
            case "POST": //if you wish to understand how it works, should read comment in TaskManager
                try (InputStreamReader inputStreamReader = new InputStreamReader(exchange.getRequestBody())) {
                    String json = new BufferedReader(inputStreamReader).lines()
                            .collect(Collectors.joining("\n"));

                    switch (String.valueOf(json.charAt(0))) {
                        case "[":
                            ArrayList<Object> jsonList = TaskSerializerToJson
                                    .prepareGson()
                                    .fromJson(json, new TypeToken<ArrayList<Object>>() {
                                    }.getType());
                            Subtask[] subtaskArray = new Subtask[jsonList.size() - 1];
                            int counter = 0;
                            Gson gsonEpic = new Gson();
                            Epic epic1 = TaskSerializerToJson
                                    .prepareGson()
                                    .fromJson(gsonEpic.toJson(jsonList.get(0)), Epic.class);
                            for (int i = 1; i < jsonList.size(); i++) {
                                Gson gsonSubtask = new Gson();
                                subtaskArray[counter] = TaskSerializerToJson
                                        .prepareGson()
                                        .fromJson(gsonSubtask.toJson(jsonList.get(i)), Subtask.class);
                                counter++;
                            }
                            manager.addEpicWithSubtask(epic1, subtaskArray);
                            System.out.println("Epic and subtasks were added");
                            exchange.sendResponseHeaders(201, -1);
                            break;
                        case "{":
                            Epic epic2 = TaskSerializerToJson
                                    .prepareGson()
                                    .fromJson(json, Epic.class);
                            manager.addEpic(epic2);
                            System.out.println("Epic was added");
                            exchange.sendResponseHeaders(201, -1);
                            break;
                        default:
                            System.out.println("ERROR");
                            exchange.sendResponseHeaders(400, -1);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    exchange.sendResponseHeaders(400, -1);
                }
                break;
            case "PUT":
                try (InputStreamReader inputStreamReader = new InputStreamReader(exchange.getRequestBody())) {
                    UUID uuid = UUID.fromString(exchange.getRequestURI()
                            .getPath()
                            .substring("/tasks/epic/".length()));
                    Epic epic = TaskSerializerToJson
                            .prepareGson()
                            .fromJson(inputStreamReader, Epic.class);
                    manager.changeEpicByUuid(uuid, epic);
                    System.out.println("Epic was changed");
                    exchange.sendResponseHeaders(204, -1);
                } catch (Exception e) {
                    e.printStackTrace();
                    exchange.sendResponseHeaders(400, -1);
                }
                break;
            case "DELETE":
                manager.clearEpics();
                System.out.println("Epics were deleted");
                exchange.sendResponseHeaders(204, -1);
                break;
            default:
                System.out.println("This context works only with methods: GET, POST, PUT, DELETE");
                exchange.sendResponseHeaders(400, -1);
        }
    }
}