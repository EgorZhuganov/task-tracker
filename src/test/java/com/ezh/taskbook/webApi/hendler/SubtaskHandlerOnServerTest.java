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
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class SubtaskHandlerOnServerTest {

    private final TaskManager manager = new FileBackedTasksManager(new File("test.txt"));
    private final HttpTaskServer server = new HttpTaskServer(manager, 8080);
    private final HttpClient client = HttpClient.newHttpClient();
    private HttpRequest request;
    private final URI url = URI.create("http://localhost:8080/tasks/subtask/");

    @BeforeEach
    void beforeEach() { server.start(); }

    @AfterEach
    void afterEach() { server.stop(); }

    @Test //GET
    void test1checkContextWithGetRequestIfSubtaskWasSentShouldReturnStatusCode200()
            throws IOException, InterruptedException, TasksIntersectionException {
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

        Assertions.assertEquals(200, response.statusCode());
        Assertions.assertTrue(response.headers().allValues("content-type").contains("application/json"));
    }

    @Test //GET
    void test2checkContextWithGetRequestIfSubtaskWasntSentShouldReturnStatusCode404()
            throws IOException, InterruptedException, TasksIntersectionException {
        Epic epic1 = new Epic();
        Subtask subtask1 = new Subtask(epic1);
        Subtask subtask2 = new Subtask(epic1); //Attention! Single task won't add to epic
        epic1.getSubtaskList().add(subtask1);

        manager.addEpicWithSubtask(epic1);
        URI urlForRequest = URI.create(url.toString().concat(subtask2.getUuid().toString())); //uuid won't found in storage
        request = HttpRequest.newBuilder()
                .GET()
                .uri(urlForRequest)
                .build();
        var response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Assertions.assertEquals("", response.body());
        Assertions.assertEquals(404, response.statusCode());
    }

    @Test //GET
    void test3checkContextWithGetRequestIfRequestHasWrongUuidFormatShouldReturnStatusCode400()
            throws IOException, InterruptedException, TasksIntersectionException {
        Epic epic1 = new Epic();
        Subtask subtask1 = new Subtask(epic1);
        epic1.getSubtaskList().add(subtask1);

        manager.addEpicWithSubtask(epic1);
        URI urlForRequest = URI.create(url.toString().concat("123456")); //wrong format uuid
        request = HttpRequest.newBuilder()
                .GET()
                .uri(urlForRequest)
                .build();
        var response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Assertions.assertEquals("", response.body());
        Assertions.assertEquals(400, response.statusCode());
    }

    @Test //POST
    void test4checkContextWithPostRequestShouldAddOneSubtaskToStorageAndReturnCode201AndReturnSubtaskListSize1()
            throws IOException, InterruptedException, TaskNotFoundException {
        Epic epic1 = new Epic();
        Subtask subtask1 = new Subtask(epic1);

        manager.addEpic(epic1);
        request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(new Gson().toJson(subtask1)))
                .header("content-type", "application/json")
                .uri(url)
                .build();
        var response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Assertions.assertEquals(subtask1.getUuid(), manager.getSubtaskByUuid(subtask1.getUuid()).getUuid());
        Assertions.assertEquals(1, manager.getListSubtasks().size());
        Assertions.assertEquals("", response.body());
        Assertions.assertEquals(201, response.statusCode());
    }

    @Test //POST if tasks will intersect (first response code 201, second 400)
    void test6checkContextWithPostRequestIfAddTwoSubtasksWithIntersectTimeShouldReturnCode400AndAddOnlyOneTask()
            throws IOException, InterruptedException {
        Epic epic1 = new Epic();
        Subtask subtask1 = new Subtask(epic1);
        Subtask subtask2 = new Subtask(epic1);
        subtask1.setStartTimeAndDuration(LocalDateTime.of(2022, 12, 12, 10, 0), Duration.ofDays(10));
        subtask2.setStartTimeAndDuration(LocalDateTime.of(2022, 12, 12, 20, 0), Duration.ofDays(10));

        manager.addEpic(epic1);
        request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(new Gson().toJson(subtask1)))
                .header("content-type", "application/json")
                .uri(url)
                .build();
        var response1 = client.send(request, HttpResponse.BodyHandlers.ofString());

        request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(new Gson().toJson(subtask2)))
                .header("content-type", "application/json")
                .uri(url)
                .build();
        var response2 = client.send(request, HttpResponse.BodyHandlers.ofString());

        Assertions.assertEquals(1, manager.getListSubtasks().size());
        Assertions.assertEquals(201, response1.statusCode());
        Assertions.assertEquals(400, response2.statusCode());
    }

    @Test //PUT
    void test7checkContextWithPutRequestShouldReturnStatus204AndChangeSubtask1FromStorage()
            throws IOException, InterruptedException, TaskNotFoundException, TasksIntersectionException {
        Epic epic1 = new Epic();
        Subtask subtask1 = new Subtask(epic1);
        Subtask subtask2 = new Subtask(epic1); //won't add to epic
        epic1.getSubtaskList().add(subtask1);
        subtask2.setName("Subtask2 name");
        subtask2.setDescription("Subtasks2 description");

        manager.addEpicWithSubtask(epic1);
        URI urlForRequest = URI.create(url.toString().concat(subtask1.getUuid().toString()));
        request = HttpRequest.newBuilder()
                .PUT(HttpRequest.BodyPublishers.ofString(new Gson().toJson(subtask2)))
                .header("content-type", "application/json")
                .uri(urlForRequest)
                .build();
        var response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Assertions.assertEquals(subtask2.getName(), manager.getSubtaskByUuid(subtask1.getUuid()).getName());
        Assertions.assertEquals(subtask2.getDescription(), manager.getSubtaskByUuid(subtask1.getUuid()).getDescription());
        Assertions.assertEquals(204, response.statusCode());
    }

    @Test //PUT
    void test8checkContextWithPutRequestIfWrongFormatUuidShouldReturnStatusCode400()
            throws IOException, InterruptedException, TasksIntersectionException {
        Epic epic1 = new Epic();
        Subtask subtask1 = new Subtask(epic1);
        Subtask subtask2 = new Subtask(epic1); //won't add to epic
        epic1.getSubtaskList().add(subtask1);
        subtask1.setName("SingleTask2 name");
        subtask2.setDescription("SingleTask2 description");

        manager.addEpicWithSubtask(epic1);

        URI urlForRequest = URI.create(url.toString().concat("123456")); //wrong format uuid
        request = HttpRequest.newBuilder()
                .PUT(HttpRequest.BodyPublishers.ofString(new Gson().toJson(subtask2)))
                .header("content-type", "application/json")
                .uri(urlForRequest)
                .build();
        var response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Assertions.assertEquals(400, response.statusCode());
    }

    @Test //PUT
    void test9checkContextWithPutRequestIfSubtaskNotFoundShouldReturnStatus404()
            throws IOException, InterruptedException {
        Epic epic1 = new Epic();
        Subtask subtask1 = new Subtask(epic1); //subtask will not add to storage
        Subtask subtask2 = new Subtask(epic1); //subtask will not add to storage
        subtask2.setName("SingleTask2 name");
        subtask2.setDescription("SingleTask2 description");

        URI urlForRequest = URI.create(url.toString().concat(subtask1.getUuid().toString()));
        request = HttpRequest.newBuilder()
                .PUT(HttpRequest.BodyPublishers.ofString(new Gson().toJson(subtask2)))
                .header("content-type", "application/json")
                .uri(urlForRequest)
                .build();
        var response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Assertions.assertEquals(404, response.statusCode());
    }

    @Test //DELETE
    void test10checkContextWithDeleteRequestIf1SubtaskThereIsInStorageShouldReturnStatus204AndRemoveSubtaskFromStorage()
            throws IOException, InterruptedException, TasksIntersectionException {
        Epic epic1 = new Epic();
        Subtask subtask1 = new Subtask(epic1);
        epic1.getSubtaskList().add(subtask1);

        manager.addEpicWithSubtask(epic1);
        URI urlForRequest = URI.create(url.toString().concat(subtask1.getUuid().toString()));
        request = HttpRequest.newBuilder()
                .DELETE()
                .uri(urlForRequest)
                .build();

        Assertions.assertEquals(1, manager.getListSubtasks().size());

        var response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Assertions.assertEquals(0, manager.getListSubtasks().size());
        Assertions.assertEquals("", response.body());
        Assertions.assertEquals(204, response.statusCode());
    }

    @Test //DELETE
    void test11checkContextWithDeleteRequestIfTryToUseUrlWithWrongFormatUuidShouldReturnStatus400AndDoNotDeleteTask()
            throws IOException, InterruptedException, TasksIntersectionException {
        Epic epic1 = new Epic();
        Subtask subtask1 = new Subtask(epic1);
        epic1.getSubtaskList().add(subtask1);

        manager.addEpicWithSubtask(epic1);
        URI urlForRequest = URI.create(url.toString().concat("123456")); //wrong format uuid
        request = HttpRequest.newBuilder()
                .DELETE()
                .uri(urlForRequest)
                .build();

        var response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Assertions.assertEquals(1, manager.getListSubtasks().size());
        Assertions.assertEquals(400, response.statusCode());
    }

    @Test //DELETE
    void test12checkContextWithDeleteRequestIfTryToUseUrlWithUuidWhichNotExistingInStorageShouldReturnStatus404()
            throws IOException, InterruptedException, TasksIntersectionException {
        Epic epic1 = new Epic();
        Subtask subtask1 = new Subtask(epic1);
        Subtask subtask2 = new Subtask(epic1); //won't add to storage
        epic1.getSubtaskList().add(subtask1);

        manager.addEpicWithSubtask(epic1);
        URI urlForRequest = URI.create(url.toString().concat(subtask2.getUuid().toString())); //not existing in storage
        request = HttpRequest.newBuilder()
                .DELETE()
                .uri(urlForRequest)
                .build();

        var response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Assertions.assertEquals(1, manager.getListSubtasks().size());
        Assertions.assertEquals(404, response.statusCode());
    }
}