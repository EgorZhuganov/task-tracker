package com.ezh.taskbook.webApi.hendler;

import com.ezh.taskbook.exception.TaskNotFoundException;
import com.ezh.taskbook.manager.FileBackedTasksManager;
import com.ezh.taskbook.manager.TaskManager;
import com.ezh.taskbook.task.Epic;
import com.ezh.taskbook.task.Subtask;
import com.ezh.taskbook.webApi.HttpTaskServer;
import com.google.gson.Gson;
import org.junit.jupiter.api.*;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

class EpicHandlerOnServerTest {

    private final TaskManager manager = new FileBackedTasksManager(new File("test.txt"));
    private final HttpTaskServer server = new HttpTaskServer(manager, 8080);
    private final HttpClient client = HttpClient.newHttpClient();
    private HttpRequest request;
    private final URI url = URI.create("http://localhost:8080/tasks/epic/");

    @BeforeEach
    void beforeEach() { server.start(); }

    @AfterEach
    void afterEach() { server.stop(); }

    @Test //GET
    void test1checkContextWithGetRequestIfEpicWasSentShouldReturnStatusCode200() throws IOException, InterruptedException {
        Epic epic1 = new Epic();

        manager.addEpic(epic1);

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
    void test2checkContextWithGetRequestIfEpicWasntSentShouldReturnStatusCode404() throws IOException, InterruptedException {
        Epic epic1 = new Epic();
        Epic epic2 = new Epic(); //Attention! Epic won't add to storage
        manager.addEpic(epic1);

        URI urlForRequest = URI.create(url.toString().concat(epic2.getUuid().toString())); //uuid won't found in storage
        request = HttpRequest.newBuilder()
                .GET()
                .uri(urlForRequest)
                .build();
        var response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Assertions.assertEquals("", response.body());
        Assertions.assertEquals(404, response.statusCode());
    }

    @Test //GET
    void test3checkContextWithGetRequestIfRequestHasWrongUuidFormatShouldReturnStatusCode400() throws IOException, InterruptedException {
        Epic epic1 = new Epic();
        manager.addEpic(epic1);

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
    void test4checkContextWithPostRequestAddEpicWhichHas2SubtasksShouldAddEpicWithSubtasksToStorageAndReturnCode201And2SubtaskAnd1EpicFromManager()
            throws IOException, InterruptedException, TaskNotFoundException {
        Epic epic1 = new Epic();
        Subtask subtask1 = new Subtask(epic1);
        Subtask subtask2 = new Subtask(epic1);
        epic1.getSubtaskList().add(subtask1);
        epic1.getSubtaskList().add(subtask2);

        request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(new Gson().toJson(epic1)))
                .header("Content-Type", "application/json")
                .uri(url)
                .build();
        var response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Assertions.assertEquals(epic1.getUuid(), manager.getEpicByUuid(epic1.getUuid()).getUuid());
        Assertions.assertEquals(2, manager.getListSubtasks().size());
        Assertions.assertEquals("", response.body());
        Assertions.assertEquals(201, response.statusCode());

    }

    @Test //POST
    void test6checkContextWithPostRequestIfAddOnlyOneEpicShouldReturnCode201AndReturnEpicFromStorage()
            throws IOException, InterruptedException, TaskNotFoundException {
        Epic epic1 = new Epic();

        request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(new Gson().toJson(epic1)))
                .header("content-type", "application/json")
                .uri(url)
                .build();
        var response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Assertions.assertEquals(epic1.getUuid(), manager.getEpicByUuid(epic1.getUuid()).getUuid());
        Assertions.assertEquals(201, response.statusCode());
    }

    @Test //PUT
    void test7checkContextWithPutRequestShouldReturnStatus204AndChangeEpic1FromStorage()
            throws IOException, InterruptedException, TaskNotFoundException {
        Epic epic1 = new Epic();
        Epic epic2 = new Epic();
        epic2.setName("Epic2 name");
        epic2.setDescription("Epic2 description");

        manager.addEpic(epic1);

        URI urlForRequest = URI.create(url.toString().concat(epic1.getUuid().toString()));
        request = HttpRequest.newBuilder()
                .PUT(HttpRequest.BodyPublishers.ofString(new Gson().toJson(epic2)))
                .header("content-type", "application/json")
                .uri(urlForRequest)
                .build();
        var response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Assertions.assertEquals(epic2.getName(), manager.getEpicByUuid(epic1.getUuid()).getName());
        Assertions.assertEquals(epic2.getDescription(), manager.getEpicByUuid(epic1.getUuid()).getDescription());
        Assertions.assertEquals(204, response.statusCode());
    }

    @Test //PUT
    void test8checkContextWithPutRequestIfWrongFormatUuidShouldReturnStatusCode400()
            throws IOException, InterruptedException {
        Epic epic1 = new Epic();
        Epic epic2 = new Epic();
        epic2.setName("Epic2 name");
        epic2.setDescription("Epic2 description");

        manager.addEpic(epic1);

        URI urlForRequest = URI.create(url.toString().concat("123456")); //wrong format uuid
        request = HttpRequest.newBuilder()
                .PUT(HttpRequest.BodyPublishers.ofString(new Gson().toJson(epic2)))
                .header("content-type", "application/json")
                .uri(urlForRequest)
                .build();
        var response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Assertions.assertEquals(400, response.statusCode());
    }

    @Test //PUT
    void test9checkContextWithPutRequestIfEpicNotFoundShouldReturnStatus404 ()
            throws IOException, InterruptedException {
        Epic epic1 = new Epic(); //epic will not add to storage
        Epic epic2 = new Epic(); //epic will not add to storage
        epic2.setName("Epic2 name");
        epic2.setDescription("Epic2 description");

        URI urlForRequest = URI.create(url.toString().concat(epic1.getUuid().toString()));
        request = HttpRequest.newBuilder()
                .PUT(HttpRequest.BodyPublishers.ofString(new Gson().toJson(epic2)))
                .header("content-type", "application/json")
                .uri(urlForRequest)
                .build();
        var response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Assertions.assertEquals(404, response.statusCode());
    }

    @Test //DELETE
    void test10checkContextWithDeleteRequestIf1EpicThereIsInStorageShouldReturnStatus204AndRemoveEpicFromStorage()
            throws IOException, InterruptedException {
        Epic epic1 = new Epic();

        manager.addEpic(epic1);
        URI urlForRequest = URI.create(url.toString().concat(epic1.getUuid().toString()));
        request = HttpRequest.newBuilder()
                .DELETE()
                .uri(urlForRequest)
                .build();

        Assertions.assertEquals(1, manager.getListEpics().size());

        var response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Assertions.assertEquals(0, manager.getListEpics().size());
        Assertions.assertEquals("", response.body());
        Assertions.assertEquals(204, response.statusCode());
    }

    @Test //DELETE
    void test11checkContextWithDeleteRequestIfTryToUseUrlWithWrongFormatUuidShouldReturnStatus400 ()
            throws IOException, InterruptedException {
        Epic epic1 = new Epic();

        manager.addEpic(epic1);
        URI urlForRequest = URI.create(url.toString().concat("123456")); //wrong format uuid
        request = HttpRequest.newBuilder()
                .DELETE()
                .uri(urlForRequest)
                .build();

        var response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Assertions.assertEquals(1, manager.getListEpics().size());
        Assertions.assertEquals(400, response.statusCode());
    }

    @Test //DELETE
    void test12checkContextWithDeleteRequestIfTryToUseUrlWithUuidWhichNotExistingInStorageShouldReturnStatus404 ()
            throws IOException, InterruptedException {
        Epic epic1 = new Epic();
        Epic epic2 = new Epic(); //won't add to storage

        manager.addEpic(epic1);
        URI urlForRequest = URI.create(url.toString().concat(epic2.getUuid().toString())); //not existing in storage
        request = HttpRequest.newBuilder()
                .DELETE()
                .uri(urlForRequest)
                .build();

        var response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Assertions.assertEquals(1, manager.getListEpics().size());
        Assertions.assertEquals(404, response.statusCode());
    }
}