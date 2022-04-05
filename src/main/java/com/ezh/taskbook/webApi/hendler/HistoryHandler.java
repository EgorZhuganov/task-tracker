package com.ezh.taskbook.webApi.hendler;

import com.ezh.taskbook.manager.TaskManager;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

public class HistoryHandler implements HttpHandler {

    TaskManager manager;

    public HistoryHandler (TaskManager manager) {
        this.manager = manager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        if (method.equals("GET")) {
            manager.getHistory();
            System.out.println("History was got");
            exchange.sendResponseHeaders(200, 0);
        } else {
            System.out.println("This context work only with method GET");
        }
    }
}
