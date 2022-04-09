package com.ezh.taskbook.webApi.hendler;

import com.ezh.taskbook.exception.TaskNotFoundException;
import com.ezh.taskbook.exception.TasksIntersectionException;
import com.ezh.taskbook.manager.TaskManager;
import com.ezh.taskbook.task.SingleTask;
import com.google.gson.Gson;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;

public class SingleTaskHandler implements HttpHandler {

    private final TaskManager manager;

    public SingleTaskHandler(TaskManager manager) {
        this.manager = manager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            String method = exchange.getRequestMethod();
            Headers requestHeaders = exchange.getRequestHeaders();
            List<String> contentTypeValues = requestHeaders.get("content-type");

            switch (method) {
                case "GET":
                    try {
                        UUID uuid = UUID.fromString(exchange.getRequestURI()
                                .getPath()
                                .substring("/tasks/single-task/".length()));
                        String response = new Gson().toJson(manager.getSingleTaskByUuid(uuid));
                        exchange.getResponseHeaders().add("content-type", "application/json");
                        exchange.sendResponseHeaders(200, response.length());
                        exchange.getResponseBody().write(response.getBytes(StandardCharsets.UTF_8));
                        System.out.println("Single task was got");
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                        exchange.sendResponseHeaders(400, -1);
                    } catch (TaskNotFoundException e) {
                        e.printStackTrace();
                        exchange.sendResponseHeaders(404,-1);
                    }
                    break;
                case "POST":
                    if ((contentTypeValues != null) && (contentTypeValues.contains("application/json"))) {
                        try (InputStreamReader inputStreamReader = new InputStreamReader(exchange.getRequestBody())) {
                            SingleTask singleTask = new Gson().fromJson(inputStreamReader, SingleTask.class);
                            manager.addSingleTask(singleTask);
                            System.out.println("Single task was added");
                            exchange.sendResponseHeaders(201, -1);
                        } catch (Exception e) {
                            e.printStackTrace();
                            exchange.sendResponseHeaders(400, -1);
                        }
                    }
                    break;
                case "PUT":
                    if ((contentTypeValues != null) && (contentTypeValues.contains("application/json"))) {
                        try (InputStreamReader inputStreamReader = new InputStreamReader(exchange.getRequestBody())) {
                            UUID uuid = UUID.fromString(exchange.getRequestURI()
                                    .getPath()
                                    .substring("/tasks/single-task/".length()));
                            SingleTask singleTask = new Gson().fromJson(inputStreamReader, SingleTask.class);
                            manager.changeSingleTaskByUuid(uuid, singleTask);
                            System.out.println("Single task was changed");
                            exchange.sendResponseHeaders(204, -1);
                        } catch (IllegalArgumentException | TasksIntersectionException e) {
                            e.printStackTrace();
                            exchange.sendResponseHeaders(400, -1);
                        } catch (TaskNotFoundException e) {
                            e.printStackTrace();
                            exchange.sendResponseHeaders(404,-1);
                        }
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
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                        exchange.sendResponseHeaders(400, -1);
                    } catch (TaskNotFoundException e) {
                        e.printStackTrace();
                        exchange.sendResponseHeaders(404,-1);
                    }
                    break;
                default:
                    System.out.println("This context work only with methods: GET, POST, PUT, DELETE");
                    exchange.sendResponseHeaders(400, -1);
            }
        } finally {
            exchange.sendResponseHeaders(500,-1);
        }
    }
}
