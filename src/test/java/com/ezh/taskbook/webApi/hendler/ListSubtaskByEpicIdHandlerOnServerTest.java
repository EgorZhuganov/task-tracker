package com.ezh.taskbook.webApi.hendler;

import com.ezh.taskbook.exception.TaskNotFoundException;
import com.ezh.taskbook.exception.TasksIntersectionException;
import com.ezh.taskbook.manager.FileBackedTasksManager;
import com.ezh.taskbook.manager.TaskManager;
import com.ezh.taskbook.task.Epic;
import com.ezh.taskbook.task.Subtask;
import com.ezh.taskbook.webApi.HttpTaskServer;
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

class ListSubtaskByEpicIdHandlerOnServerTest {

    private final TaskManager manager = new FileBackedTasksManager(new File("test.txt"));
    private final HttpTaskServer server = new HttpTaskServer(manager, 8080);
    private final HttpClient client = HttpClient.newHttpClient();
    private HttpRequest request;
    private final URI url = URI.create("http://localhost:8080/tasks/subtask/epic/");

    @BeforeEach
    void beforeEach() { server.start(); }

    @AfterEach
    void afterEach() { server.stop(); }

    @Test //GET
    void test1_checkContextGetRequestIfAddEpicWith3SubtasksShouldReturnCode200()
            throws TasksIntersectionException, IOException, InterruptedException {
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

        Assertions.assertEquals(200, response.statusCode());
        Assertions.assertTrue(response.headers().allValues("content-type").contains("application/json"));
    }

    @Test //GET
    void test2_checkContextGetRequestIfTryToFindEpicWithWrongFormatUuidShouldReturnCode400 ()
            throws TasksIntersectionException, IOException, InterruptedException {

        Epic epic1 = new Epic();
        Subtask subtask1 = new Subtask(epic1);
        Subtask subtask2 = new Subtask(epic1);
        Subtask subtask3 = new Subtask(epic1);
        epic1.getSubtaskList().add(subtask1);
        epic1.getSubtaskList().add(subtask2);
        epic1.getSubtaskList().add(subtask3);

        manager.addEpicWithSubtask(epic1);
        URI urlForRequest = URI.create(url.toString().concat("12345")); //wrong format uuid
        request = HttpRequest.newBuilder()
                .GET()
                .uri(urlForRequest)
                .build();
        var response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Assertions.assertEquals(400, response.statusCode());
    }

    @Test //GET
    void test3_checkContextGetRequestIfTryToFindEpicWithUuidWhichNotExistInStorageShouldReturnCode404 ()
            throws TasksIntersectionException, IOException, InterruptedException {

        Epic epic1 = new Epic();
        Epic epic2 = new Epic(); //won't add to storage
        Subtask subtask1 = new Subtask(epic1);
        Subtask subtask2 = new Subtask(epic1);
        Subtask subtask3 = new Subtask(epic1);
        epic1.getSubtaskList().add(subtask1);
        epic1.getSubtaskList().add(subtask2);
        epic2.getSubtaskList().add(subtask3);

        manager.addEpicWithSubtask(epic1);
        URI urlForRequest = URI.create(url.toString().concat(epic2.getUuid().toString())); //epic with this uuid not exist in storage
        request = HttpRequest.newBuilder()
                .GET()
                .uri(urlForRequest)
                .build();
        var response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Assertions.assertEquals(404, response.statusCode());
    }

    @Test //DELETE
    void test4_checkContextDeleteRequestIfAddEpicWith3SubtasksShouldReturnCode204AndSubtasksListSize0()
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

    @Test //DELETE
    void test5_checkContextDeleteRequestIfTryFindEpicWithWrongUuidShouldReturnCode400 ()
            throws TasksIntersectionException, IOException, InterruptedException, TaskNotFoundException {
        Epic epic1 = new Epic();
        Subtask subtask1 = new Subtask(epic1);
        Subtask subtask2 = new Subtask(epic1);
        Subtask subtask3 = new Subtask(epic1);
        epic1.getSubtaskList().add(subtask1);
        epic1.getSubtaskList().add(subtask2);
        epic1.getSubtaskList().add(subtask3);

        manager.addEpicWithSubtask(epic1);
        URI urlForRequest = URI.create(url.toString().concat("12345")); //wrong format uuid
        request = HttpRequest.newBuilder()
                .DELETE()
                .uri(urlForRequest)
                .build();

        Assertions.assertEquals(3, manager.getListSubtasksByEpicUuid(epic1.getUuid()).size());

        var response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Assertions.assertEquals(400, response.statusCode());
        Assertions.assertEquals(3, manager.getListSubtasksByEpicUuid(epic1.getUuid()).size());
    }

    @Test //DELETE
    void test6_checkContextDeleteRequestIfTryFindEpicInStorageWithNotExistingUuidShouldReturnCode404()
            throws TasksIntersectionException, IOException, InterruptedException, TaskNotFoundException {
        Epic epic1 = new Epic();
        Epic epic2 = new Epic(); //won't add to storage
        Subtask subtask1 = new Subtask(epic1);
        Subtask subtask2 = new Subtask(epic1);
        Subtask subtask3 = new Subtask(epic1);
        epic1.getSubtaskList().add(subtask1);
        epic1.getSubtaskList().add(subtask2);
        epic2.getSubtaskList().add(subtask3);

        manager.addEpicWithSubtask(epic1);

        URI urlForRequest = URI.create(url.toString().concat(epic2.getUuid().toString()));
        request = HttpRequest.newBuilder()
                .DELETE()
                .uri(urlForRequest)
                .build();

        Assertions.assertEquals(2, manager.getListSubtasksByEpicUuid(epic1.getUuid()).size());

        var response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Assertions.assertEquals(404, response.statusCode());
        Assertions.assertEquals(2, manager.getListSubtasksByEpicUuid(epic1.getUuid()).size());
    }
}