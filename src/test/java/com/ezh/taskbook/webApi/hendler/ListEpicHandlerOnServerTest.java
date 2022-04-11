package com.ezh.taskbook.webApi.hendler;

import com.ezh.taskbook.manager.FileBackedTasksManager;
import com.ezh.taskbook.manager.TaskManager;
import com.ezh.taskbook.task.Epic;
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

class ListEpicHandlerOnServerTest {

    private final TaskManager manager = new FileBackedTasksManager(new File("test.txt"));
    private final HttpTaskServer server = new HttpTaskServer(manager, 8080);
    private final HttpClient client = HttpClient.newHttpClient();
    private HttpRequest request;
    private final URI url = URI.create("http://localhost:8080/tasks/epic");

    @BeforeEach
    void beforeEach() { server.start(); }

    @AfterEach
    void afterEach() { server.stop(); }

    @Test //GET
    void test1_checkContextWithGetRequestIfAddTwoEpicToStorageShouldReturnStatusCode200 ()
            throws IOException, InterruptedException {
        Epic epic1 = new Epic();
        Epic epic2 = new Epic();
        manager.addEpic(epic1);
        manager.addEpic(epic2);

        request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();

        var response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Assertions.assertEquals(200, response.statusCode());
        Assertions.assertTrue(response.headers().allValues("content-type").contains("application/json"));
    }

    @Test //DELETE
    void test2_checkContextWithDeleteRequestIfAddTwoEpicShouldReturnCode204WithoutBodyAndStorageWithoutEpics ()
            throws IOException, InterruptedException {
        Epic epic1 = new Epic();
        Epic epic2 = new Epic();
        manager.addEpic(epic1);
        manager.addEpic(epic2);

        request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();

        Assertions.assertEquals(2, manager.getListEpics().size());

        var response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Assertions.assertEquals(0, manager.getListEpics().size());
        Assertions.assertEquals("", response.body());
        Assertions.assertEquals(204, response.statusCode());
    }
}