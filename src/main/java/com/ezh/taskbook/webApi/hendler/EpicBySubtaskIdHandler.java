package com.ezh.taskbook.webApi.hendler;

import com.ezh.taskbook.exception.TaskNotFoundException;
import com.ezh.taskbook.manager.TaskManager;
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class EpicBySubtaskIdHandler implements HttpHandler {

    private final TaskManager manager;

    public EpicBySubtaskIdHandler(TaskManager manager) {
        this.manager = manager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            String method = exchange.getRequestMethod();

            switch (method) {
                case "GET":
                    try {
                        UUID uuid = UUID.fromString(exchange.getRequestURI().getPath().substring("/tasks/epic/subtask/".length()));
                        String response = new Gson().toJson(manager.getEpicBySubtaskUuid(uuid));
                        exchange.getResponseHeaders().add("content-type", "application/json");
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
                default:
                    System.out.println("This context works only with method GET");
                    exchange.sendResponseHeaders(400, -1);
            }
        } finally {
            exchange.sendResponseHeaders(500,-1);
        }
    }
}