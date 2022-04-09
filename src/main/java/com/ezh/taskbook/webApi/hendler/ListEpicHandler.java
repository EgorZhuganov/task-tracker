package com.ezh.taskbook.webApi.hendler;

import com.ezh.taskbook.manager.TaskManager;
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class ListEpicHandler implements HttpHandler {

    private final TaskManager manager;

    public ListEpicHandler(TaskManager manager) {
        this.manager = manager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            String method = exchange.getRequestMethod();
            switch (method) {
                case "GET":
                    String response = new Gson().toJson(manager.getListEpics());
                    exchange.getResponseHeaders().add("content-type", "application/json");
                    exchange.sendResponseHeaders(200, response.length());
                    exchange.getResponseBody().write(response.getBytes(StandardCharsets.UTF_8));
                    System.out.println("Epics were got");
                    break;
                case "DELETE":
                    manager.clearEpics();
                    System.out.println("Epics were deleted");
                    exchange.sendResponseHeaders(204, -1);
                    break;
                default:
                    System.out.println("This context work only with methods: GET, DELETE");
                    exchange.sendResponseHeaders(400, -1);
            }
        } finally {
            exchange.sendResponseHeaders(500,-1);
        }
    }
}