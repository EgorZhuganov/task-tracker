package com.ezh.taskbook.webApi.hendler;

import com.ezh.taskbook.manager.TaskManager;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

public class ListSubtaskHandler implements HttpHandler {

    TaskManager manager;

    public ListSubtaskHandler(TaskManager manager) {
        this.manager = manager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();

        switch (method) {
            case "GET":
                manager.getListSubtasks();
                System.out.println("Subtasks were got");
                exchange.sendResponseHeaders(200,0);
                break;
            case "DELETE":
                manager.clearSubtasksInAllEpic();
                System.out.println("Subtasks were deleted");
                exchange.sendResponseHeaders(204,-1);
                break;
            default:
                System.out.println("This context works only with methods: GET, DELETE");
                exchange.sendResponseHeaders(400,-1);
        }
    }
}