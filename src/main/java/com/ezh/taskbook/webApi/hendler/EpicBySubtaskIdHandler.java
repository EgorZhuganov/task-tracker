package com.ezh.taskbook.webApi.hendler;

import com.ezh.taskbook.manager.TaskManager;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.util.UUID;

public class EpicBySubtaskIdHandler implements HttpHandler {

    TaskManager manager;

    public EpicBySubtaskIdHandler(TaskManager manager) {
        this.manager = manager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();

        switch (method) {
            case "GET":
                try {
                    UUID uuid = UUID.fromString(exchange.getRequestURI().getPath().substring("/tasks/epic/subtask/".length()));
                    manager.getEpicBySubtaskUuid(uuid);
                    System.out.println("Epic was got");
                    exchange.sendResponseHeaders(200,0);
                } catch (Exception e) {
                    e.printStackTrace();
                    exchange.sendResponseHeaders(400,-1);
                }
                break;
            default:
                System.out.println("This context works only with method GET");
                exchange.sendResponseHeaders(400,-1);
        }
    }
}