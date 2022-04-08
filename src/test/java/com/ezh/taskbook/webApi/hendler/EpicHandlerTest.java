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

class EpicHandlerTest {

    private final TaskManager manager = new FileBackedTasksManager(new File("test.txt"));
    private final HttpTaskServer server = new HttpTaskServer(manager,8080);
    private final HttpClient client = HttpClient.newHttpClient();
    private HttpRequest request;
    private final URI url = URI.create("http://localhost:8080/tasks/epic/");

    @BeforeEach
    public void beforeEach() {
        server.start();
    }

    @AfterEach
    public void afterEach() {
        server.stop();
    }

    @Test //GET
    public void test1_checkContextWithGetRequestIfEpicWasSentShouldReturnEpicAsJsonAndStatusCode200 () throws IOException, InterruptedException {
        Epic epic1 = new Epic();

        manager.addEpic(epic1);

        URI urlForRequest = URI.create(url.toString().concat(epic1.getUuid().toString()));
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
    public void test2_checkContextWithGetRequestIfEpicWasntSentShouldReturnStatusCode400 () throws IOException, InterruptedException {
        Epic epic1 = new Epic();
        Epic epic2 = new Epic(); //Attention! Epic won't add to storage
        manager.addEpic(epic1);

        URI urlForRequest = URI.create(url.toString().concat(epic2.getUuid().toString())); //wrong uuid
        request = HttpRequest.newBuilder()
                .GET()
                .uri(urlForRequest)
                .build();
        var response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Assertions.assertEquals("", response.body());
        Assertions.assertEquals(400, response.statusCode());
    }

    @Test //POST
    public void test3_checkContextWithPostRequestAndEpicWhichHas2SubtasksShouldAddEpicWithSubtasksToStorageAndReturnCode204 ()
            throws IOException, InterruptedException, TaskNotFoundException {
        Epic epic1 = new Epic();
        Subtask subtask1 = new Subtask(epic1);
        Subtask subtask2 = new Subtask(epic1);
        epic1.getSubtaskList().add(subtask1);
        epic1.getSubtaskList().add(subtask2);

        request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(new Gson().toJson(epic1)))
                .header("Content-Type","application/json")
                .uri(url)
                .build();
        var response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Assertions.assertEquals(epic1.getUuid(), manager.getEpicByUuid(epic1.getUuid()).getUuid());
        Assertions.assertEquals(2, manager.getListSubtasks().size());
        Assertions.assertEquals("", response.body());
        Assertions.assertEquals(204, response.statusCode());

    }

    @Test //POST
    public void test4_checkContextWithPostRequestIfThereIsNoHeaderContentTypeShouldReturnStatusCode400  () throws IOException, InterruptedException {
        Epic epic1 = new Epic();
        Subtask subtask1 = new Subtask(epic1);
        Subtask subtask2 = new Subtask(epic1);
        epic1.getSubtaskList().add(subtask1);
        epic1.getSubtaskList().add(subtask2);

        request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(new Gson().toJson(epic1)))
                .uri(url)
                .build(); //There is not Content Type
        var response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(400, response.statusCode());
    }

    @Test //POST
    public void test5_checkContextWithPostRequestIfContentTypeIsWrongShouldReturnStatusCode400  () throws IOException, InterruptedException {
        Epic epic1 = new Epic();
        Subtask subtask1 = new Subtask(epic1);
        Subtask subtask2 = new Subtask(epic1);
        epic1.getSubtaskList().add(subtask1);
        epic1.getSubtaskList().add(subtask2);

        request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(new Gson().toJson(epic1)))
                .header("Content-Type","wrong value")
                .uri(url)
                .build(); //It has wrong Content Type
        var response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(400, response.statusCode());
    }

    @Test //POST
    public void test6_checkContextWithPostRequestIfAddOnlyOneEpicShouldReturnCode200AndReturnEpicFromStorage  ()
            throws IOException, InterruptedException, TaskNotFoundException {
        Epic epic1 = new Epic();

        request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(new Gson().toJson(epic1)))
                .header("Content-Type","application/json")
                .uri(url)
                .build();
        var response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Assertions.assertEquals(epic1.getUuid(), manager.getEpicByUuid(epic1.getUuid()).getUuid());
        Assertions.assertEquals(204, response.statusCode());
    }

    @Test //PUT
    public void test7_checkContextWithPutRequestShouldReturnStatus204AndChangeEpic1 ()
            throws IOException, InterruptedException, TaskNotFoundException {
        Epic epic1 = new Epic();
        Epic epic2 = new Epic();
        epic2.setName("Epic2 name");
        epic2.setDescription("Epic2 description");

        manager.addEpic(epic1);

        URI urlForRequest = URI.create(url.toString().concat(epic1.getUuid().toString()));
        request = HttpRequest.newBuilder()
                .PUT(HttpRequest.BodyPublishers.ofString(new Gson().toJson(epic2)))
                .header("Content-Type","application/json")
                .uri(urlForRequest)
                .build();
        var response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Assertions.assertEquals(epic2.getName(), manager.getEpicByUuid(epic1.getUuid()).getName());
        Assertions.assertEquals(epic2.getDescription(), manager.getEpicByUuid(epic1.getUuid()).getDescription());
        Assertions.assertEquals(204, response.statusCode());
    }

    @Test //DELETE
    public void test8_checkContextWithDeleteRequestShouldReturnStatus204AndRemoveAllEpics ()
            throws IOException, InterruptedException {
        Epic epic1 = new Epic();
        Epic epic2 = new Epic();

        manager.addEpic(epic1);
        manager.addEpic(epic2);

        request = HttpRequest.newBuilder()
                .DELETE()
                .uri(url)
                .build();
        var response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Assertions.assertEquals(0, manager.getListEpics().size());
        Assertions.assertEquals("", response.body());
        Assertions.assertEquals(204, response.statusCode());
    }
}