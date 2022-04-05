package com.ezh.taskbook.webApi.hendler;

import com.ezh.taskbook.manager.TaskManager;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

public class ListSingleTaskHandler implements HttpHandler {

    private final TaskManager manager;

    public ListSingleTaskHandler(TaskManager manager) {
        this.manager = manager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        switch (method) {
            case "GET":
                manager.getListSingleTasks();
                System.out.println("Single tasks were got");
                exchange.sendResponseHeaders(200, 0);
                break;
            case "DELETE":
                manager.clearSingleTasks();
                System.out.println("Single tasks were deleted");
                exchange.sendResponseHeaders(204, -1);
                break;
            default:
                System.out.println("This context work only with methods: GET, DELETE");
                exchange.sendResponseHeaders(400,-1);
        }
    }
}
