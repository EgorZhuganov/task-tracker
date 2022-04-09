package com.ezh.taskbook.webApi.hendler;

import com.ezh.taskbook.exception.TaskNotFoundException;
import com.ezh.taskbook.exception.TasksIntersectionException;
import com.ezh.taskbook.manager.TaskManager;
import com.ezh.taskbook.task.Subtask;
import com.google.gson.Gson;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;

public class SubtaskHandler implements HttpHandler {

    private final TaskManager manager;

    public SubtaskHandler(TaskManager manager) {
        this.manager = manager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            String method = exchange.getRequestMethod();
            Headers requestHeaders = exchange.getRequestHeaders();
            List<String> contentTypeValues = requestHeaders.get("content-type");

            switch (method) {
                case "GET":
                    try {
                        UUID uuid = UUID.fromString(exchange.getRequestURI()
                                .getPath()
                                .substring("/tasks/subtask/".length()));
                        String response = new Gson().toJson(manager.getSubtaskByUuid(uuid));
                        exchange.getResponseHeaders().add("content-type", "application/json");
                        exchange.sendResponseHeaders(200, response.length());
                        exchange.getResponseBody().write(response.getBytes(StandardCharsets.UTF_8));
                        System.out.println("Subtask was got");
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                        exchange.sendResponseHeaders(400, -1);
                    } catch (TaskNotFoundException e) {
                        e.printStackTrace();
                        exchange.sendResponseHeaders(404,-1);
                    }
                    break;
                case "POST":
                    if ((contentTypeValues != null) && (contentTypeValues.contains("application/json"))) {
                        try (InputStreamReader inputStreamReader = new InputStreamReader(exchange.getRequestBody())) {
                            Subtask subtask = new Gson().fromJson(inputStreamReader, Subtask.class);
                            manager.addSubtaskInAddedEpic(subtask);
                            System.out.println("Subtask was added");
                            exchange.sendResponseHeaders(201, -1);
                        } catch (Exception e) {
                            e.printStackTrace();
                            exchange.sendResponseHeaders(400, -1);
                        }
                    } else {
                        exchange.sendResponseHeaders(400, -1);
                    }
                    break;
                case "PUT":
                    if ((contentTypeValues != null) && (contentTypeValues.contains("application/json"))) {
                        try (InputStreamReader inputStreamReader = new InputStreamReader(exchange.getRequestBody())) {
                            UUID uuid = UUID.fromString(exchange.getRequestURI()
                                    .getPath()
                                    .substring("/tasks/subtask/".length()));
                            Subtask subtask = new Gson().fromJson(inputStreamReader, Subtask.class);
                            manager.changeSubtaskByUuid(uuid, subtask);
                            System.out.println("Subtask was changed");
                            exchange.sendResponseHeaders(204, -1);
                        } catch (IllegalArgumentException | TasksIntersectionException e) {
                            e.printStackTrace();
                            exchange.sendResponseHeaders(400, -1);
                        } catch (TaskNotFoundException e) {
                            e.printStackTrace();
                            exchange.sendResponseHeaders(404,-1);
                        }
                    } else {
                        exchange.sendResponseHeaders(400, -1);
                    }
                    break;
                case "DELETE":
                    try {
                        UUID uuid = UUID.fromString(exchange.getRequestURI()
                                .getPath()
                                .substring("/tasks/subtask/".length()));
                        manager.removeSubtaskByUuid(uuid);
                        System.out.println("Subtask was deleted");
                        exchange.sendResponseHeaders(204, -1);
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                        exchange.sendResponseHeaders(400, -1);
                    } catch (TaskNotFoundException e) {
                        e.printStackTrace();
                        exchange.sendResponseHeaders(404,-1);
                    }
                    break;
                default:
                    System.out.println("This context works only with methods: GET, POST, PUT, DELETE");
                    exchange.sendResponseHeaders(400, -1);
            }
        } finally {
            exchange.sendResponseHeaders(500,-1);
        }
    }
}