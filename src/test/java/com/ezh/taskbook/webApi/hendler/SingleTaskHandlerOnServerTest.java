package com.ezh.taskbook.webApi.hendler;

import com.ezh.taskbook.exception.TaskNotFoundException;
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
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class SingleTaskHandlerOnServerTest {

    private final TaskManager manager = new FileBackedTasksManager(new File("test.txt"));
    private final HttpTaskServer server = new HttpTaskServer(manager, 8080);
    private final HttpClient client = HttpClient.newHttpClient();
    private HttpRequest request;
    private final URI url = URI.create("http://localhost:8080/tasks/single-task/");

    @BeforeEach
    void beforeEach() { server.start(); }

    @AfterEach
    void afterEach() { server.stop(); }

    @Test //GET
    void test1checkContextWithGetRequestIfSingleTaskWasSentShouldReturnStatusCode200()
            throws IOException, InterruptedException, TasksIntersectionException {
        SingleTask singleTask = new SingleTask();

        manager.addSingleTask(singleTask);

        URI urlForRequest = URI.create(url.toString().concat(singleTask.getUuid().toString()));
        request = HttpRequest.newBuilder()
                .GET()
                .uri(urlForRequest)
                .build();
        var response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Assertions.assertEquals(200, response.statusCode());
        Assertions.assertTrue(response.headers().allValues("content-type").contains("application/json"));
    }

    @Test //GET
    void test2checkContextWithGetRequestIfSingleTaskWasntSentShouldReturnStatusCode404()
            throws IOException, InterruptedException, TasksIntersectionException {
        SingleTask singleTask1 = new SingleTask();
        SingleTask singleTask2 = new SingleTask(); //Attention! Single task won't add to storage
        manager.addSingleTask(singleTask1);

        URI urlForRequest = URI.create(url.toString().concat(singleTask2.getUuid().toString())); //uuid won't found in storage
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
        SingleTask singleTask1 = new SingleTask();
        manager.addSingleTask(singleTask1);

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
    void test4checkContextWithPostRequestShouldAddOneSingleTaskToStorageAndReturnCode201AndReturnSubtaskListSize1()
            throws IOException, InterruptedException, TaskNotFoundException {
        SingleTask singleTask1 = new SingleTask();

        request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(new Gson().toJson(singleTask1)))
                .header("content-type", "application/json")
                .uri(url)
                .build();
        var response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Assertions.assertEquals(singleTask1.getUuid(), manager.getSingleTaskByUuid(singleTask1.getUuid()).getUuid());
        Assertions.assertEquals(1, manager.getListSingleTasks().size());
        Assertions.assertEquals("", response.body());
        Assertions.assertEquals(201, response.statusCode());
    }

    @Test //POST if tasks will intersect (first response code 201, second 400)
    void test6checkContextWithPostRequestIfAddTwoSingleTasksWithIntersectTimeShouldReturnCode400AndAddOnlyOneTask()
            throws IOException, InterruptedException {
        SingleTask singleTask1 = new SingleTask();
        SingleTask singleTask2 = new SingleTask();
        singleTask1.setStartTimeAndDuration(LocalDateTime.of(2022, 12, 12, 10, 0), Duration.ofDays(10));
        singleTask2.setStartTimeAndDuration(LocalDateTime.of(2022, 12, 12, 20, 0), Duration.ofDays(10));

        request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(new Gson().toJson(singleTask1)))
                .header("content-type", "application/json")
                .uri(url)
                .build();
        var response1 = client.send(request, HttpResponse.BodyHandlers.ofString());

        request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(new Gson().toJson(singleTask2)))
                .header("content-type", "application/json")
                .uri(url)
                .build();
        var response2 = client.send(request, HttpResponse.BodyHandlers.ofString());

        Assertions.assertEquals(1, manager.getListSingleTasks().size());
        Assertions.assertEquals(201, response1.statusCode());
        Assertions.assertEquals(400, response2.statusCode());
    }

    @Test //PUT
    void test7checkContextWithPutRequestShouldReturnStatus204AndChangeSingleTask1FromStorage()
            throws IOException, InterruptedException, TaskNotFoundException, TasksIntersectionException {
        SingleTask singleTask1 = new SingleTask();
        SingleTask singleTask2 = new SingleTask();
        singleTask2.setName("SingleTask2 name");
        singleTask2.setDescription("SingleTask2 description");

        manager.addSingleTask(singleTask1);

        URI urlForRequest = URI.create(url.toString().concat(singleTask1.getUuid().toString()));
        request = HttpRequest.newBuilder()
                .PUT(HttpRequest.BodyPublishers.ofString(new Gson().toJson(singleTask2)))
                .header("content-type", "application/json")
                .uri(urlForRequest)
                .build();
        var response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Assertions.assertEquals(singleTask2.getName(), manager.getSingleTaskByUuid(singleTask1.getUuid()).getName());
        Assertions.assertEquals(singleTask2.getDescription(), manager.getSingleTaskByUuid(singleTask1.getUuid()).getDescription());
        Assertions.assertEquals(204, response.statusCode());
    }

    @Test //PUT
    void test8checkContextWithPutRequestIfWrongFormatUuidShouldReturnStatusCode400()
            throws IOException, InterruptedException, TasksIntersectionException {
        SingleTask singleTask1 = new SingleTask();
        SingleTask singleTask2 = new SingleTask();
        singleTask2.setName("SingleTask2 name");
        singleTask2.setDescription("SingleTask2 description");

        manager.addSingleTask(singleTask1);

        URI urlForRequest = URI.create(url.toString().concat("123456")); //wrong format uuid
        request = HttpRequest.newBuilder()
                .PUT(HttpRequest.BodyPublishers.ofString(new Gson().toJson(singleTask2)))
                .header("content-type", "application/json")
                .uri(urlForRequest)
                .build();
        var response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Assertions.assertEquals(400, response.statusCode());
    }

    @Test //PUT
    void test9checkContextWithPutRequestIfSingleTaskNotFoundShouldReturnStatus404()
            throws IOException, InterruptedException {
        SingleTask singleTask1 = new SingleTask(); //single task will not add to storage
        SingleTask singleTask2 = new SingleTask(); //single task will not add to storage
        singleTask2.setName("SingleTask2 name");
        singleTask2.setDescription("SingleTask2 description");

        URI urlForRequest = URI.create(url.toString().concat(singleTask1.getUuid().toString()));
        request = HttpRequest.newBuilder()
                .PUT(HttpRequest.BodyPublishers.ofString(new Gson().toJson(singleTask2)))
                .header("content-type", "application/json")
                .uri(urlForRequest)
                .build();
        var response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Assertions.assertEquals(404, response.statusCode());
    }

    @Test //DELETE
    void test10checkContextWithDeleteRequestIf1SingleTaskThereIsInStorageShouldReturnStatus204AndRemoveSingleTaskFromStorage()
            throws IOException, InterruptedException, TasksIntersectionException {
        SingleTask singleTask1 = new SingleTask();

        manager.addSingleTask(singleTask1);
        URI urlForRequest = URI.create(url.toString().concat(singleTask1.getUuid().toString()));
        request = HttpRequest.newBuilder()
                .DELETE()
                .uri(urlForRequest)
                .build();

        Assertions.assertEquals(1, manager.getListSingleTasks().size());

        var response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Assertions.assertEquals(0, manager.getListSingleTasks().size());
        Assertions.assertEquals("", response.body());
        Assertions.assertEquals(204, response.statusCode());
    }

    @Test //DELETE
    void test11checkContextWithDeleteRequestIfTryToUseUrlWithWrongFormatUuidShouldReturnStatus400AndDoNotDeleteTask()
            throws IOException, InterruptedException, TasksIntersectionException {
        SingleTask singleTask1 = new SingleTask();

        manager.addSingleTask(singleTask1);
        URI urlForRequest = URI.create(url.toString().concat("123456")); //wrong format uuid
        request = HttpRequest.newBuilder()
                .DELETE()
                .uri(urlForRequest)
                .build();

        var response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Assertions.assertEquals(1, manager.getListSingleTasks().size());
        Assertions.assertEquals(400, response.statusCode());
    }

    @Test //DELETE
    void test12checkContextWithDeleteRequestIfTryToUseUrlWithUuidWhichNotExistingInStorageShouldReturnStatus404()
            throws IOException, InterruptedException, TasksIntersectionException {
        SingleTask singleTask1 = new SingleTask();
        SingleTask singleTask2 = new SingleTask(); //won't add to storage

        manager.addSingleTask(singleTask1);
        URI urlForRequest = URI.create(url.toString().concat(singleTask2.getUuid().toString())); //not existing in storage
        request = HttpRequest.newBuilder()
                .DELETE()
                .uri(urlForRequest)
                .build();

        var response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Assertions.assertEquals(1, manager.getListSingleTasks().size());
        Assertions.assertEquals(404, response.statusCode());
    }
}