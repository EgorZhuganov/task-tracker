package com.ezh.taskbook.manager;

import com.ezh.taskbook.kvserver.KVServer;
import com.ezh.taskbook.webApi.HttpTaskServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;

class HttpTaskManagerIntegrationTest {
    //TODO To be continued
    private KVServer kvServer; //port 8078
    private HttpTaskServer taskServer1;
    private HttpTaskServer taskServer2;
    private HttpTaskManager manager1;
    private HttpTaskManager manager2;

    @BeforeEach
    public void beforeEach() throws IOException {
        kvServer = new KVServer();
        manager1 = new HttpTaskManager(URI.create("http://localhost:8080/"), "8080");
        manager2 = new HttpTaskManager(URI.create("http://localhost:8081/"), "8081");
        taskServer1 = new HttpTaskServer(manager1, 8080);
        taskServer2 = new HttpTaskServer(manager2, 8081);
        kvServer.start();
        taskServer1.start();
        taskServer2.start();
    }

    @AfterEach
    public void afterEach() {
        taskServer1.stop();
        taskServer2.stop();
        kvServer.stop();
    }

    @Test
    public void test1_() {
    }

    @Test
    public void test2_() {
    }

    @Test
    public void test3_() {
    }


}