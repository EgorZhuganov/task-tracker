package com.ezh.taskbook.webApi.hendler;

import com.ezh.taskbook.exception.TaskNotFoundException;
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

import static org.junit.jupiter.api.Assertions.*;

class ListSubtaskByEpicIdHandlerTest {

    private final TaskManager manager = new FileBackedTasksManager(new File("test.txt"));
    private final HttpTaskServer server = new HttpTaskServer(manager, 8080);
    private final HttpClient client = HttpClient.newHttpClient();
    private HttpRequest request;
    private final URI url = URI.create("http://localhost:8080/tasks/subtask/epic/");

    @BeforeEach
    public void beforeEach() { server.start(); }

    @AfterEach
    public void afterEach() {
        server.stop();
    }

    @Test //GET
    public void test1_checkContextGetRequestIfAddEpicWith3SubtasksShouldReturnListSubtasksAsJsonAndCode200()
            throws TasksIntersectionException, IOException, InterruptedException, TaskNotFoundException {

        Epic epic1 = new Epic();
        Subtask subtask1 = new Subtask(epic1);
        Subtask subtask2 = new Subtask(epic1);
        Subtask subtask3 = new Subtask(epic1);
        epic1.getSubtaskList().add(subtask1);
        epic1.getSubtaskList().add(subtask2);
        epic1.getSubtaskList().add(subtask3);

        manager.addEpicWithSubtask(epic1);

        URI urlForRequest = URI.create(url.toString().concat(epic1.getUuid().toString()));

        request = HttpRequest.newBuilder()
                .GET()
                .uri(urlForRequest)
                .build();

        var response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String listSubtaskByJson = new Gson().toJson(manager.getListSubtasksByEpicUuid(epic1.getUuid()));

        Assertions.assertEquals(200, response.statusCode());
        Assertions.assertEquals(listSubtaskByJson, response.body());
    }

    @Test //GET
    public void test2_checkContextGetRequestIfTryToFindEpicWithWrongUuidShouldReturnCode400 ()
            throws TasksIntersectionException, IOException, InterruptedException {

        Epic epic1 = new Epic();
        Subtask subtask1 = new Subtask(epic1);
        Subtask subtask2 = new Subtask(epic1);
        Subtask subtask3 = new Subtask(epic1);
        epic1.getSubtaskList().add(subtask1);
        epic1.getSubtaskList().add(subtask2);
        epic1.getSubtaskList().add(subtask3);

        manager.addEpicWithSubtask(epic1);

        URI urlForRequest = URI.create(url.toString().concat("12345")); //wrong uuid


        request = HttpRequest.newBuilder()
                .GET()
                .uri(urlForRequest)
                .build();

        var response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Assertions.assertEquals(400, response.statusCode());
    }

    @Test //DELETE
    public void test3_checkContextDeleteRequestIfAddEpicWith3SubtasksShouldReturnCode204AndSubtasksListSize0()
            throws TasksIntersectionException, IOException, InterruptedException, TaskNotFoundException {
        Epic epic1 = new Epic();
        Subtask subtask1 = new Subtask(epic1);
        Subtask subtask2 = new Subtask(epic1);
        Subtask subtask3 = new Subtask(epic1);
        epic1.getSubtaskList().add(subtask1);
        epic1.getSubtaskList().add(subtask2);
        epic1.getSubtaskList().add(subtask3);

        manager.addEpicWithSubtask(epic1);

        URI urlForRequest = URI.create(url.toString().concat(epic1.getUuid().toString()));
        request = HttpRequest.newBuilder()
                .DELETE()
                .uri(urlForRequest)
                .build();

        Assertions.assertEquals(3, manager.getListSubtasksByEpicUuid(epic1.getUuid()).size());

        var response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Assertions.assertEquals(204, response.statusCode());
        Assertions.assertEquals(0, manager.getListSubtasksByEpicUuid(epic1.getUuid()).size());
    }
}