package com.ezh.taskbook.webApi.hendler;

import com.ezh.taskbook.manager.TaskManager;
import com.ezh.taskbook.task.Subtask;
import com.ezh.taskbook.task.taskSerializers.TaskSerializerToJson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.UUID;

public class SubtaskHandler implements HttpHandler {

    TaskManager manager;

    public SubtaskHandler(TaskManager manager) {
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
                            .substring("/tasks/subtask/".length()));
                    manager.getSubtaskByUuid(uuid);
                    System.out.println("Subtask was got");
                    exchange.sendResponseHeaders(200, 0);
                } catch (Exception e) {
                    e.printStackTrace();
                    exchange.sendResponseHeaders(400, -1);
                }
                break;
            case "POST":
                try (InputStreamReader inputStreamReader = new InputStreamReader(exchange.getRequestBody())) {
                    Subtask subtask = TaskSerializerToJson
                            .prepareGson()
                            .fromJson(inputStreamReader, Subtask.class);
                    manager.addSubtaskInAddedEpic(subtask);
                    System.out.println("Subtask was added");
                    exchange.sendResponseHeaders(201, -1);
                } catch (Exception e) {
                    e.printStackTrace();
                    exchange.sendResponseHeaders(400, -1);
                }
                break;
            case "PUT":
                try (InputStreamReader inputStreamReader = new InputStreamReader(exchange.getRequestBody())) {
                    UUID uuid = UUID.fromString(exchange.getRequestURI()
                            .getPath()
                            .substring("/tasks/subtask/".length()));
                    Subtask subtask = TaskSerializerToJson.prepareGson().fromJson(inputStreamReader, Subtask.class);
                    manager.changeSubtaskByUuid(uuid, subtask);
                    System.out.println("Subtask was changed");
                    exchange.sendResponseHeaders(204,-1);
                } catch (Exception e) {
                    e.printStackTrace();
                    exchange.sendResponseHeaders(400,-1);
                }
                break;
            case "DELETE":
                try {
                    UUID uuid = UUID.fromString(exchange.getRequestURI().getPath().substring("/tasks/subtask/".length()));
                    manager.removeSubtaskByUuid(uuid);
                    System.out.println("Subtask was deleted");
                    exchange.sendResponseHeaders(204,-1);
                } catch (Exception e) {
                    e.printStackTrace();
                    exchange.sendResponseHeaders(400,-1);
                }
                break;
            default:
                System.out.println("This context works only with methods: GET, POST, PUT, DELETE");
                exchange.sendResponseHeaders(400,-1);
        }
    }
}