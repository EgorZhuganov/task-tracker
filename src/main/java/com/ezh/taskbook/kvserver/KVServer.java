package com.ezh.taskbook.kvserver;

import com.ezh.taskbook.kvserver.handler.LoadHandler;
import com.ezh.taskbook.kvserver.handler.RegisterHandler;
import com.ezh.taskbook.kvserver.handler.SaveHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

public class KVServer {

    private final int port;
    private final String apiKey;
    private final HttpServer server;
    private final Map<String, String> data = new HashMap<>();

    public KVServer(int port) throws IOException {
        this.port = port;
        apiKey = generateApiKey();
        server = HttpServer.create(new InetSocketAddress("localhost", port), 0);
        createAllContext();
    }

    public void start() {
        System.out.println("Server starts (KVServer) on the port " + port);
        System.out.println("Open in the browser http://localhost:" + port + "/");
        System.out.println("API_KEY: " + apiKey);
        server.start();
    }

    public void stop() {
        server.stop(0);
        System.out.println("Server (KVServer) was stopped on the port " + port);
    }

    private String generateApiKey() {
        return "" + System.currentTimeMillis();
    }

    private void createAllContext() {
        server.createContext("/register", new RegisterHandler(apiKey));
        server.createContext("/save", new SaveHandler(apiKey, data));
        server.createContext("/load", new LoadHandler(apiKey, data));
    }
}
