package com.ezh.taskbook.webApi;

import com.ezh.taskbook.manager.TaskManager;
import com.ezh.taskbook.webApi.hendler.*;
import com.sun.net.httpserver.HttpServer;

import java.io.*;
import java.net.InetSocketAddress;

public class HttpTaskServer {

    private final int port;
    private HttpServer server;
    private final TaskManager manager;

    public HttpTaskServer(TaskManager taskManager, int port) {
        this.manager = taskManager;
        this.port = port;
        try {
            server = HttpServer.create(new InetSocketAddress("localhost", port), 0);
        } catch (IOException e) {
            System.out.println("Error when creating a server with a port number" + port);
            e.printStackTrace();
        }
        createAllContext();
    }

    public void start() {
        System.out.println("Server starts (HttpTaskServer) on the port " + port);
        System.out.println("Open in the browser http://localhost:" + port + "/");
        server.start();
    }

    public void stop() {
        server.stop(0);
        System.out.println("Server (HttpTaskServer) was stopped on the port "+ port);
    }

    private void createAllContext() {
        server.createContext("/tasks/", new AllTasksHandler(manager));
        server.createContext("/tasks/history", new HistoryHandler(manager));
        server.createContext("/tasks/single-task", new ListSingleTaskHandler(manager));
        server.createContext("/tasks/single-task/", new SingleTaskHandler(manager));
        server.createContext("/tasks/epic", new ListEpicHandler(manager));
        server.createContext("/tasks/epic/", new EpicHandler(manager));
        server.createContext("/tasks/epic/subtask/", new EpicBySubtaskIdHandler(manager));
        server.createContext("/task/subtask", new ListSubtaskHandler(manager));
        server.createContext("/tasks/subtask/", new SubtaskHandler(manager));
        server.createContext("/tasks/subtask/epic/", new ListSubtaskByEpicIdHandler(manager));
    }
}
