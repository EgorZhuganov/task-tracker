package com.ezh.taskbook.webApi.hendler;

import com.ezh.taskbook.exception.TasksIntersectionException;
import com.ezh.taskbook.manager.FileBackedTasksManager;
import com.ezh.taskbook.manager.TaskManager;
import com.ezh.taskbook.task.Epic;
import com.ezh.taskbook.task.Subtask;
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

class EpicBySubtaskIdHandlerTestOnServer {

    private final TaskManager manager = new FileBackedTasksManager(new File("test.txt"));
    private final HttpTaskServer server = new HttpTaskServer(manager,8080);
    private final HttpClient client = HttpClient.newHttpClient();
    private HttpRequest request;
    private final URI url = URI.create("http://localhost:8080/tasks/epic/subtask/");

    @BeforeEach
    public void beforeEach() {
        server.start();
    }

    @AfterEach
    public void afterEach() {
        server.stop();
    }

    @Test //GET
    public void test1_checkContextWithGetRequestIfEpicWasSentShouldReturnEpicAsJsonAndStatusCode200 () throws IOException, InterruptedException, TasksIntersectionException {
        Epic epic1 = new Epic();
        Subtask subtask1 = new Subtask(epic1);
        epic1.getSubtaskList().add(subtask1);

        manager.addEpicWithSubtask(epic1);
        URI urlForRequest = URI.create(url.toString().concat(subtask1.getUuid().toString()));

        request = HttpRequest.newBuilder()
                .GET()
                .uri(urlForRequest)
                .build();
        var response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String epicJson = new Gson().toJson(epic1);

        Assertions.assertEquals(epicJson, response.body());
        Assertions.assertEquals(200, response.statusCode());
    }

    @Test //GET
    public void test2_checkContextWithGetRequestIfEpicWasntSentShouldReturnStatusCode404 () throws IOException, InterruptedException, TasksIntersectionException {
        Epic epic1 = new Epic();
        Subtask subtask1 = new Subtask(epic1);
        Subtask subtask2 = new Subtask(epic1); //Attention, Subtask won't add to manager and Epic's subtask list
        epic1.getSubtaskList().add(subtask1);

        manager.addEpicWithSubtask(epic1); //Will add epic1 and subtask1
        URI urlForRequest = URI.create(url.toString().concat(subtask2.getUuid().toString())); //wrong uuid

        request = HttpRequest.newBuilder()
                .GET()
                .uri(urlForRequest)
                .build();
        var response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Assertions.assertEquals("", response.body());
        Assertions.assertEquals(404, response.statusCode());
    }
}