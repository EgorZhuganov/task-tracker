package com.ezh.taskbook.webApi.hendler;

import com.ezh.taskbook.manager.TaskManager;
import com.ezh.taskbook.task.SingleTask;
import com.ezh.taskbook.task.taskSerializers.TaskSerializerToJson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.UUID;

public class SingleTaskHandler implements HttpHandler {

    private final TaskManager manager;

    public SingleTaskHandler (TaskManager manager) {
        this.manager = manager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        switch (method) {
            case "GET":
                try {
                    UUID uuid = UUID.fromString(exchange.getRequestURI()
                            .getPath()
                            .substring("/tasks/single-task/".length()));
                    manager.getSingleTaskByUuid(uuid);
                    System.out.println("Single task was got");
                    exchange.sendResponseHeaders(200, 0);
                } catch (Exception e) {
                    e.printStackTrace();
                    exchange.sendResponseHeaders(400, -1);
                }
                break;
            case "POST":
                try (InputStreamReader inputStreamReader = new InputStreamReader(exchange.getRequestBody())) {
                    SingleTask singleTask = TaskSerializerToJson
                            .prepareGson()
                            .fromJson(inputStreamReader, SingleTask.class);
                    try {
                        manager.addSingleTask(singleTask);
                        System.out.println("Single task was added");
                        exchange.sendResponseHeaders(201, 0);
                    } catch (Exception e) {
                        e.printStackTrace();
                        exchange.sendResponseHeaders(400, -1);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case "PUT":
                try (InputStreamReader inputStreamReader = new InputStreamReader(exchange.getRequestBody())) {
                    try {
                        UUID uuid = UUID.fromString(exchange.getRequestURI()
                                .getPath()
                                .substring("/tasks/single-task/".length()));
                        SingleTask singleTask = TaskSerializerToJson
                                .prepareGson()
                                .fromJson(inputStreamReader, SingleTask.class);
                        manager.changeSingleTaskByUuid(uuid, singleTask);
                        System.out.println("Single task was changed");
                        exchange.sendResponseHeaders(201, 0);
                    } catch (Exception e) {
                        e.printStackTrace();
                        exchange.sendResponseHeaders(400, -1);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    exchange.sendResponseHeaders(400, -1);
                }
                break;
            case "DELETE":
                try {
                    UUID uuid = UUID.fromString(exchange.getRequestURI()
                            .getPath()
                            .substring("/tasks/single-task/".length()));
                    manager.removeSingleTaskByUuid(uuid);
                    System.out.println("Single task was deleted");
                    exchange.sendResponseHeaders(204, -1);
                } catch (Exception e) {
                    e.printStackTrace();
                    exchange.sendResponseHeaders(400, -1);
                }
                break;
            default:
                System.out.println("This context work only with method GET, POST, PUT, DELETE");
                exchange.sendResponseHeaders(400, -1);
        }
    }
}
