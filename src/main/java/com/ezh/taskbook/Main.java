package com.ezh.taskbook;

import com.ezh.taskbook.manager.HttpTaskManager;
import com.ezh.taskbook.webApi.HttpTaskServer;

import java.net.URI;

public class Main {

    public static void main(String[] args) {

        HttpTaskServer myServer1 = new HttpTaskServer(
                new HttpTaskManager(URI.create("http://localhost:8078/"), "8080"), 8080);
        myServer1.start();

        HttpTaskServer myServer2 = new HttpTaskServer(
                new HttpTaskManager(URI.create("http://localhost:8078/"), "8081"), 8081);
        myServer2.start();

        HttpTaskServer myServer3 = new HttpTaskServer(
                new HttpTaskManager(URI.create("http://localhost:8078/"), "8082"), 8082);
        myServer3.start();

        myServer1.stop();
        myServer2.stop();
        myServer3.stop();
    }
}
