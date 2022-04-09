package com.ezh.taskbook.webApi.hendler;

import com.ezh.taskbook.manager.TaskManager;
import com.ezh.taskbook.task.AbstractTask;
import com.ezh.taskbook.task.taskSerializers.jsonAdapter.PropertyMarshallerOfObject;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class HistoryHandler implements HttpHandler {

    private final TaskManager manager;

    public HistoryHandler(TaskManager manager) {
        this.manager = manager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            String method = exchange.getRequestMethod();
            switch (method) {
                case "GET":
                    String response = new GsonBuilder()
                            .registerTypeAdapter(AbstractTask.class, new PropertyMarshallerOfObject())
                            .create()
                            .toJson(manager.getHistory(), new TypeToken<List<AbstractTask>>(){}.getType());
                    exchange.getResponseHeaders().add("content-type", "application/json");
                    exchange.sendResponseHeaders(200, response.length());
                    exchange.getResponseBody().write(response.getBytes(StandardCharsets.UTF_8));
                    System.out.println("History was got");
                    break;
                default:
                    System.out.println("This context work only with method GET");
                    exchange.sendResponseHeaders(400, -1);
            }
        } finally {
            exchange.sendResponseHeaders(500,-1);
        }
    }
}
