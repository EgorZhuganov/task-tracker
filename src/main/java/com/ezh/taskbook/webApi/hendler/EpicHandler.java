package com.ezh.taskbook.webApi.hendler;

import com.ezh.taskbook.exception.TaskNotFoundException;
import com.ezh.taskbook.manager.TaskManager;
import com.ezh.taskbook.task.Epic;
import com.google.gson.Gson;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;

public class EpicHandler implements HttpHandler {

    private final TaskManager manager;

    public EpicHandler(TaskManager manager) {
        this.manager = manager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            String method = exchange.getRequestMethod();
            Headers requestHeaders = exchange.getRequestHeaders();
            List<String> contentTypeValues = requestHeaders.get("Content-Type");

            switch (method) {
                case "GET":
                    try {
                        UUID uuid = UUID.fromString(exchange.getRequestURI()
                                .getPath()
                                .substring("/tasks/epic/".length()));
                        manager.getEpicByUuid(uuid);
                        String response = new Gson().toJson(manager.getEpicByUuid(uuid));
                        exchange.sendResponseHeaders(200, response.length());
                        exchange.getResponseBody().write(response.getBytes(StandardCharsets.UTF_8));
                        System.out.println("Epic was got");
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
                            Epic epic = new Gson().fromJson(inputStreamReader, Epic.class);
                            if (epic.getSubtaskList() != null && epic.getSubtaskList().size() > 0) {
                                manager.addEpicWithSubtask(epic);
                                System.out.println("Epic and subtasks were added");
                            } else {
                                manager.addEpic(epic);
                                System.out.println("Epic was added");
                            }
                            exchange.sendResponseHeaders(204, -1);
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
                                    .substring("/tasks/epic/".length()));
                            Epic epic = new Gson().fromJson(inputStreamReader, Epic.class);
                            manager.changeEpicByUuid(uuid, epic);
                            System.out.println("Epic was changed");
                            exchange.sendResponseHeaders(204, -1);
                        } catch (Exception e) {
                            e.printStackTrace();
                            exchange.sendResponseHeaders(400, -1);
                        }
                    } else {
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
        } finally {
            exchange.sendResponseHeaders(500,-1);
            exchange.close();
        }
    }
}