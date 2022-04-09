package com.ezh.taskbook.webApi.hendler;

import com.ezh.taskbook.exception.TasksIntersectionException;
import com.ezh.taskbook.manager.FileBackedTasksManager;
import com.ezh.taskbook.manager.TaskManager;
import com.ezh.taskbook.task.Epic;
import com.ezh.taskbook.task.SingleTask;
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
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

class AllTasksHandlerOnServerTest {

    private final TaskManager manager = new FileBackedTasksManager(new File("test.txt"));
    private final HttpTaskServer server = new HttpTaskServer(manager,8080);
    private final HttpClient client = HttpClient.newHttpClient();
    private HttpRequest request;
    private final URI url = URI.create("http://localhost:8080/tasks/");

    @BeforeEach
    public void beforeEach() {
        server.start();
    }

    @AfterEach
    public void afterEach() {
        server.stop();
    }

    @Test //GET
    public void test2_checkContextWithGetRequestIfPrioritizedTasksExistShouldReturnResponseAsJsonAndCode200()
            throws IOException, InterruptedException, TasksIntersectionException {
        SingleTask singleTask1 = new SingleTask();
        SingleTask singleTask2 = new SingleTask();
        Epic epic1 = new Epic();
        Subtask subtask1 = new Subtask(epic1);
        epic1.getSubtaskList().add(subtask1);

        subtask1.setStartTimeAndDuration(LocalDateTime.of(2021,12,31,23,59), Duration.ofDays(10));
        singleTask1.setStartTimeAndDuration(LocalDateTime.of(2022,12,31,23,59), Duration.ofDays(10));
        singleTask1.setStartTimeAndDuration(LocalDateTime.of(2022,10,31,23,59), Duration.ofDays(10));

        manager.addSingleTask(singleTask1);
        manager.addSingleTask(singleTask2);
        manager.addEpicWithSubtask(epic1);

        request = HttpRequest.newBuilder()
                .GET()
                .uri(url)
                .build();
        var response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Assertions.assertEquals(200, response.statusCode());
        Assertions.assertTrue(response.headers().allValues("content-type").contains("application/json"));
    }

    @Test //DELETE
    public void test3_checkContextWithDeleteRequestIfWasReceivedNonGetRequestShouldReturnNoBodyAndCode400() throws IOException, InterruptedException, TasksIntersectionException {
        SingleTask singleTask1 = new SingleTask();
        SingleTask singleTask2 = new SingleTask();
        singleTask1.setStartTimeAndDuration(LocalDateTime.of(2022,12,31,23,59), Duration.ofDays(10));
        singleTask1.setStartTimeAndDuration(LocalDateTime.of(2022,10,31,23,59), Duration.ofDays(10));

        manager.addSingleTask(singleTask1);
        manager.addSingleTask(singleTask2);

        request = HttpRequest.newBuilder()
                .DELETE()
                .uri(url)
                .build();
        var response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Assertions.assertEquals("", response.body());
        Assertions.assertEquals(400, response.statusCode());
    }
}