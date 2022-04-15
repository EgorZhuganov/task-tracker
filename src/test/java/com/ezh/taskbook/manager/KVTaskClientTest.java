package com.ezh.taskbook.manager;

import com.ezh.taskbook.kvserver.KVServer;
import com.google.gson.Gson;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;

import static org.junit.jupiter.api.Assertions.*;

class KVTaskClientTest {

    private KVServer kvServer; //port 8078
    private KVTaskClient client;
    private URI url = URI.create("http://localhost:8078/");

    @BeforeEach
    void beforeEach() {
        try {
            kvServer = new KVServer(8078);
            kvServer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        client = new KVTaskClient(url);
    }

    @AfterEach
    void afterEach() { kvServer.stop(); }

    @Test
    void test1putTryPutSomeJsonThrowKVClientWithKeyShouldAddJsonToKVServer () {
        String json = new Gson().toJson("Im JSON");
        client.put("My-most-secure-key", json);
    }

    @Test
    void test2loadPutAndLoadJsonThrowKVClientShouldGetJsonByKeyFromKVStorage () {
        String json = "Im JSON";
        client.put("My-most-secure-key", new Gson().toJson(json));

        String jsonAfterLoad = client.load("My-most-secure-key");
        Assertions.assertEquals(json, new Gson().fromJson(jsonAfterLoad, String.class));
    }
}