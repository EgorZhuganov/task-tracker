package com.ezh.taskbook.webApi.hendler;

import com.ezh.taskbook.manager.TaskManager;
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class AllTasksHandler implements HttpHandler {

    private final TaskManager manager;

    public AllTasksHandler(TaskManager manager) {
        this.manager = manager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            String method = exchange.getRequestMethod();
            if (method.equals("GET")) {
                if (manager.getPrioritizedTasks().size() == 0) {
                    String response = "No data to transfer";
                    exchange.sendResponseHeaders(201, response.length());
                    exchange.getResponseBody().write(response.getBytes(StandardCharsets.UTF_8));
                    System.out.println(response);
                } else {
                    manager.getPrioritizedTasks();
                    String gson = new Gson().toJson(manager.getPrioritizedTasks());
                    exchange.sendResponseHeaders(200, 0);
                    exchange.getResponseBody().write(gson.getBytes(StandardCharsets.UTF_8));
                    System.out.println("Prioritized tasks were got");
                }
            } else {
                System.out.println("This context work only with method GET");
                exchange.sendResponseHeaders(400, -1);
            }
        } finally {
            exchange.close();
        }
    }
}
