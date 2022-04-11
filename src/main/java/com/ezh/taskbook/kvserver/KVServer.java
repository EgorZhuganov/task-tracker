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

    public static final int PORT = 8078;
    private final String apiKey;
    private HttpServer server;
    private Map<String, String> data = new HashMap<>();

    public KVServer() throws IOException {
        apiKey = generateApiKey();
        server = HttpServer.create(new InetSocketAddress("localhost", PORT), 0);
        createAllContext();
    }

    public void start() {
        System.out.println("Server starts (KVServer) on the port " + PORT);
        System.out.println("Open in the browser http://localhost:" + PORT + "/");
        System.out.println("API_KEY: " + apiKey);
        server.start();
    }

    public void stop() {
        server.stop(0);
        System.out.println("Server (KVServer) was stopped on the port " + PORT);
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
