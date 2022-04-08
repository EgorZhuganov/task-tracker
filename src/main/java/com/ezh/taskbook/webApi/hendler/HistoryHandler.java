package com.ezh.taskbook.webApi.hendler;

import com.ezh.taskbook.manager.TaskManager;
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class HistoryHandler implements HttpHandler {

    private final TaskManager manager;

    public HistoryHandler (TaskManager manager) {
        this.manager = manager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            String method = exchange.getRequestMethod();
            switch (method) {
                case "GET":
                    if (manager.getHistory().size() == 0) {
                        //TODO нужно отправлять такой ответ или нет? Какой ответ отправлять если данных нет? Писать ли что-то в боди? Или 204-ый код?
                        String response = "No data to transfer";
                        exchange.sendResponseHeaders(200, response.length());
                        exchange.getResponseBody().write(response.getBytes(StandardCharsets.UTF_8));
                        System.out.println(response);
                    } else {
                        manager.getHistory();
                        String response = new Gson().toJson(manager.getHistory());
                        exchange.sendResponseHeaders(200, response.length());
                        exchange.getResponseBody().write(response.getBytes(StandardCharsets.UTF_8));
                        System.out.println("History was got");
                    }
                    break;
                default:
                    System.out.println("This context work only with method GET");
                    exchange.sendResponseHeaders(400, -1);
            }
        } finally {
            exchange.close();
        }
    }
}
