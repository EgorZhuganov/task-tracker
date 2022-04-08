package com.ezh.taskbook.webApi.hendler;

import com.ezh.taskbook.manager.TaskManager;
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class ListSingleTaskHandler implements HttpHandler {

    private final TaskManager manager;

    public ListSingleTaskHandler(TaskManager manager) {
        this.manager = manager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            String method = exchange.getRequestMethod();
            switch (method) {
                case "GET":
                    manager.getListSingleTasks();
                    String response = new Gson().toJson(manager.getListSingleTasks());
                    exchange.sendResponseHeaders(200, response.length());
                    exchange.getResponseBody().write(response.getBytes(StandardCharsets.UTF_8));
                    System.out.println("Single tasks were got");
                    break;
                case "DELETE":
                    manager.clearSingleTasks();
                    System.out.println("Single tasks were deleted");
                    exchange.sendResponseHeaders(204, -1);
                    break;
                default:
                    System.out.println("This context work only with methods: GET, DELETE");
                    exchange.sendResponseHeaders(400, -1);
            }
        } finally {
            exchange.close();
        }
    }
}
