package com.ezh.taskbook.webApi.hendler;

import com.ezh.taskbook.manager.TaskManager;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

public class ListEpicHandler implements HttpHandler {

    TaskManager manager;

    public ListEpicHandler(TaskManager manager) {
        this.manager = manager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        switch (method) {
            case "GET":
                manager.getListEpics();
                System.out.println("Epics were got");
                exchange.sendResponseHeaders(200, 0);
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
    }
}