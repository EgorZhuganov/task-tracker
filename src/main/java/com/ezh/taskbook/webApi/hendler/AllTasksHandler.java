package com.ezh.taskbook.webApi.hendler;

import com.ezh.taskbook.manager.TaskManager;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

public class AllTasksHandler implements HttpHandler {

    TaskManager manager;

    public AllTasksHandler (TaskManager manager) {
        this.manager = manager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        if (method.equals("GET")) {
            manager.getPrioritizedTasks();
            System.out.println("Prioritized tasks were got");
            exchange.sendResponseHeaders(200, 0);
        } else {
            System.out.println("This context work only with method GET");
            exchange.sendResponseHeaders(400, -1);
        }
    }
}
