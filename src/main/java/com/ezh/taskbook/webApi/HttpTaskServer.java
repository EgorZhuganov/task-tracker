package com.ezh.taskbook.webApi;

import com.ezh.taskbook.manager.FileBackedTasksManager;
import com.ezh.taskbook.manager.TaskManager;
import com.ezh.taskbook.webApi.hendler.*;
import com.sun.net.httpserver.HttpServer;

import java.io.*;
import java.net.InetSocketAddress;

public class HttpTaskServer {

    static final int PORT = 8080;
    HttpServer server;
    TaskManager manager;

    public HttpTaskServer(TaskManager taskManager) {
        this.manager = taskManager;
        try {
            server = HttpServer.create(new InetSocketAddress("localhost", PORT), 0);
        } catch (IOException e) {
            System.out.println("Error when creating a server with a port number" + PORT);
            e.printStackTrace();
        }
        createAllContext();
    }

    public static void main(String[] args) {
        HttpTaskServer server = new HttpTaskServer(FileBackedTasksManager.loadFromFile(new File("test.txt")));
        server.start();
    }

    public void start() {
        server.start();
        System.out.println("Server starts");
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
