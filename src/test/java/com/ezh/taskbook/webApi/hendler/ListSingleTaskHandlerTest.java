package com.ezh.taskbook.webApi.hendler;

import com.ezh.taskbook.exception.TasksIntersectionException;
import com.ezh.taskbook.manager.FileBackedTasksManager;
import com.ezh.taskbook.manager.TaskManager;
import com.ezh.taskbook.task.SingleTask;
import com.ezh.taskbook.webApi.HttpTaskServer;
import com.google.gson.Gson;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.*;

class ListSingleTaskHandlerTest {

    private final TaskManager manager = new FileBackedTasksManager(new File("test.txt"));
    private final HttpTaskServer server = new HttpTaskServer(manager, 8080);
    private final HttpClient client = HttpClient.newHttpClient();
    private HttpRequest request;
    private final URI url = URI.create("http://localhost:8080/tasks/single-task");

    @BeforeEach
    public void beforeEach() { server.start(); }

    @AfterEach
    public void afterEach() {
        server.stop();
    }

    @Test //GET
    public void test1_checkContextWithGetRequestIfAddTwoSingleTasksToStorageShouldReturnStatusCode200AndHistoryAsJson ()
            throws IOException, InterruptedException, TasksIntersectionException {
        SingleTask singleTask1 = new SingleTask();
        SingleTask singleTask2 = new SingleTask();
        manager.addSingleTask(singleTask1);
        manager.addSingleTask(singleTask2);

        request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();

        var response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String jsonSingleTasks = new Gson().toJson(manager.getListSingleTasks());

        Assertions.assertEquals(jsonSingleTasks, response.body());
        Assertions.assertEquals(200, response.statusCode());
    }

    @Test //DELETE
    public void test2_checkContextWithDeleteRequestIfAddTwoSingleTasksShouldReturnCode204WithoutBody ()
            throws IOException, InterruptedException, TasksIntersectionException {
        SingleTask singleTask1 = new SingleTask();
        SingleTask singleTask2 = new SingleTask();
        manager.addSingleTask(singleTask1);
        manager.addSingleTask(singleTask2);

        request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();

        var response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Assertions.assertEquals("", response.body());
        Assertions.assertEquals(204, response.statusCode());
    }
}